package GUI;

import API.ManagementService;

import javax.swing.*;
import java.awt.*;

/**
 * A modal dialog window for adding a new client to the system.
 * <p>
 * This class provides a form interface for the user to input client details
 * (name, surname, AFM, phone, email). It handles the input collection
 * and communicates with the {@link ManagementService} to validate and store
 * the new client.
 * </p>
 * Implements {@link StyleAddCancel} for consistent UI styling.
 */
public class AddClientDialog extends JDialog implements StyleAddCancel {

    /**
     * Reference to the backend service for data handling.
     */
    private final ManagementService service;

    // UI Input Fields
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField afmField;
    private JTextField phoneField;
    private JTextField emailField;

    /**
     * Constructs the Add Client Dialog.
     * Initializes the user interface, sets up the layout grid, applies styling,
     * and defines the action listeners for saving and canceling.
     *
     * @param parent  The parent JFrame (MainFrame) to center the dialog relative to.
     * @param service The ManagementService instance used to perform the add operation.
     */
    public AddClientDialog(JFrame parent, ManagementService service) {
        super(parent, "Προσθήκη Νέου Πελάτη", true);
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

        addClientPanel.add(new JLabel("ΑΦΜ"));  //AFM
        afmField = new JTextField();
        addClientPanel.add(afmField);

        addClientPanel.add(new JLabel("Τηλέφωνο"));  //Phone
        phoneField = new JTextField();
        addClientPanel.add(phoneField);

        addClientPanel.add(new JLabel("Email"));  //Email
        emailField = new JTextField();
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

    /**
     * Collects input data from the form fields and attempts to save the new client.
     * <p>
     * Retrieves text inputs, trims whitespace, and calls
     * {@link ManagementService#addNewClient(String, String, String, String, String)}.
     * Displays a success message and closes the dialog upon successful addition,
     * or an error message if the operation fails (e.g., duplicate AFM).
     * </p>
     */
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
