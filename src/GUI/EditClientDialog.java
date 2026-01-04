package GUI;

import API.Client;
import API.ManagementService;

import javax.swing.*;
import java.awt.*;

/**
 * A modal dialog window for editing the details of an existing client.
 * <p>
 * This class pre-fills the form fields with the current data of the selected {@link Client}.
 * It allows the user to modify contact details like Name, Surname, Phone, and Email.
 * <b>Note:</b> The Tax ID (AFM) field is set to read-only mode as it serves as the
 * unique identifier for the client.
 * </p>
 * Implements {@link StyleAddCancel} for consistent UI styling.
 */
public class EditClientDialog extends JDialog implements StyleAddCancel {

    /**
     * Reference to the backend service.
     */
    private final ManagementService service;

    /**
     * The specific client object being edited.
     */
    private final Client client;

    // UI Input Fields
    private final JTextField nameField;
    private final JTextField surnameField;
    private final JTextField afmField;
    private final JTextField phoneField;
    private final JTextField emailField;

    /**
     * Constructs the Edit Client Dialog.
     * Initializes the UI, populates fields with existing client data, locks the AFM field,
     * and sets up action listeners for saving changes.
     *
     * @param parent  The parent dialog (usually TableClient) from which this dialog was opened.
     * @param service The ManagementService instance for data operations.
     * @param client  The Client object containing the data to be edited.
     */
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


        //read-only
        addClientPanel.add(new JLabel("ΑΦΜ")); //δεν αλλάζει-AFM
        afmField = new JTextField();
        afmField.setText(client.getAFM());
        afmField.setEditable(false);
        afmField.setBackground(new Color(204, 204, 204));
        addClientPanel.add(afmField);

        addClientPanel.add(new JLabel("Όνομα"));  //Name
        nameField = new JTextField();
        nameField.setText(client.getName());
        addClientPanel.add(nameField);

        addClientPanel.add(new JLabel("Επίθετο"));  //Surname
        surnameField = new JTextField();
        surnameField.setText(client.getSurname());
        addClientPanel.add(surnameField);

        addClientPanel.add(new JLabel("Τηλέφωνο"));  //Phone
        phoneField = new JTextField();
        phoneField.setText(client.getPhone());
        addClientPanel.add(phoneField);

        addClientPanel.add(new JLabel("Email"));  //Email
        emailField = new JTextField();
        emailField.setText(String.valueOf(client.getEmail()));
        addClientPanel.add(emailField);


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

    /**
     * Collects the modified data and saves the changes.
     * <p>
     * Retrieves text from the editable fields and calls
     * {@link ManagementService#editExistingClient(String, String, String, String, String)}.
     * Displays a success message if the update is successful, or an error message otherwise.
     * </p>
     *
     * @param afm The Tax ID of the client (used as the key for the update operation).
     */
    private void saveClient(String afm) {

        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        // Pass updated data to service
        String check = service.editExistingClient(name, surname, afm, phone, email);

        if (check.startsWith("Επιτυχής")) {
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

