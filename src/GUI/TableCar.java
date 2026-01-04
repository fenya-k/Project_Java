package GUI;

import API.Car;
import API.ManagementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TableCar extends JDialog implements StyleEditRemoveHistory, StyleTwoOptions {

    private final ManagementService service;

    // UI Components
    private final JTable table;
    private final DefaultTableModel model;

    // Search Filter Components
    private final JTextField plateField, brandField, typeField, modelField, colorField;
    private final JComboBox<String> availabilityBox;

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

     private boolean isEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

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

    private void refreshTable() {
        performSearch();
    }
}
