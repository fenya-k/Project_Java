package GUI;

import API.Car;
import API.ManagementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TableCar extends JDialog implements StyleEditRemoveHistory {

    private final ManagementService service;
    private JTable table;
    private DefaultTableModel model;

    public TableCar(JFrame parent, ManagementService service) {
        super(parent, "Διαχείριση Οχημάτων", true); // true = modal
        this.service = service;

        // DIALOG
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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
        String plate = (String) model.getValueAt(row, 0);
        Car car = service.getCarManager().findByPlate(plate);

        HistoryCarDialog dialog = new HistoryCarDialog(this, service, car);
        dialog.setVisible(true);

        refreshTable();
    }

    private void refreshTable() {

        model.setRowCount(0);

        ArrayList<Car> list = service.getCarManager().getList();
        for (Car car : list) {
            model.addRow(new Object[]{
                    car.getPlate(), car.getBrand(), car.getType(), car.getModel(), car.getYear(), car.getColor(), car.getCarStatus()
            });
        }
    }
}
