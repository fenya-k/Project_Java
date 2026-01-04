package GUI;

import API.Car;
import API.ManagementService;

import javax.swing.*;
import java.awt.*;

/**
 * A modal dialog window for editing the details of an existing vehicle.
 * <p>
 * This class pre-fills the form fields with the current data of the selected {@link Car}.
 * It allows the user to modify attributes like Brand, Type, Model, Year, and Color.
 * <b>Note:</b> The License Plate field is set to read-only mode to preserve the
 * vehicle's unique identity.
 * </p>
 * Implements {@link StyleAddCancel} for consistent UI styling.
 */
public class EditCarDialog extends JDialog implements StyleAddCancel {

    /**
     * Reference to the backend service for data handling.
     */
    private final ManagementService service;

    /**
     * The specific car object being edited.
     */
    private Car car;

    // UI Input Fields
    private JTextField plateField;
    private JTextField brandField;
    private JTextField typeField;
    private JTextField modelField;
    private JTextField yearField;
    private JTextField colorField;

    /**
     * Constructs the Edit Car Dialog.
     * Initializes the UI, populates fields with existing car data, locks the plate field,
     * and sets up action listeners.
     *
     * @param parent  The parent window (TableCar) from which this dialog was opened.
     * @param service The ManagementService instance for data operations.
     * @param car     The Car object containing the data to be edited.
     */
    public EditCarDialog(TableCar parent, ManagementService service, Car car) {
        super(parent, "Επεξεργασία Οχήματος", true);
        this.service = service;
        this.car = car;

        // DIALOG
        setSize(600, 460);
        setLocationRelativeTo(parent);

        // PANEL
        JPanel addCarPanel = new JPanel(new GridLayout(0, 2, 7, 16));
        addCarPanel.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        //read-only
        addCarPanel.add(new JLabel("Πινακίδα"));  //Plate
        plateField = new JTextField();
        plateField.setText(car.getPlate());
        plateField.setEditable(false);
        plateField.setBackground(new Color(204, 204, 204));
        addCarPanel.add(plateField);

        addCarPanel.add(new JLabel("Μάρκα"));  //Brand
        brandField = new JTextField();
        brandField.setText(car.getBrand());
        addCarPanel.add(brandField);

        addCarPanel.add(new JLabel("Τύπος"));  //Type
        typeField = new JTextField();
        typeField.setText(car.getType());
        addCarPanel.add(typeField);

        addCarPanel.add(new JLabel("Μοντέλο"));  //Model
        modelField = new JTextField();
        modelField.setText(car.getModel());
        addCarPanel.add(modelField);

        addCarPanel.add(new JLabel("Χρονολογία"));  //Year
        yearField = new JTextField();
        yearField.setText(String.valueOf(car.getYear()));
        addCarPanel.add(yearField);

        addCarPanel.add(new JLabel("Χρώμα"));  //Color
        colorField = new JTextField();
        colorField.setText(car.getColor());
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
     * Collects the modified data and saves the changes.
     * <p>
     * Retrieves text from the editable fields and calls
     * {@link ManagementService#editExistingCar(String, String, String, String, String, String)}.
     * Displays a success message if the update is successful, or an error message otherwise.
     * </p>
     */
    private void saveCar() {

        String plate = plateField.getText().trim();
        String brand = brandField.getText().trim();
        String model = modelField.getText().trim();
        String type = typeField.getText().trim();
        String year = yearField.getText().trim();
        String colour = colorField.getText().trim();

        // Pass updated data to service
        String check = service.editExistingCar(plate, brand, type, model, year, colour);

        if (check.equals("Επιτυχής καταχώρηση.")) {
            JOptionPane.showMessageDialog(this, "Επιτυχής ενημέρωση.",
                    "Το όχημα ενημερώθηκε επιτυχώς!",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, check,
                    "Η ενημέρωση απέτυχε!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
