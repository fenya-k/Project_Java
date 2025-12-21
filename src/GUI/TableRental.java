package GUI;

import API.Client;
import API.ManagementService;
import API.Rental;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TableRental extends JDialog implements StyleEditRemoveHistory {
    private final ManagementService service;
    private JTable table;
    private DefaultTableModel model;

    public TableRental(JFrame parent,ManagementService service){
        super(parent,"Λίστα Ενοικιάσεων",true);
        this.service=service;

        setSize(950,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // TABLE
        String[] columns = {"Κωδικός", "Πινακίδα", "ΑΦΜ Πελάτη", "Έναρξη", "Λήξη", "Υπάλληλος"};

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
        JButton closeButton = new JButton("Κλείσιμο");
        styleButtonEdit(closeButton);
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); //padding
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);
        refreshTable();
    }

    private void refreshTable() {

        model.setRowCount(0);

        ArrayList<Rental> list = service.getRentalManager().getList();
        for (Rental rental : list) {
            model.addRow(new Object[]{
                    rental.getRentCode(), rental.getRentCar().getPlate(), rental.getClient().getAFM(), rental.getStartDate(), rental.getEndDate(),rental.getEmployee().getUsername()
            });
        }
    }

}
