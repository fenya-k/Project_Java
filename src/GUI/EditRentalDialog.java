package GUI;

import API.Car;
import API.Client;
import API.Rental;
import API.Employee;
import API.ManagementService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditRentalDialog extends JDialog implements StyleAddCancel {
    private final ManagementService service;
    private final Rental rental;

    private final JTextField plateField;
    private final JTextField afmField;
    private final JTextField startField;
    private final JTextField endField;
    private final JTextField usernameField;

    public EditRentalDialog(JDialog parent, ManagementService service, Rental rental) {
        super(parent, "Επεξεργασία Ενοικίασης", true);
        this.service = service;
        this.rental = rental;

        // DIALOG
        setSize(600, 460);
        setLocationRelativeTo(parent);

        // PANEL
        JPanel addRentalPanel = new JPanel(new GridLayout(0, 2, 7, 16));
        addRentalPanel.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        addRentalPanel.add(new JLabel("Πινακίδα"));
        plateField = new JTextField();
        plateField.setText(rental.getRentCar().getPlate());
        plateField.setEditable(false);
        plateField.setBackground(new Color(204, 204, 204));
        addRentalPanel.add(plateField);

        addRentalPanel.add(new JLabel("ΑΦΜ Πελάτη:"));
        afmField = new JTextField();
        afmField.setText(rental.getClient().getAFM());
        addRentalPanel.add(afmField);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        addRentalPanel.add(new JLabel("Ημ/νία Έναρξης (ηη/μμ/εεεε):"));
        startField = new JTextField();
        if (rental.getStartDate() != null) {
            startField.setText(rental.getStartDate().format(formatter));
        }
        addRentalPanel.add(startField);

        addRentalPanel.add(new JLabel("Ημ/νία Λήξης (ηη/μμ/εεεε):"));
        endField = new JTextField();
        if (rental.getEndDate() != null) {
            endField.setText(rental.getEndDate().format(formatter));
        }
        addRentalPanel.add(endField);

        addRentalPanel.add(new JLabel("Username Υπαλλήλου:"));
        usernameField = new JTextField();
        usernameField.setText(rental.getEmployee().getName());
        addRentalPanel.add(usernameField);

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
        JButton addButton = new JButton("Αποθήκευση");
        styleButtonAdd(addButton);
        addButton.addActionListener(e -> saveRental(rental.getRentCode()));

        JButton cancelButton = new JButton("Ακύρωση");
        styleButtonCancel(cancelButton);
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 40, 0)); //padding
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalStrut(32));
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveRental(int code) {

        String plate = plateField.getText().trim();
        String afm = afmField.getText().trim();
        String start = startField.getText().trim();
        String end = endField.getText().trim();
        String emp = usernameField.getText().trim();

        Car car = service.getCarManager().findByPlate(plate);
        Client client = service.getClientManager().findByAFM(afm);
        Employee employee = service.getEmployeeManager().findByUsername(emp);
        LocalDate startDate = null;
        LocalDate endDate = null;

        try {
            // Προσπαθούμε να μετατρέψουμε το κείμενο σε ημερομηνία
            startDate = LocalDate.parse(start);
            endDate = LocalDate.parse(end);
        } catch (DateTimeParseException e) {
            // Αν ο χρήστης έγραψε λάθος ημερομηνία
            JOptionPane.showMessageDialog(this,
                    "Η ημερομηνία πρέπει να είναι της μορφής ηη/μμ/εεεε (π.χ. 01/01/2026)",
                    "Σφάλμα Ημερομηνίας",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String check = service.editExistingRental(code, car, client, startDate, endDate, employee);

        if (check.equals("Επιτυχής καταχώρηση.")) {
            JOptionPane.showMessageDialog(this, "Επιτυχής ενημέρωση.",
                    "Η ενοικίαση ενημερώθηκε επιτυχώς!",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, check,
                    "Η ενημέρωση απέτυχε!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}