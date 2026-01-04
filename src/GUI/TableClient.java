package GUI;

import API.Client;
import API.ManagementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * A management dialog for the Client Database.
 * <p>
 * This class provides a comprehensive view of all registered clients.
 * Key features include:
 * <ul>
 * <li><b>Search Panel:</b> Allows filtering clients by Name, Surname, AFM (Tax ID), Phone, or Email.</li>
 * <li><b>Data Table:</b> Displays client contact details in a tabular format.</li>
 * <li><b>Action Buttons:</b> Provides options to <b>Edit</b>, <b>Remove</b>, or view the rental <b>History</b> of a selected client.</li>
 * </ul>
 * </p>
 * Implements {@link StyleEditRemoveHistory} for action buttons and {@link StyleTwoOptions} for search buttons.
 */
public class TableClient extends JDialog implements StyleEditRemoveHistory, StyleTwoOptions {

    /**
     * Reference to the backend service.
     */
    private final ManagementService service;

    // UI Components
    private final JTable table;
    private final DefaultTableModel model;

    // Search Filter Components
    private final JTextField nameField, surnameField, afmField, phoneField, emailField;

    /**
     * Constructs the Client Management Table Dialog.
     * Initializes the window, builds the search filter panel, sets up the JTable,
     * and configures the action buttons.
     *
     * @param parent  The parent JFrame (MainFrame).
     * @param service The ManagementService instance for data operations.
     */
    public TableClient(JFrame parent, ManagementService service) {
        super(parent, "Διαχείριση Πελατών", true); // true = modal
        this.service = service;

        // DIALOG
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // SEARCH PANEL
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 4, 10, 5));

        // LABELS
        fieldsPanel.add(new JLabel("Όνομα:"));
        fieldsPanel.add(new JLabel("Επώνυμο:"));
        fieldsPanel.add(new JLabel("ΑΦΜ:"));
        fieldsPanel.add(new JLabel("Τηλέφωνο:"));
        fieldsPanel.add(new JLabel("Email:"));

        // FIELDS
        nameField = new JTextField();
        fieldsPanel.add(nameField);
        surnameField = new JTextField();
        fieldsPanel.add(surnameField);
        afmField = new JTextField();
        fieldsPanel.add(afmField);
        phoneField = new JTextField();
        fieldsPanel.add(phoneField);
        emailField = new JTextField();
        fieldsPanel.add(emailField);

        searchPanel.add(fieldsPanel);

        // SEARCH BUTTONS
        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton buttonSearch = new JButton("Αναζήτηση");
        styleButtonOptionOne(buttonSearch);
        buttonSearch.addActionListener(e -> performSearch());

        JButton buttonReset = new JButton("Καθαρισμός Φίλτρων");
        styleButtonOptionTwo(buttonReset);
        buttonReset.addActionListener(e -> {
            nameField.setText("");
            surnameField.setText("");
            afmField.setText("");
            phoneField.setText("");
            emailField.setText("");
            performSearch();
        });

        searchButtonPanel.add(buttonSearch);
        searchButtonPanel.add(Box.createHorizontalStrut(26));
        searchButtonPanel.add(buttonReset);
        searchPanel.add(searchButtonPanel);

        add(searchPanel, BorderLayout.NORTH);

        // TABLE
        String[] columns = {"Όνομα", "Επίθετο", "ΑΦΜ", "Τηλέφωνο", "Email"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }  // Disable direct editing
        };

        table = new JTable(model);
        table.setRowHeight(24);
        table.getTableHeader().setFont(boldFont);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Επιλογή μίας γραμμής

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        tablePanel.add(scroll, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // BUTTONS
        JButton editButton = new JButton("Επεξεργασία");
        styleButtonEdit(editButton);
        editButton.addActionListener(e -> edit());

        JButton removeButton = new JButton("Αφαίρεση");
        styleButtonRemove(removeButton);
        removeButton.addActionListener(e -> remove());

        JButton historyButton = new JButton("Ιστορικό");
        styleButtonHistory(historyButton);
        historyButton.addActionListener(e -> history());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0)); //padding
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(32));
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createHorizontalStrut(32));
        buttonPanel.add(historyButton);

        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    /**
     * Executes the search query based on the filters in the search panel.
     * <p>
     * Collects text from all fields, calls {@link API.ClientManager#search},
     * and updates the table model with the results.
     * </p>
     */
    private void performSearch() {
        String name = isEmpty(nameField) ? null : nameField.getText().trim();
        String surname = isEmpty(surnameField) ? null : surnameField.getText().trim();
        String afm = isEmpty(afmField) ? null : afmField.getText().trim();
        String phone = isEmpty(phoneField) ? null : phoneField.getText().trim();
        String email = isEmpty(emailField) ? null : emailField.getText().trim();

        // Perform search via Manager
        ArrayList<Client> results = service.getClientManager().search(name, surname, afm, phone);

        // Update Table
        ((DefaultTableModel) table.getModel()).setRowCount(0);

        if (results != null) {
            for (Client c : results) {
                ((DefaultTableModel) table.getModel()).addRow(new Object[]{
                        c.getName(),
                        c.getSurname(),
                        c.getAFM(),
                        c.getPhone(),
                        c.getEmail()
                });
            }
        }
    }

    /**
     * Helper method to check if a text field is empty.
     */
    private boolean isEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    /**
     * Opens the {@link EditClientDialog} for the selected client.
     * <p>
     * Retrieves the AFM from the selected table row, finds the Client object,
     * and passes it to the edit dialog.
     * </p>
     */
    private void edit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Επιλέξτε πελάτη!",
                    "Δεν επιλέχθηκε πελάτης.",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String afm = (String) model.getValueAt(row, 2);
        Client client = service.getClientManager().findByAFM(afm);

        if (client != null) {
            EditClientDialog dialog = new EditClientDialog(this, service, client);
            dialog.setVisible(true);
            refreshTable();
        }
    }

    /**
     * Removes the selected client from the system.
     * <p>
     * Prompts the user for confirmation before deleting. Displays a success or error
     * message based on the operation result.
     * </p>
     */
    private void remove() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Επιλέξτε πελάτη!",
                    "Δεν επιλέχθηκε πελάτης.",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Είστε σίγουροι?",
                "Επιβεβαίωση Διαγραφής",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String afm = (String) model.getValueAt(row, 2);
        Client client = service.getClientManager().findByAFM(afm);

        boolean isRemoved = service.getClientManager().remove(client);

        if (isRemoved) {
            refreshTable();
            JOptionPane.showMessageDialog(this,
                    "Ο πελάτης αφαιρέθηκε επιτυχώς!",
                    "Επιτυχής αφαίρεση.",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Η καταχώρηση απέτυχε!",
                    "Σφάλμα.",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens the {@link HistoryClientDialog} to show the rental history of the selected client.
     */
    private void history() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Επιλέξτε πελάτη!",
                    "Δεν επιλέχθηκε πελάτης.",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String afm = (String) model.getValueAt(row, 2);
        Client client = service.getClientManager().findByAFM(afm);

        HistoryClientDialog dialog = new HistoryClientDialog(this, service, client);
        dialog.setVisible(true);

        refreshTable();
    }

    /**
     * Refreshes the table data by re-applying current search filters.
     */
    private void refreshTable() {
        performSearch();
    }
}