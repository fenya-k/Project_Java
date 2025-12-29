package GUI;

import API.Employee;
import API.ManagementService;

import javax.swing.*;
import java.awt.*;

public class AddEmployeeDialog extends JDialog implements StyleAddCancel{
        private final ManagementService service;

        private JTextField nameField;
        private JTextField surnameField;
        private JTextField usernameField;
        private JTextField emailField;
        private JPasswordField passwordField;

        public AddEmployeeDialog(JFrame parent, ManagementService service) {
            super(parent, "Προσθήκη Υπαλλήλου", true);
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

            addClientPanel.add(new JLabel("Username"));
            usernameField = new JTextField();
            addClientPanel.add(usernameField);

            addClientPanel.add(new JLabel("Email"));
            emailField = new JTextField();
            addClientPanel.add(emailField);

            addClientPanel.add(new JLabel("Password"));
            passwordField = new JPasswordField();
            addClientPanel.add(passwordField);

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
            addButton.addActionListener(e -> save());

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

        private void save() {

            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String user = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || surname.isEmpty() || user.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "'Ολα τα πεδία πρέπει να είναι συμπληρωμένα", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (service.getEmployeeManager().findByUsername(user)!=null) {
                JOptionPane.showMessageDialog(this, "Το username υπάρχει ήδη", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Employee newEmployee= new Employee(name,surname,user,email,password);
            service.getEmployeeManager().add(newEmployee);
            JOptionPane.showMessageDialog(this, "Ο υπάλληλος προστέθηκε επιτυχώς!");
                dispose();
        }

}
