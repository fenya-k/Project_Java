package GUI;

import API.Client;
import API.ManagementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TableClient extends JDialog implements StyleEditRemoveHistory {
    private final ManagementService service;
    private JTable table;
    private DefaultTableModel model;

    public TableClient(JFrame parent, ManagementService service){
        super(parent, "Διαχείριση Πελατών", true); // true = modal
        this.service = service;

        // DIALOG
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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

        if(client!=null){
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
                    "Επιτυχής αφαίρεση.",
                    "Ο πελάτης αφαιρέθηκε επιτυχώς!",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Σφάλμα.",
                    "Η καταχώρηση απέτυχε!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void history() {

    }

    private void refreshTable() {

        model.setRowCount(0);

        ArrayList<Client> list = service.getClientManager().getList();
        for (Client client : list) {
            model.addRow(new Object[]{
                    client.getName(), client.getSurname(), client.getAFM(), client.getPhone(), client.getEmail()
            });
        }
    }
}
