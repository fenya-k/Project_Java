package GUI;

import API.ManagementService;

import javax.swing.*;
import java.awt.*;

/**
 * A modal dialog window for adding a new vehicle to the system's fleet.
 * <p>
 * This class provides a form interface for the user to input vehicle details
 * (plate, brand, type, model, year, color). It handles the input collection
 * and communicates with the {@link ManagementService} to validate and store
 * the new car.
 * </p>
 * Implements {@link StyleAddCancel} to maintain consistent button styling across the application.
 */
public class AddCarDialog extends JDialog implements StyleAddCancel {

    /**
     * Reference to the backend service for data handling.
     */
    private final ManagementService service;

    // UI Input Fields
    private JTextField plateField;
    private JTextField brandField;
    private JTextField typeField;
    private JTextField modelField;
    private JTextField yearField;
    private JTextField colorField;

    /**
     * Constructs the Add Car Dialog.
     * Initializes the user interface, sets up the layout grid, applies styling,
     * and defines the action listeners for the Save and Cancel buttons.
     *
     * @param parent  The parent JFrame (usually MainFrame) to center the dialog relative to.
     * @param service The ManagementService instance used to perform the add operation.
     */
    public AddCarDialog(JFrame parent, ManagementService service) {
        super(parent, "Προσθήκη Νέου Οχήματος", true); // true = modal
        this.service = service;

        // DIALOG
        setSize(600, 460);
        setLocationRelativeTo(parent);

        // PANEL
        JPanel addCarPanel = new JPanel(new GridLayout(0, 2, 7, 16));
        addCarPanel.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        // LABELS AND TEXTFIELDS
        addCarPanel.add(new JLabel("Πινακίδα")); //Plate
        plateField = new JTextField();
        addCarPanel.add(plateField);

        addCarPanel.add(new JLabel("Μάρκα"));  //Brand
        brandField = new JTextField();
        addCarPanel.add(brandField);

        addCarPanel.add(new JLabel("Τύπος"));  //Type
        typeField = new JTextField();
        addCarPanel.add(typeField);

        addCarPanel.add(new JLabel("Μοντέλο"));  //Model
        modelField = new JTextField();
        addCarPanel.add(modelField);

        addCarPanel.add(new JLabel("Χρονολογία"));  //Year
        yearField = new JTextField();
        addCarPanel.add(yearField);

        addCarPanel.add(new JLabel("Χρώμα"));  //Color
        colorField = new JTextField();
        addCarPanel.add(colorField);

        //STYLING
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

    /**
     * Collects input data from the text fields and attempts to save the new car.
     * <p>
     * It retrieves the raw text from the fields, trims whitespace, and calls
     * {@link ManagementService#addNewCar(String, String, String, String, String, String)}.
     * Based on the return message, it displays a success or error dialog to the user.
     * </p>
     */
    private void saveCar() {

        String plate = plateField.getText().trim();
        String brand = brandField.getText().trim();
        String model = modelField.getText().trim();
        String type = typeField.getText().trim();
        String year = yearField.getText().trim();
        String colour = colorField.getText().trim();

        String check = service.addNewCar(plate, brand, type, model, year, colour);

        if (check.equals("Επιτυχής καταχώρηση.")) {
            JOptionPane.showMessageDialog(this, check,
                    "Το όχημα προστέθηκε επιτυχώς!",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, check,
                    "Η καταχώρηση απέτυχε!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}