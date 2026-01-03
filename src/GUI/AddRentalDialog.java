package GUI;

import API.*;

import javax.swing.*;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AddRentalDialog extends JDialog implements StyleAddCancel {

    private final ManagementService service;
    private final Employee currentUser;

    private JComboBox<String> carCombo;
    private JComboBox<String> clientCombo;

    private JTextField startField;
    private JTextField endField;
    private JLabel empLabel;

    public AddRentalDialog(JFrame parent, ManagementService service, Employee currentUser) {
        super(parent, "Προσθήκη Νέας Ενοικίασης", true);
        this.service = service;
        this.currentUser = currentUser;

        // DIALOG
        setSize(600, 460);
        setLocationRelativeTo(parent);

        // PANEL
        JPanel addRentalPanel = new JPanel(new GridLayout(0, 2, 7, 16));
        addRentalPanel.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        //CAR SELECTION
        addRentalPanel.add(new JLabel("Επιλογή Οχήματος (Διαθέσιμα):"));
        carCombo = new JComboBox<>();
        populateCars();
        addRentalPanel.add(carCombo);

        //CLIENT SELECTION
        addRentalPanel.add(new JLabel("Επιλογή Πελάτη (ΑΦΜ - Όνομα):"));
        clientCombo = new JComboBox<>();
        populateClients();
        addRentalPanel.add(clientCombo);

        addRentalPanel.add(new JLabel("Ημ/νία Έναρξης (YYYY-MM-DD):"));
        startField = new JTextField();
        addRentalPanel.add(startField);

        addRentalPanel.add(new JLabel("Ημ/νία Λήξης (YYYY-MM-DD):"));
        endField = new JTextField();
        addRentalPanel.add(endField);

        addRentalPanel.add(new JLabel("Υπάλληλος:"));
        empLabel = new JLabel(currentUser.getUsername() + " (" + currentUser.getSurname() + ")");
        empLabel.setForeground(Color.BLUE);
        addRentalPanel.add(empLabel);

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
            if (c instanceof JLabel) {
                c.setFont(boldFont);
            }
        }

        for (Component c : addRentalPanel.getComponents()) {
            if (c instanceof JComboBox) {
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

    private void populateClients() {
        ArrayList<Client> clients = service.getClientManager().getList();
        for (Client client : clients) {
            clientCombo.addItem(client.getAFM() + " - " + client.getName() + " " + client.getSurname());
        }
        if (clients.isEmpty()) {
            clientCombo.addItem("Δεν υπάρχουν πελάτες");
            clientCombo.setEnabled(false);
        }
    }

    private void populateCars() {
        ArrayList<Car> cars = service.getCarManager().getList();
        boolean found = false;
        for (Car car : cars) {
            if (car.isAvailable()) {
                carCombo.addItem(car.getPlate() + " (" + car.getBrand() + " " + car.getModel() + ")");
                found = true;
            }
        }
        if (!found) {
            carCombo.addItem("Δεν υπάρχουν διαθέσιμα οχήματα");
            carCombo.setEnabled(false);
        }
    }

    private void saveRental() {
        try {
            if (!carCombo.isEnabled() || !clientCombo.isEnabled()) {
                JOptionPane.showMessageDialog(this, "Δεν υπάρχουν διαθέσιμα στοιχεία", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String carSelection = (String) carCombo.getSelectedItem();
            String plate = carSelection.split(" ")[0];

            String clientSelection = (String) clientCombo.getSelectedItem();
            String afm = clientSelection.split(" - ")[0];

            String startString = startField.getText().trim();
            String endString = endField.getText().trim();

            Employee employee = this.currentUser;
            Car car = service.getCarManager().findByPlate(plate);
            Client client = service.getClientManager().findByAFM(afm);

            if (car == null || client == null) {
                JOptionPane.showMessageDialog(this, "Σφάλμα επιλογής", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
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

            JOptionPane.showMessageDialog(this,
                    "Η ενοικίαση ολοκληρώθηκε",
                    "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (DateTimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Λάθος μορφή ημερομηνίας! (YYYY-MM-DD)",
                    "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Σφάλμα:" + ex.getMessage(),
                    "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }
}
