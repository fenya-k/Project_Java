package GUI;

import API.Client;
import API.Employee;
import API.ManagementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * A management dialog for the Employee (User) Database.
 * <p>
 * This class provides a tabular view of all registered system users (employees).
 * Unlike other management tables, this view is simplified (no search filters) as the
 * number of employees is typically small.
 * <br>
 * It provides administrative functions to:
 * <ul>
 * <li><b>Edit:</b> Modify employee details (Name, Surname, Email).</li>
 * <li><b>Delete:</b> Remove an employee account from the system.</li>
 * </ul>
 * </p>
 * Implements {@link StyleEditRemoveHistory} for consistent button styling.
 */
public class TableEmployee extends JDialog implements StyleEditRemoveHistory {

    /**
     * Reference to the backend service.
     */
    private final ManagementService service;

    // UI Components
    private JTable table;
    private DefaultTableModel model;

    /**
     * Constructs the Employee Management Table Dialog.
     * Initializes the window, sets up the JTable with employee columns,
     * and configures the Edit and Remove buttons.
     *
     * @param parent  The parent JFrame (MainFrame).
     * @param service The ManagementService instance for data operations.
     */
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

    /**
     * Opens the {@link EditEmployeeDialog} for the selected employee.
     * <p>
     * Retrieves the Username (unique ID) from the selected row, finds the
     * Employee object, and opens the edit form.
     * </p>
     */
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

    /**
     * Permanently deletes the selected employee account.
     * <p>
     * Prompts for confirmation before calling {@link API.EmployeeManager#remove}.
     * Refreshes the table upon success.
     * </p>
     */
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

    /**
     * Refreshes the table data by retrieving the full list of employees
     * from the manager.
     */
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
