package GUI;

import API.Employee;
import API.ManagementService;

import javax.swing.*;
import java.awt.*;

public class EditEmployeeDialog extends JDialog implements StyleAddCancel {
    private final ManagementService service;
    private final Employee employee;

    private final JTextField nameField;
    private final JTextField surnameField;
    private final JTextField usernameField;
    private final JTextField emailField;

    public EditEmployeeDialog(JDialog parent, ManagementService service, Employee employee) {
        super(parent, "Επεξεργασία Υπαλλήλου", true);
        this.service = service;
        this.employee = employee;

        // DIALOG
        setSize(600, 460);
        setLocationRelativeTo(parent);

        // PANEL
        JPanel addEmployeePanel = new JPanel(new GridLayout(0, 2, 7, 16));
        addEmployeePanel.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        addEmployeePanel.add(new JLabel("Παρατσούκλι"));
        usernameField = new JTextField();
        usernameField.setText(employee.getUsername());
        usernameField.setEditable(false);
        usernameField.setBackground(new Color(204, 204, 204));
        addEmployeePanel.add(usernameField);

        addEmployeePanel.add(new JLabel("Όνομα"));
        nameField = new JTextField();
        nameField.setText(employee.getName());
        addEmployeePanel.add(nameField);

        addEmployeePanel.add(new JLabel("Επίθετο"));
        surnameField = new JTextField();
        surnameField.setText(employee.getSurname());
        addEmployeePanel.add(surnameField);

        addEmployeePanel.add(new JLabel("Email"));
        emailField = new JTextField();
        emailField.setText(String.valueOf(employee.getEmail()));
        addEmployeePanel.add(emailField);

        for (Component c : addEmployeePanel.getComponents()) {
            if (c instanceof JLabel) {
                c.setFont(boldFont);
            }
        }

        for (Component c : addEmployeePanel.getComponents()) {
            if (c instanceof JTextField) {
                c.setFont(regularFont);
            }
        }

        add(addEmployeePanel, BorderLayout.CENTER);

        //BUTTONS
        JButton addButton = new JButton("Αποθήκευση");
        styleButtonAdd(addButton);
        addButton.addActionListener(e -> saveEmployee(employee.getUsername()));

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

    private void saveEmployee(String username) {

        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String email = emailField.getText().trim();

        String check = service.editExistingEmployee(username, name, surname, email);

        if (check.equals("Επιτυχής καταχώρηση.")) {
            JOptionPane.showMessageDialog(this, "Επιτυχής ενημέρωση.",
                    "Ο υπάλληλος ενημερώθηκε επιτυχώς!",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, check,
                    "Η ενημέρωση απέτυχε!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}