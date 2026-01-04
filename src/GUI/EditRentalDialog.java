package GUI;

import API.Car;
import API.Client;
import API.Rental;
import API.Employee;
import API.ManagementService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


public class EditRentalDialog extends JDialog implements StyleAddCancel {

     private final ManagementService service;
     private final Rental rental;

    // UI Components
    private final JTextField plateField;
    private final JComboBox<String> clientCombo;
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

        //read-only
        addRentalPanel.add(new JLabel("Πινακίδα(Δεν αλλάζει):"));  //Plate
        plateField = new JTextField();
        plateField.setText(rental.getRentCar().getPlate());
        plateField.setEditable(false);
        plateField.setBackground(new Color(204, 204, 204));
        addRentalPanel.add(plateField);

        addRentalPanel.add(new JLabel("Πελάτης:")); //Client Selection(Dropdown)
        clientCombo = new JComboBox<>();
        populateClients();
        addRentalPanel.add(clientCombo);

        addRentalPanel.add(new JLabel("Ημ/νία Έναρξης (YYYY-MM-DD):"));  //Start Date
        startField = new JTextField();
        if (rental.getStartDate() != null) {
            startField.setText(rental.getStartDate().toString());
        }
        addRentalPanel.add(startField);

        addRentalPanel.add(new JLabel("Ημ/νία Λήξης (YYYY-MM-DD):"));  //End Date
        endField = new JTextField();
        if (rental.getEndDate() != null) {
            endField.setText(rental.getEndDate().toString());
        }
        addRentalPanel.add(endField);


        //read-only
        addRentalPanel.add(new JLabel("Username Υπαλλήλου:"));  //Employee Username
        usernameField = new JTextField();
        usernameField.setText(rental.getEmployee().getUsername());
        usernameField.setEditable(false);
        usernameField.setBackground(new Color(230, 230, 230));
        addRentalPanel.add(usernameField);

        //STYLING
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

        for (Component c : addRentalPanel.getComponents()) {
            if (c instanceof JComboBox) {
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

      private void populateClients() {
        ArrayList<Client> clients = service.getClientManager().getList();
        String currentAfm = rental.getClient().getAFM();

        for (Client client : clients) {
            clientCombo.addItem(client.getAFM() + " - " + client.getName() + " " + client.getSurname());

            // Check if this is the current client of the rental
            if (client.getAFM().equals(currentAfm)) {
                clientCombo.setSelectedItem(client.getAFM() + " - " + client.getName() + " " + client.getSurname());
            }
        }

    }

 private void saveRental(int code) {

        String start = startField.getText().trim();
        String end = endField.getText().trim();
        String emp = usernameField.getText().trim();

        Car car = rental.getRentCar();

        //Validate Client Selection
        String clientSel = (String) clientCombo.getSelectedItem();

        if (clientSel == null) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ επιλέξτε πελάτη", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Extract AFM and find Client object
        String afm = clientSel.split(" - ")[0];
        Client client = service.getClientManager().findByAFM(afm);

        Employee employee = service.getEmployeeManager().findByUsername(emp);
        LocalDate startDate = null;
        LocalDate endDate = null;

        try {
            //Parse Dates
            startDate = LocalDate.parse(start);
            endDate = LocalDate.parse(end);

            // Logic check: End date cannot be before Start date
            if (endDate.isBefore(startDate)) {
                JOptionPane.showMessageDialog(this, "Η ημερομηνία λήξης είναι πριν την έναρξη", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Η ημερομηνία πρέπει να είναι της μορφής YYYY-MM-DD", "Σφάλμα Ημερομηνίας", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Perform update via service
        String check = service.editExistingRental(code, car, client, startDate, endDate, employee);

        if (check.startsWith("Επιτυχής") || check.contains("Success")) {
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