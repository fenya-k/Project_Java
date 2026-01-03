package GUI;

import API.Client;
import API.ManagementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TableClient extends JDialog implements StyleEditRemoveHistory, StyleTwoOptions {
    private final ManagementService service;
    private final JTable table;
    private final DefaultTableModel model;

    // For search
    private final JTextField nameField, surnameField, afmField, phoneField, emailField;

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

        // LABELS //
        fieldsPanel.add(new JLabel("Όνομα:"));
        fieldsPanel.add(new JLabel("Επώνυμο:"));
        fieldsPanel.add(new JLabel("ΑΦΜ:"));
        fieldsPanel.add(new JLabel("Τηλέφωνο:"));
        fieldsPanel.add(new JLabel("Email:"));

        // FIELDS //
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

        // SEARCH BUTTONS //
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
            }
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

    private void performSearch() {
        String name = isEmpty(nameField) ? null : nameField.getText().trim();
        String surname = isEmpty(surnameField) ? null : surnameField.getText().trim();
        String afm = isEmpty(afmField) ? null : afmField.getText().trim();
        String phone = isEmpty(phoneField) ? null : phoneField.getText().trim();
        String email = isEmpty(emailField) ? null : emailField.getText().trim();

        // ΚΛΗΣΗ ΤΗΣ SEARCH ΤΟΥ MANAGER
        ArrayList<Client> results = service.getClientManager().search(name, surname, afm, phone);

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

    private boolean isEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

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

    private void refreshTable() {
        performSearch();
    }
}