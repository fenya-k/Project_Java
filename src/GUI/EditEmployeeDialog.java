package GUI;

import API.Employee;
import API.ManagementService;

import javax.swing.*;
import java.awt.*;

/**
 * A modal dialog window for editing the details of an existing employee.
 * <p>
 * This class pre-fills the form fields with the current data of the selected {@link Employee}.
 * It allows the administrator to modify personal details like Name, Surname, and Email.
 * <b>Note:</b> The Username field is set to read-only mode as it serves as the unique
 * login identifier for the system.
 * </p>
 * Implements {@link StyleAddCancel} for consistent UI styling.
 */
public class EditEmployeeDialog extends JDialog implements StyleAddCancel {

    /**
     * Reference to the backend service.
     */
    private final ManagementService service;

    /**
     * The specific employee object being edited.
     */
    private final Employee employee;

    // UI Input Fields
    private final JTextField nameField;
    private final JTextField surnameField;
    private final JTextField usernameField;
    private final JTextField emailField;

    /**
     * Constructs the Edit Employee Dialog.
     * Initializes the UI, populates fields with existing employee data, locks the Username field,
     * and sets up action listeners for saving changes.
     *
     * @param parent   The parent dialog (usually TableEmployee) from which this dialog was opened.
     * @param service  The ManagementService instance for data operations.
     * @param employee The Employee object containing the data to be edited.
     */
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

        //read-only
        addEmployeePanel.add(new JLabel("Παρατσούκλι"));  //Username
        usernameField = new JTextField();
        usernameField.setText(employee.getUsername());
        usernameField.setEditable(false);
        usernameField.setBackground(new Color(204, 204, 204));
        addEmployeePanel.add(usernameField);

        addEmployeePanel.add(new JLabel("Όνομα"));  //Name
        nameField = new JTextField();
        nameField.setText(employee.getName());
        addEmployeePanel.add(nameField);

        addEmployeePanel.add(new JLabel("Επίθετο"));  //Surname
        surnameField = new JTextField();
        surnameField.setText(employee.getSurname());
        addEmployeePanel.add(surnameField);

        addEmployeePanel.add(new JLabel("Email"));  //Email
        emailField = new JTextField();
        emailField.setText(String.valueOf(employee.getEmail()));
        addEmployeePanel.add(emailField);


        //STYLING
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

    /**
     * Collects the modified data and saves the changes.
     * <p>
     * Retrieves text from the editable fields and calls
     * {@link ManagementService#editExistingEmployee(String, String, String, String)}.
     * Displays a success message if the update is successful, or an error message otherwise.
     * </p>
     *
     * @param username The unique username of the employee (used as the key for the update operation).
     */
    private void saveEmployee(String username) {

        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String email = emailField.getText().trim();

        // Pass updated data to service
        String check = service.editExistingEmployee(username, name, surname, email);

        if (check.startsWith("Επιτυχής")) {
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