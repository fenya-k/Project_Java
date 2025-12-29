package GUI;

import API.*;

import javax.swing.*;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;

public class AddRentalDialog extends JDialog implements StyleAddCancel {

    private final ManagementService service;

    private JTextField empField;
    private JTextField startField;
    private JTextField endField;
    private JTextField plateField;
    private JTextField afmField;

    public AddRentalDialog(JFrame parent, ManagementService service) {
        super(parent, "Προσθήκη Νέου Ενοικίασης", true);
        this.service = service;

        // DIALOG
        setSize(600, 460);
        setLocationRelativeTo(parent);

        // PANEL
        JPanel addRentalPanel = new JPanel(new GridLayout(0, 2, 7, 16));
        addRentalPanel.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        addRentalPanel.add(new JLabel("Πινακίδα Οχήματος:"));
        plateField = new JTextField();
        addRentalPanel.add(plateField);

        addRentalPanel.add(new JLabel("ΑΦΜ Πελάτη:"));
        afmField = new JTextField();
        addRentalPanel.add(afmField);

        addRentalPanel.add(new JLabel("Ημ/νία Έναρξης (YYYY-MM-DD):"));
        startField = new JTextField();
        addRentalPanel.add(startField);

        addRentalPanel.add(new JLabel("Ημ/νία Λήξης (YYYY-MM-DD):"));
        endField = new JTextField();
        addRentalPanel.add(endField);

        addRentalPanel.add(new JLabel("Username Υπαλλήλου:"));
        empField = new JTextField();
        addRentalPanel.add(empField);


        for (Component c : addRentalPanel.getComponents()) {
            if (c instanceof JLabel) {
                c.setFont(boldFont);
            }
        }

        for (Component c : addRentalPanel.getComponents()) {
            if (c instanceof JTextField) {
                c.setFont(regularFont);
            }
        }

        add(addRentalPanel, BorderLayout.CENTER);

        //BUTTONS
        JButton saveButton = new JButton("Ενοικίαση");
        styleButtonAdd(saveButton);
        saveButton.addActionListener(e -> saveRental());

        JButton cancelButton = new JButton("Ακύρωση");
        styleButtonCancel(cancelButton);
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 40, 0)); //padding
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createHorizontalStrut(32));
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveRental() {
      try {
          String plate = plateField.getText().trim();
          String afm = afmField.getText().trim();
          String startString = startField.getText().trim();
          String endString = endField.getText().trim();
          String empUser = empField.getText().trim();

          if (plate.isEmpty() || afm.isEmpty() || empUser.isEmpty()) {
              JOptionPane.showMessageDialog(this, "'Ολα τα πεδία πρέπει να είναι συμπληρωμένα", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
              return;
          }

          Employee employee = service.getEmployeeManager().findByUsername(empUser);
          Car car = service.getCarManager().findByPlate(plate);
          Client client = service.getClientManager().findByAFM(afm);

          if (employee == null) {
              JOptionPane.showMessageDialog(this, "Δεν βρέθηκε υπάλληλος με αυτό το username", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
              return;
          }
          if (car == null) {
              JOptionPane.showMessageDialog(this, "Δεν βρέθηκε όχημα με αυτή την πινακίδα", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
              return;
          }
          if (!car.isAvailable()) {
              JOptionPane.showMessageDialog(this, "Το όχημα δεν είναι διαθέσιμο (Rented)", "Προσοχή", JOptionPane.WARNING_MESSAGE);
              return;
          }
          if (client == null) {
              JOptionPane.showMessageDialog(this, "Δεν βρέθηκε πελάτης με αυτό το ΑΦΜ", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
              return;
          }

          LocalDate start = LocalDate.parse(startString);
          LocalDate end = LocalDate.parse(endString);

          if (end.isBefore(start)) {
              JOptionPane.showMessageDialog(this, "Η ημερομηνία λήξης είναι πριν την έναρξη", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
              return;
          }

          Rental rental = new Rental(car, client, start, end, employee);
          service.rentCar(rental);

          JOptionPane.showMessageDialog(this, "Η ενοικίαση ολοκληρώθηκε", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
          dispose();
      }catch (DateTimeException ex){
          JOptionPane.showMessageDialog(this,"Λάθος μορφή ημερομηνίας! (YYYY-MM-DD)", "Σφάλμα",JOptionPane.ERROR_MESSAGE);
      }catch (Exception ex){
          JOptionPane.showMessageDialog(this,"Σφάλμα:"+ ex.getMessage(),"Σφάλμα",JOptionPane.ERROR_MESSAGE);
      }
    }
}
