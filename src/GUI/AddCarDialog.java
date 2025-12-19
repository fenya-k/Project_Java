package GUI;

import API.ManagementService;

import javax.swing.*;
import java.awt.*;

public class AddCarDialog extends JDialog implements StyleAddCancel {

    private final ManagementService service;

    private JTextField plateField;
    private JTextField brandField;
    private JTextField typeField;
    private JTextField modelField;
    private JTextField yearField;
    private JTextField colorField;

    public AddCarDialog(JFrame parent, ManagementService service) {
        super(parent, "Προσθήκη Νέου Οχήματος", true); // true = modal
        this.service = service;

        // DIALOG
        setSize(600, 460);
        setLocationRelativeTo(parent);

        // PANEL
        JPanel addCarPanel = new JPanel(new GridLayout(0, 2, 7, 16));
        addCarPanel.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        addCarPanel.add(new JLabel("Πινακίδα"));
        plateField = new JTextField();
        addCarPanel.add(plateField);

        addCarPanel.add(new JLabel("Μάρκα"));
        brandField = new JTextField();
        addCarPanel.add(brandField);

        addCarPanel.add(new JLabel("Τύπος"));
        typeField = new JTextField();
        addCarPanel.add(typeField);

        addCarPanel.add(new JLabel("Μοντέλο"));
        modelField = new JTextField();
        addCarPanel.add(modelField);

        addCarPanel.add(new JLabel("Χρονολογία"));
        yearField = new JTextField();
        addCarPanel.add(yearField);

        addCarPanel.add(new JLabel("Χρώμα"));
        colorField = new JTextField();
        addCarPanel.add(colorField);

        for (Component c : addCarPanel.getComponents()) {
            if (c instanceof JLabel) {
                c.setFont(boldFont);
            }
        }

        for (Component c : addCarPanel.getComponents()) {
            if (c instanceof JTextField) {
                c.setFont(regularFont);
            }
        }

        add(addCarPanel, BorderLayout.CENTER);

        //BUTTONS
        JButton addButton = new JButton("Αποθήκευση");
        styleButtonAdd(addButton);
        addButton.addActionListener(e -> saveCar());

        JButton cancelButton = new JButton("Ακύρωση");
        styleButtonCancel(cancelButton);
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 40, 0)); //padding
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalStrut(32));
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveCar() {

        String plate = plateField.getText().trim();
        String brand = brandField.getText().trim();
        String model = modelField.getText().trim();
        String type = typeField.getText().trim();
        String year = yearField.getText().trim();
        String colour = colorField.getText().trim();

        String check = service.addNewCar(plate, brand, type, model, year, colour);

        if (check.equals("Επιτυχής καταχώρηση.")){
            JOptionPane.showMessageDialog(this, check, "Το όχημα προστέθηκε επιτυχώς!", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this, check, "Η καταχώρηση απέτυχε!", JOptionPane.ERROR_MESSAGE);
        }
    }
}

//να βαλω τη λιστα με ατ αυτοκινητα και τους πελατες
//να εχει διαγραφη και επεξεργασια
//προβολη ιστορικου