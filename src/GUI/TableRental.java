package GUI;

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
        super(parent,"Λίστα Ενοικιάσεων και Επιστροφές",true);
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

        // BUTTONS
        JButton returnButton = new JButton("Επιστροφή Οχήματος");
        styleButtonRemove(returnButton);
        returnButton.setText("Επιστροφή");
        returnButton.addActionListener(e -> returnCar());

        JButton closeButton = new JButton("Κλείσιμο");
        styleButtonEdit(closeButton);
        closeButton.addActionListener(e -> dispose());

        JPanel tablePanel = new JPanel(new GridBagLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        tablePanel.add(returnButton);
        tablePanel.add(Box.createHorizontalStrut(30));
        tablePanel.add(closeButton);

        add(tablePanel, BorderLayout.SOUTH);
        refreshTable();
    }

    private void returnCar(){
        int row=table.getSelectedRow();
        if(row==-1){
            JOptionPane.showMessageDialog(this,"Επιλέξτε ενοικίαση","Προσοχή",JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmation=JOptionPane.showConfirmDialog(this,"Επιβεβαίωση επιστροφής οχήματος;\\nΤο όχημα θα γίνει ξανά 'Διαθέσιμο'","Επιστροφή",JOptionPane.YES_NO_OPTION);

        if (confirmation!=JOptionPane.YES_OPTION){
            return;
        }

        int rentCode=(int) model.getValueAt(row,0);

        Rental selected=null;
        for (Rental rental: service.getRentalManager().getList()){
            if (rental.getRentCode()==rentCode){
                selected=rental;
                break;
            }
        }
        if (selected!=null) {
            service.returnCar(selected);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Το όχημα επιστράφηκε επιτυχώς");
        }else {
            JOptionPane.showMessageDialog(this,"Σφάλμα κατά την επιστροφή","Error",JOptionPane.ERROR_MESSAGE);
        }
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
