package GUI;

import API.Car;
import API.ManagementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * A management dialog for the Vehicle Fleet.
 * <p>
 * This class provides a comprehensive view of all vehicles in the system.
 * Key features include:
 * <ul>
 * <li><b>Search Panel:</b> Allows filtering cars by plate, brand, type, model, color, and availability status.</li>
 * <li><b>Data Table:</b> Displays vehicle details in a tabular format.</li>
 * <li><b>Action Buttons:</b> Provides options to <b>Edit</b>, <b>Remove</b>, or view the <b>History</b> of a selected car.</li>
 * </ul>
 * </p>
 * Implements {@link StyleEditRemoveHistory} for action buttons and {@link StyleTwoOptions} for search buttons.
 */
public class TableCar extends JDialog implements StyleEditRemoveHistory, StyleTwoOptions {

    /**
     * Reference to the backend service.
     */
    private final ManagementService service;

    // UI Components
    private final JTable table;
    private final DefaultTableModel model;

    // Search Filter Components
    private final JTextField plateField, brandField, typeField, modelField, colorField;
    private final JComboBox<String> availabilityBox;

    /**
     * Constructs the Car Management Table Dialog.
     * Initializes the window, builds the search filter panel, sets up the JTable,
     * and configures the action buttons.
     *
     * @param parent  The parent JFrame (MainFrame).
     * @param service The ManagementService instance for data operations.
     */
    public TableCar(JFrame parent, ManagementService service) {
        super(parent, "Διαχείριση Οχημάτων", true); // true = modal
        this.service = service;

        // DIALOG
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // SEARCH PANEL
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 6, 10, 5));

        // LABELS //
        fieldsPanel.add(new JLabel("Πινακίδα:"));
        fieldsPanel.add(new JLabel("Μάρκα:"));
        fieldsPanel.add(new JLabel("Τύπος:"));
        fieldsPanel.add(new JLabel("Μοντέλο:"));
        fieldsPanel.add(new JLabel("Χρώμα:"));
        fieldsPanel.add(new JLabel("Κατάσταση:"));

        // FIELDS //
        plateField = new JTextField();
        fieldsPanel.add(plateField);
        brandField = new JTextField();
        fieldsPanel.add(brandField);
        typeField = new JTextField();
        fieldsPanel.add(typeField);
        modelField = new JTextField();
        fieldsPanel.add(modelField);
        colorField = new JTextField();
        fieldsPanel.add(colorField);

        // Availability Dropdown
        String[] options = {"Όλα", "Διαθέσιμα", "Ενοικιασμένα"};
        availabilityBox = new JComboBox<>(options);
        fieldsPanel.add(availabilityBox);

        searchPanel.add(fieldsPanel);
        searchPanel.add(Box.createVerticalStrut(10));

        // SEARCH BUTTONS
        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton buttonSearch = new JButton("Αναζήτηση");
        styleButtonOptionOne(buttonSearch);
        buttonSearch.addActionListener(e -> performSearch());

        JButton buttonReset = new JButton("Καθαρισμός Φίλτρων");
        styleButtonOptionTwo(buttonReset);
        buttonReset.addActionListener(e -> {
            plateField.setText("");
            brandField.setText("");
            typeField.setText("");
            modelField.setText("");
            colorField.setText("");
            availabilityBox.setSelectedIndex(0);
            performSearch();
        });

        searchButtonPanel.add(buttonSearch);
        searchButtonPanel.add(Box.createHorizontalStrut(26));
        searchButtonPanel.add(buttonReset);
        searchPanel.add(searchButtonPanel);

        add(searchPanel, BorderLayout.NORTH);

        // TABLE
        String[] columns = {"Πινακίδα", "Μάρκα", "Τύπος", "Μοντέλο", "Έτος", "Χρώμα", "Κατάσταση"};

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

    /**
     * Executes the search query based on the filters in the search panel.
     * <p>
     * Collects text from all fields, determines the availability status from the dropdown,
     * calls {@link API.CarManager#search}, and updates the table model with the results.
     * </p>
     */
    private void performSearch() {
        String plate = isEmpty(plateField) ? null : plateField.getText().trim();
        String brand = isEmpty(brandField) ? null : brandField.getText().trim();
        String type = isEmpty(typeField) ? null : typeField.getText().trim();
        String model = isEmpty(modelField) ? null : modelField.getText().trim();
        String color = isEmpty(colorField) ? null : colorField.getText().trim();

        // Convert ComboBox index to Boolean (null=All, true=Available, false=Rented)
        Boolean available = null;
        if (availabilityBox.getSelectedIndex() == 1) available = true;
        if (availabilityBox.getSelectedIndex() == 2) available = false;

        // Perform search via Manager
        ArrayList<Car> results = service.getCarManager().search(plate, brand, type, model, color, available);

        // Update Table
        ((DefaultTableModel) table.getModel()).setRowCount(0);

        if (results != null) {
            for (Car c : results) {
                ((DefaultTableModel) table.getModel()).addRow(new Object[]{
                        c.getPlate(),
                        c.getBrand(),
                        c.getType(),
                        c.getModel(),
                        c.getYear(),
                        c.getColor(),
                        c.isAvailable() ? "Διαθέσιμο" : "Ενοικιασμένο"
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
     * Opens the {@link EditCarDialog} for the selected vehicle.
     * Validates that a row is selected before proceeding.
     */
    private void edit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Επιλέξτε όχημα!",
                    "Δεν επιλέχθηκε όχημα",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String plate = ((String) model.getValueAt(row, 0)).trim();
        Car car = service.getCarManager().findByPlate(plate);

        EditCarDialog dialog = new EditCarDialog(this, service, car);
        dialog.setVisible(true);

        refreshTable();
    }

    /**
     * Removes the selected vehicle from the system.
     * <p>
     * Prompts the user for confirmation before deleting. Displays a success or error
     * message based on the operation result.
     * </p>
     */
    private void remove() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Επιλέξτε όχημα!",
                    "Δεν επιλέχθηκε όχημα.",
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

        String plate = ((String) model.getValueAt(row, 0)).trim();
        Car car = service.getCarManager().findByPlate(plate);

        boolean isRemoved = service.getCarManager().remove(car);

        if (isRemoved) {
            refreshTable();
            JOptionPane.showMessageDialog(this,
                    "Το όχημα αφαιρέθηκε επιτυχώς!",
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
     * Opens the {@link HistoryCarDialog} to show the rental history of the selected vehicle.
     */
    private void history() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Επιλέξτε όχημα!",
                    "Δεν επιλέχθηκε όχημα",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String plate = ((String) model.getValueAt(row, 0)).trim();
        Car car = service.getCarManager().findByPlate(plate);

        HistoryCarDialog dialog = new HistoryCarDialog(this, service, car);
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
