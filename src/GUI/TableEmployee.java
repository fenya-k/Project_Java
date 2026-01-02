package GUI;

import API.Client;
import API.Employee;
import API.ManagementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TableEmployee extends JDialog implements StyleEditRemoveHistory {
    private final ManagementService service;
    private JTable table;
    private DefaultTableModel model;

    public TableEmployee(JFrame parent, ManagementService service) {
        super(parent, "Λίστα Υπαλλήλων", true); // true = modal
        this.service = service;

        // DIALOG
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // TABLE
        String[] columns = {"Username", "Όνομα", "Επίθετο", "Email"};

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

        JButton removeButton = new JButton("Διαγραφή");
        styleButtonRemove(removeButton);
        removeButton.addActionListener(e -> remove());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0)); //padding
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(32));
        buttonPanel.add(removeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void edit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Επιλέξτε υπάλληλο!",
                    "Δεν επιλέχθηκε υπάλληλος.",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = (String) model.getValueAt(row, 0);
        Employee employee = service.getEmployeeManager().findByUsername(username);

        if (employee != null) {
            EditEmployeeDialog dialog = new EditEmployeeDialog(this, service, employee);
            dialog.setVisible(true);
            refreshTable();
        }
    }

    private void remove() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Είστε σίγουροι?",
                "Επιβεβαίωση Διαγραφής",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String username = (String) model.getValueAt(row, 0);
        Employee employee = service.getEmployeeManager().findByUsername(username);
        if (employee != null) {
            service.getEmployeeManager().remove(employee);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Ο υπάλληλος διαγράφηκε.");
        }
    }

    private void refreshTable() {

        model.setRowCount(0);

        ArrayList<Employee> list = service.getEmployeeManager().getList();
        for (Employee employee : list) {
            model.addRow(new Object[]{
                    employee.getUsername(), employee.getName(), employee.getSurname(), employee.getEmail()
            });
        }
    }
}
