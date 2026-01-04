package GUI;

import API.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * A modal dialog window that displays the rental history of a specific client.
 * <p>
 * This class presents a table view of all past and current rentals associated with
 * the selected {@link Client}. It allows the user to select a specific transaction
 * and view detailed information about the <b>Car</b> that was rented or the
 * <b>Employee</b> who processed the rental.
 * </p>
 * Implements {@link StyleEditRemoveHistory} and {@link StyleTwoOptions} for interface consistency.
 */
public class HistoryClientDialog extends JDialog implements StyleEditRemoveHistory, StyleTwoOptions {

    /**
     * Reference to the backend service.
     */
    private final ManagementService service;

    /**
     * The specific client whose history is being displayed.
     */
    private Client client;

    // UI Components
    private JTable table;
    private DefaultTableModel model;

    /**
     * Constructs the History Client Dialog.
     * Sets up the JTable to display rental data (Code, Car Plate, Brand, Dates, Employee)
     * and initializes buttons for viewing related entity details.
     *
     * @param parent  The parent dialog (usually TableClient).
     * @param service The ManagementService instance for data retrieval.
     * @param client  The specific Client object whose history is being requested.
     */
    public HistoryClientDialog(JDialog parent, ManagementService service, Client client) {
        super(parent, "Ιστορικό πελάτη", true); // true = modal
        this.service = service;
        this.client = client;

        // DIALOG
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // TABLE
        String[] columns = {"Κωδικός", "Πινακίδα", "Mάρκα", "Έναρξη", "Λήξη", "Υπάλληλος"};

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
        JButton editButton = new JButton("Στοιχεία οχήματος");  //Show Car Details
        styleButtonOptionOne(editButton);
        editButton.addActionListener(e -> showCar());

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
     * Displays detailed information about the Car involved in the selected rental.
     * <p>
     * Retrieves the License Plate from the selected table row, searches for the Car via
     * the service, and displays a popup with its full details (Brand, Model, Type, Color, etc.).
     * </p>
     */
    private void showCar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Επιλέξτε ενοικίαση!",
                    "Δεν επιλέχθηκε ενοικίαση.",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String plate = (String) model.getValueAt(row, 1);
        Car car = service.getCarManager().findByPlate(plate);

        //Check if car exists
        if (car != null) {
            String message = "Πινακίδα: " + car.getPlate() + "\n" +
                    "Μάρκα: " + car.getBrand() + "\n" +
                    "Μοντέλο: " + car.getModel() + "\n" +
                    "Τύπος: " + car.getType() + "\n" +
                    "Χρώμα: " + car.getColor() + "\n" +
                    "Έτος: " + car.getYear();

            JOptionPane.showMessageDialog(this,
                    message,
                    "Πλήρη Στοιχεία Οχήματος",
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
     * Populates the table with the rental history list specific to this client.
     * <p>
     * It calls {@link Client#returnList()} to get the past rentals associated with
     * this client and adds them as rows to the table model.
     * </p>
     */
    private void refreshTable() {

        model.setRowCount(0);

        ArrayList<Rental> list = client.returnList();
        for (Rental rental : list) {
            model.addRow(new Object[]{
                    rental.getRentCode(), rental.getRentCar().getPlate(), rental.getRentCar().getBrand(), rental.getStartDate(), rental.getEndDate(), rental.getEmployee().getUsername()
            });
        }
    }
}
