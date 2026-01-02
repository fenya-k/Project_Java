package GUI;

import API.Client;
import API.ManagementService;

import javax.swing.*;
import java.awt.*;

public class EditClientDialog extends JDialog implements StyleAddCancel {
    private final ManagementService service;
    private final Client client;

    private final JTextField nameField;
    private final JTextField surnameField;
    private final JTextField afmField;
    private final JTextField phoneField;
    private final JTextField emailField;

    public EditClientDialog(JDialog parent, ManagementService service, Client client) {
        super(parent, "Επεξεργασία Πελάτη", true);
        this.service = service;
        this.client = client;

        // DIALOG
        setSize(600, 460);
        setLocationRelativeTo(parent);

        // PANEL
        JPanel addClientPanel = new JPanel(new GridLayout(0, 2, 7, 16));
        addClientPanel.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        addClientPanel.add(new JLabel("ΑΦΜ")); //δεν αλλάζει
        afmField = new JTextField();
        afmField.setText(client.getAFM());
        afmField.setEditable(false);
        afmField.setBackground(new Color(204, 204, 204));
        addClientPanel.add(afmField);

        addClientPanel.add(new JLabel("Όνομα"));
        nameField = new JTextField();
        nameField.setText(client.getName());
        addClientPanel.add(nameField);

        addClientPanel.add(new JLabel("Επίθετο"));
        surnameField = new JTextField();
        surnameField.setText(client.getSurname());
        addClientPanel.add(surnameField);

        addClientPanel.add(new JLabel("Τηλέφωνο"));
        phoneField = new JTextField();
        phoneField.setText(client.getPhone());
        addClientPanel.add(phoneField);

        addClientPanel.add(new JLabel("Email"));
        emailField = new JTextField();
        emailField.setText(String.valueOf(client.getEmail()));
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
        addButton.addActionListener(e -> saveClient(client.getAFM()));

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
    private void saveClient(String afm) {

        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        String check = service.editExistingClient(name, surname, afm, phone, email);

        if (check.equals("Επιτυχής καταχώρηση.")) {
            JOptionPane.showMessageDialog(this, "Επιτυχής ενημέρωση.",
                    "Ο πελάτης ενημερώθηκε επιτυχώς!",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, check,
                    "Η ενημέρωση απέτυχε!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

