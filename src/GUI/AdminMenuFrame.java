package GUI;

import API.ManagementService;
import API.Employee;

import javax.swing.*;
import java.awt.*;

public class AdminMenuFrame extends JFrame {

    public AdminMenuFrame(ManagementService service,Employee employee) {
        setTitle("Admin Dashboard - User: " + employee.getUsername());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        //button for CAR LIST
        JButton carsButton = new JButton("Fleet Management(Cars)");
        carsButton.addActionListener(e -> {
            new TableFrame(service, "CARS").setVisible(true);
        });

        //button for CLIENT LIST
        JButton clientsButton = new JButton("Database(Clients)");
        clientsButton.addActionListener(e -> {
            new TableFrame(service, "CLIENTS").setVisible(true);
        });

        //button for RENTAL LIST
        JButton rentalsButton = new JButton("Rentals History");
        rentalsButton.addActionListener(e -> {
            new TableFrame(service, "RENTALS").setVisible(true);
        });

        //button for EXIT
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            new Login(service);
        });

        add(carsButton);
        add(clientsButton);
        add(rentalsButton);
        add(logoutButton);
    }
}
