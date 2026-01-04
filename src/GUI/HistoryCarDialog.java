package GUI;

import API.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * A modal dialog window that displays the rental history of a specific vehicle.
 * <p>
 * This class presents a table view of all past and current rentals associated with
 * the selected {@link Car}. It provides functionality to select a specific rental record
 * and view detailed information about the <b>Client</b> or the <b>Employee</b> involved
 * in that transaction.
 * </p>
 * Implements {@link StyleEditRemoveHistory} and {@link StyleTwoOptions} for interface consistency.
 */
public class HistoryCarDialog extends JDialog implements StyleEditRemoveHistory, StyleTwoOptions {

    /**
     * Reference to the backend service.
     */
    private final ManagementService service;

    //** Reference to the backend service. */
    private Car car;

    // UI Components
    private JTable table;
    private DefaultTableModel model;

    /**
     * Constructs the History Car Dialog.
     * Sets up the JTable to display rental data (Code, Client Surname, AFM, Dates, Employee)
     * and initializes buttons for viewing entity details.
     *
     * @param parent  The parent dialog (usually TableCar).
     * @param service The ManagementService instance for data retrieval.
     * @param car     The specific Car object whose history is being requested.
     */
    public HistoryCarDialog(JDialog parent, ManagementService service, Car car) {
        super(parent, "Ιστορικό οχήματος", true); // true = modal
        this.service = service;
        this.car = car;

        // DIALOG
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // TABLE
        String[] columns = {"Κωδικός", "Επίθετο Πελάτη", " AΦΜ", "Έναρξη", "Λήξη", "Υπάλληλος"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            } // Disable editing in history view
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
        JButton editButton = new JButton("Στοιχεία πελάτη");  //Show Client Details
        styleButtonOptionOne(editButton);
        editButton.addActionListener(e -> showClient());

        JButton removeButton = new JButton("Στοιχεία υπαλλήλου");  //Show Employee Details
        styleButtonOptionTwo(removeButton);
        removeButton.addActionListener(e -> showEmployee());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0)); //padding
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(32));
        buttonPanel.add(removeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    /**
     * Displays detailed information about the Client involved in the selected rental.
     * <p>
     * Retrieves the AFM from the selected table row, searches for the Client via
     * the service, and displays a popup with their full contact details (Name, Phone, Email).
     * </p>
     */
    private void showClient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Επιλέξτε ενοικίαση!",
                    "Δεν επιλέχθηκε ενοικίαση.",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String afm = (String) model.getValueAt(row, 2);
        Client client = service.getClientManager().findByAFM(afm);

        if (client != null) {
            String message = "Όνομα: " + client.getName() + "\n" +
                    "Επώνυμο: " + client.getSurname() + "\n" +
                    "ΑΦΜ: " + client.getAFM() + "\n" +
                    "Τηλέφωνο: " + client.getPhone() + "\n" +
                    "Email: " + client.getEmail() + "\n";

            JOptionPane.showMessageDialog(this,
                    message,
                    "Πλήρη Στοιχεία Πελάτη",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Displays detailed information about the Employee who processed the selected rental.
     * <p>
     * Retrieves the Username from the selected table row, searches for the Employee via
     * the service, and displays a popup with their details.
     * </p>
     */
    private void showEmployee() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Επιλέξτε ενοικίαση!",
                    "Δεν επιλέχθηκε ενοικίαση.",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = (String) model.getValueAt(row, 5);
        Employee employee = service.getEmployeeManager().findByUsername(username);

        if (employee != null) {
            String message = "Όνομα: " + employee.getName() + "\n" +
                    "Επώνυμο: " + employee.getSurname() + "\n" +
                    "Email: " + employee.getEmail() + "\n";

            JOptionPane.showMessageDialog(this,
                    message,
                    "Πλήρη Στοιχεία Υπαλλήλου",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Populates the table with the rental history list specific to this car.
     * <p>
     * It calls {@link Car#returnList()} to get the rentals associated with this vehicle
     * and adds them as rows to the table model.
     * </p>
     */
    private void refreshTable() {

        model.setRowCount(0);  // Clear existing rows

        ArrayList<Rental> list = car.returnList();
        for (Rental rental : list) {
            model.addRow(new Object[]{
                    rental.getRentCode(), rental.getClient().getSurname(), rental.getClient().getAFM(), rental.getStartDate(), rental.getEndDate(), rental.getEmployee().getUsername()
            });
        }
    }
}
