package GUI;

import API.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class HistoryCarDialog extends JDialog implements StyleEditRemoveHistory, StyleTwoOptions {
    private final ManagementService service;
    private Car car;
    private JTable table;
    private DefaultTableModel model;

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
        JButton editButton = new JButton("Στοιχεία πελάτη");
        styleButtonOptionOne(editButton);
        editButton.addActionListener(e -> showClient());

        JButton removeButton = new JButton("Στοιχεία υπαλλήλου");
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
                    "ΑΦΜ: " + employee.getUsername() + "\n" +
                    "Email: " + employee.getEmail() + "\n";

            JOptionPane.showMessageDialog(this,
                    message,
                    "Πλήρη Στοιχεία Υπαλλήλου",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshTable() {

        model.setRowCount(0);

        ArrayList<Rental> list = car.returnList();
        for (Rental rental : list) {
            model.addRow(new Object[]{
                    rental.getRentCode(), rental.getClient().getSurname(),  rental.getClient().getAFM(), rental.getStartDate(), rental.getEndDate(), rental.getEmployee().getUsername()
            });
        }
    }
}
