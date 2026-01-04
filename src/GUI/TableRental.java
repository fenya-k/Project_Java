package GUI;

import API.Car;
import API.ManagementService;
import API.Rental;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TableRental extends JDialog implements StyleEditRemoveHistory, StyleTwoOptions {
    private final ManagementService service;
    private final JTable table;
    private final DefaultTableModel model;

    // Input fields for search filtering
    private final JTextField codeField, plateField, afmField, employeeField;

    public TableRental(JFrame parent, ManagementService service) {
        super(parent, "Λίστα Ενοικιάσεων και Επιστροφές", true);
        this.service = service;

        // DIALOG
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // SEARCH PANEL
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 6, 10, 5));

        // LABELS
        fieldsPanel.add(new JLabel("Κωδικός:"));
        fieldsPanel.add(new JLabel("Πινακίδα:"));
        fieldsPanel.add(new JLabel("ΑΦΜ Πελάτη:"));
        fieldsPanel.add(new JLabel("Υπάλληλος:"));

        // FIELDS
        codeField = new JTextField();
        fieldsPanel.add(codeField);
        plateField = new JTextField();
        fieldsPanel.add(plateField);
        afmField = new JTextField();
        fieldsPanel.add(afmField);
        employeeField = new JTextField();
        fieldsPanel.add(employeeField);

        searchPanel.add(fieldsPanel);

        // SEARCH BUTTONS
        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton buttonSearch = new JButton("Αναζήτηση");
        styleButtonOptionOne(buttonSearch);
        buttonSearch.addActionListener(e -> performSearch());

        JButton buttonReset = new JButton("Καθαρισμός Φίλτρων");
        styleButtonOptionTwo(buttonReset);
        buttonReset.addActionListener(e -> {
            codeField.setText("");
            plateField.setText("");
            afmField.setText("");
            employeeField.setText("");
            performSearch();
        });

        searchButtonPanel.add(buttonSearch);
        searchButtonPanel.add(Box.createHorizontalStrut(26));
        searchButtonPanel.add(buttonReset);
        searchPanel.add(searchButtonPanel);

        add(searchPanel, BorderLayout.NORTH);

        // TABLE
        String[] columns = {"Κωδικός", "Πινακίδα", "ΑΦΜ Πελάτη", "Έναρξη", "Λήξη", "Υπάλληλος"};

        //Define model with read-only cells
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

        // Panel to hold the table with padding
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        tablePanel.add(scroll, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // BUTTONS
        JButton editButton = new JButton("Επεξεργασία");
        styleButtonEdit(editButton);
        editButton.addActionListener(e -> edit());

        JButton removeButton = new JButton("Επιστροφή");
        styleButtonRemove(removeButton);
        removeButton.addActionListener(e -> returnCar());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0)); //padding
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(32));
        buttonPanel.add(removeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

 private void performSearch() {
        // Parse inputs (handle empty strings)
        int code = isEmpty(codeField) ? -1 : Integer.parseInt(codeField.getText().trim());
        String plate = isEmpty(plateField) ? null : plateField.getText().trim();
        String afm = isEmpty(afmField) ? null : afmField.getText().trim();
        String username = isEmpty(employeeField) ? null : employeeField.getText().trim();

        // Call backend search logic
        ArrayList<Rental> results = service.getRentalManager().search(code, plate, afm, username);

        // Clear existing rows
        model.setRowCount(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        // Repopulate table with search results
        if (results != null) {
            for (Rental rental : results) {
                String rPlate = (rental.getRentCar() != null) ? rental.getRentCar().getPlate() : "-";
                String rAfm = (rental.getClient() != null) ? rental.getClient().getAFM() : "-";
                String rUser = (rental.getEmployee() != null) ? rental.getEmployee().getUsername() : "-";
                String start = (rental.getStartDate() != null) ? rental.getStartDate().format(formatter) : "-";
                String end = (rental.getEndDate() != null) ? rental.getEndDate().format(formatter) : "-";

                model.addRow(new Object[]{
                        rental.getRentCode(),
                        rPlate,
                        rAfm,
                        start,
                        end,
                        rUser
                });
            }
        }
    }

    // Helper to check if a text field is effectively empty
    private boolean isEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    private void edit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Επιλέξτε ενοικίαση!",
                    "Δεν επιλέχθηκε ενοικίαση.",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int code = (int) model.getValueAt(row, 0);
        Rental rental = service.getRentalManager().findByCode(code);

        if (rental != null) {
            EditRentalDialog dialog = new EditRentalDialog(this, service, rental);
            dialog.setVisible(true);
            refreshTable();
        }
    }

    private void returnCar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Επιλέξτε ενοικίαση!",
                    "Δεν επιλέχθηκε ενοικίαση.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Είστε σίγουροι ότι θέλετε να επιστρέψετε το όχημα?",
                "Επιβεβαίωση επιστροφής", JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        int rentCode = (int) model.getValueAt(row, 0);

        Rental selected = null;
        for (Rental rental : service.getRentalManager().getList()) {
            if (rental.getRentCode() == rentCode) {
                selected = rental;
                break;
            }
        }
        if (selected != null) {
            service.returnCar(selected);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Το όχημα επιστράφηκε επιτυχώς");
        } else {
            JOptionPane.showMessageDialog(this, "Σφάλμα κατά την επιστροφή", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 private void refreshTable() {
        model.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        ArrayList<Rental> list = service.getRentalManager().getList();

        for (Rental rental : list) {
            try {
                // 1. Έλεγχος αν λείπουν βασικά αντικείμενα (για να μην σκάσει)
                String plate = (rental.getRentCar() != null) ? rental.getRentCar().getPlate() : "ΑΓΝΩΣΤΟ";
                String afm = (rental.getClient() != null) ? rental.getClient().getAFM() : "ΑΓΝΩΣΤΟ";
                String user = (rental.getEmployee() != null) ? rental.getEmployee().getUsername() : "ΑΓΝΩΣΤΟ";

                // 2. Έλεγχος Ημερομηνιών
                String start = (rental.getStartDate() != null) ? rental.getStartDate().format(formatter) : "-";
                String end = (rental.getEndDate() != null) ? rental.getEndDate().format(formatter) : "-";

                // 3. Προσθήκη γραμμής
                model.addRow(new Object[]{
                        rental.getRentCode(),
                        plate,
                        afm,
                        start,
                        end,
                        user
                });
            } catch (Exception e) {
                System.err.println("Σφάλμα κατά την εμφάνιση της ενοικίασης " + rental.getRentCode());
                e.printStackTrace();
            }
        }
    }
}
