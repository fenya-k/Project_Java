package GUI;

import API.ManagementService;

import javax.swing.*;
import java.awt.*;

public class AddClientDialog extends JDialog implements StyleAddCancel {

    private final ManagementService service;

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField afmField;
    private JTextField phoneField;
    private JTextField emailField;

    public AddClientDialog(JFrame parent, ManagementService service) {
        super(parent, "Προσθήκη Νέου Πελάτη", true);
        this.service = service;

        // DIALOG
        setSize(600, 460);
        setLocationRelativeTo(parent);

        // PANEL
        JPanel addClientPanel = new JPanel(new GridLayout(0, 2, 7, 16));
        addClientPanel.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        addClientPanel.add(new JLabel("Όνομα"));
        nameField = new JTextField();
        addClientPanel.add(nameField);

        addClientPanel.add(new JLabel("Επίθετο"));
        surnameField = new JTextField();
        addClientPanel.add(surnameField);

        addClientPanel.add(new JLabel("ΑΦΜ"));
        afmField = new JTextField();
        addClientPanel.add(afmField);

        addClientPanel.add(new JLabel("Τηλέφωνο"));
        phoneField = new JTextField();
        addClientPanel.add(phoneField);

        addClientPanel.add(new JLabel("Email"));
        emailField = new JTextField();
        addClientPanel.add(emailField);

        for (Component c : addClientPanel.getComponents()) {
            if (c instanceof JLabel) {
                c.setFont(boldFont);
            }
        }

        for (Component c : addClientPanel.getComponents()) {
            if (c instanceof JTextField) {
                c.setFont(regularFont);
            }
        }

        add(addClientPanel, BorderLayout.CENTER);

        //BUTTONS
        JButton addButton = new JButton("Αποθήκευση");
        styleButtonAdd(addButton);
        addButton.addActionListener(e -> saveClient());

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

    private void saveClient() {

            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String afm = afmField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            String check = service.addNewClient(name, surname, afm, phone, email);

            if (check.equals("Επιτυχής καταχώρηση.")) {
                JOptionPane.showMessageDialog(this, check,
                        "Ο πελάτης προστέθηκε επιτυχώς!",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, check,
                        "Η καταχώρηση απέτυχε!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
