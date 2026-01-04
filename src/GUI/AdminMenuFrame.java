package GUI;

import API.ManagementService;
import API.Employee;

import javax.swing.*;
import java.awt.*;

/**
 * An administrative dashboard interface for the application.
 * <p>
 * This class provides a simplified, button-based menu structure using a {@link GridLayout}.
 * It grants the user access to specific data views (Fleet, Clients, Rentals)
 * by opening instances of {@link TableFrame}. It also handles the logout process.
 * </p>
 */
public class AdminMenuFrame extends JFrame {

    /**
     * Constructs the Admin Menu Dashboard.
     * Initializes the window settings, layout, and navigation buttons.
     *
     * @param service  The ManagementService instance used for data operations.
     * @param employee The Employee object representing the currently logged-in user.
     */
    public AdminMenuFrame(ManagementService service, Employee employee) {
        //FRAME SETTINGS
        setTitle("Admin Dashboard - User: " + employee.getUsername());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        //button for CAR LIST
        JButton carsButton = new JButton("Fleet Management(Cars)");
        carsButton.addActionListener(e -> {
            // Opens the table view specifically for Cars
            new TableFrame(service, "CARS").setVisible(true);
        });

        //button for CLIENT LIST
        JButton clientsButton = new JButton("Database(Clients)");
        clientsButton.addActionListener(e -> {
            // Opens the table view specifically for Clients
            new TableFrame(service, "CLIENTS").setVisible(true);
        });

        //button for RENTAL LIST
        JButton rentalsButton = new JButton("Rentals History");
        rentalsButton.addActionListener(e -> {
            // Opens the table view specifically for Rentals
            new TableFrame(service, "RENTALS").setVisible(true);
        });

        //button for LOGOUT
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose(); //Closes the current dashboard
            new Login(service);
        });

        // Add components to the layout
        add(carsButton);
        add(clientsButton);
        add(rentalsButton);
        add(logoutButton);
    }
}
