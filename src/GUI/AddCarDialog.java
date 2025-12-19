package GUI;

import API.ManagementService;

import javax.swing.*;
import java.awt.*;

public class AddCarDialog extends JDialog implements StyleAddCancel {

    private final ManagementService service;
    private JLabel plateLabel;
    private JTextField plateField;
    private JLabel brandLabel;
    private JTextField brandField;
    private JLabel typeLabel;
    private JTextField typeField;
    private JLabel modelLabel;
    private JTextField modelField;
    private JLabel yearLabel;
    private JTextField yearField;
    private JLabel colourLabel;
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

        plateLabel = new JLabel("Πινακίδα");
        plateField = new JTextField();
        addCarPanel.add(plateLabel);
        addCarPanel.add(plateField);
        brandLabel = new JLabel("Μάρκα");
        brandField = new JTextField();
        addCarPanel.add(brandLabel);
        addCarPanel.add(brandField);
        typeLabel = new JLabel("Τύπος");
        typeField = new JTextField();
        addCarPanel.add(typeLabel);
        addCarPanel.add(typeField);
        modelLabel = new JLabel("Μοντέλο");
        modelField = new JTextField();
        addCarPanel.add(modelLabel);
        addCarPanel.add(modelField);
        yearLabel = new JLabel("Χρονολογία");
        yearField = new JTextField();
        addCarPanel.add(yearLabel);
        addCarPanel.add(yearField);
        colourLabel = new JLabel("Χρώμα");
        colorField = new JTextField();
        addCarPanel.add(colourLabel);
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

    }

}
