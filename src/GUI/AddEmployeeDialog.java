package GUI;

import API.Employee;
import API.ManagementService;

import javax.swing.*;
import java.awt.*;

/**
 * A modal dialog window for registering a new employee (user) in the system.
 * <p>
 * Allows the administrator to create a new user account by providing personal details
 * and login credentials (username, password). It includes validation to ensure
 * all fields are filled and the username is unique.
 * </p>
 */
public class AddEmployeeDialog extends JDialog implements StyleAddCancel {

    /**
     * Reference to the backend service for data handling.
     */
    private final ManagementService service;

    // UI Input Fields
    private final JTextField nameField;
    private final JTextField surnameField;
    private final JTextField usernameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;


    /**
     * Constructs the Add Employee Dialog.
     * Sets up the input form for employee details and credentials.
     *
     * @param parent  The parent JFrame.
     * @param service The ManagementService instance for data operations.
     */
    public AddEmployeeDialog(JFrame parent, ManagementService service) {
        super(parent, "Προσθήκη Υπαλλήλου", true);
        this.service = service;

        // DIALOG
        setSize(600, 460);
        setLocationRelativeTo(parent);

        // PANEL
        JPanel addClientPanel = new JPanel(new GridLayout(0, 2, 7, 16));
        addClientPanel.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));


        // LABELS AND TEXTFIELDS
        addClientPanel.add(new JLabel("Όνομα"));  //Name
        nameField = new JTextField();
        addClientPanel.add(nameField);

        addClientPanel.add(new JLabel("Επίθετο"));  //Surname
        surnameField = new JTextField();
        addClientPanel.add(surnameField);

        addClientPanel.add(new JLabel("Email"));  //Email
        emailField = new JTextField();
        addClientPanel.add(emailField);

        addClientPanel.add(new JLabel("Username"));  //Username
        usernameField = new JTextField();
        addClientPanel.add(usernameField);

        addClientPanel.add(new JLabel("Password"));  //Password
        passwordField = new JPasswordField();
        addClientPanel.add(passwordField);

        //STYLING
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

    /**
     * Validates input and creates a new Employee account.
     * <p>
     * Checks if any fields are empty and if the username already exists.
     * If validation passes, creates a new {@link Employee} object and adds it
     * to the system via the EmployeeManager.
     * </p>
     */
    private void save() {

        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String email = emailField.getText().trim();
        String user = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (name.isEmpty() || surname.isEmpty() || user.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "'Ολα τα πεδία πρέπει να είναι συμπληρωμένα", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (service.getEmployeeManager().findByUsername(user) != null) {
            JOptionPane.showMessageDialog(this, "Το username υπάρχει ήδη", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Employee newEmployee = new Employee(name, surname, user, email, password);
        service.getEmployeeManager().add(newEmployee);
        JOptionPane.showMessageDialog(this, "Ο υπάλληλος προστέθηκε επιτυχώς!");
        dispose();
    }

}
