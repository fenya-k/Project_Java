package GUI;

import API.Car;
import API.Rental;
import API.Client;
import API.ManagementService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TableFrame extends JFrame {
    public TableFrame(ManagementService service, String type) {
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel();

        //table for CARS
        if (type.equals("CARS")) {
            setTitle("Vehicles");
            String[] columns = {"ID", "Plate", "Brand", "Model", "Year", "Status"};
            model.setColumnIdentifiers(columns);

            ArrayList<Car> list = service.getCarManager().getList();
            for (Car car : list) {
                model.addRow(new Object[]{
                        car.getId(), car.getPlate(), car.getBrand(), car.getModel(), car.getYear(), car.getCarStatus()
                });
            }
        }
        //table for CLIENTS
        else if (type.equals("CLIENTS")) {
            setTitle("Client Database");
            String[] columns = {"Name", "Surname", "AFM", "Phone", "Email"};
            model.setColumnIdentifiers(columns);

            ArrayList<Client> list = service.getClientManager().getList();
            for (Client client : list) {
                model.addRow(new Object[]{
                        client.getName(), client.getSurname(), client.getAFM(), client.getPhone(), client.getEmail()
                });
            }
        }
        //table for RENTALS
        else if (type.equals("RENTALS")) {
            setTitle("Rentals History");
            String[] columns = {"Code", "Car Plate", "Client AFM", "Start Date", "End Date", "Employee"};
            model.setColumnIdentifiers(columns);

            ArrayList<Rental> list = service.getRentalManager().getList();
            for (Rental rental : list) {
                model.addRow(new Object[]{
                        rental.getRentCode(), rental.getRentCar().getPlate(), rental.getClient().getAFM(), rental.getStartDate(), rental.getEndDate(), rental.getEmployee().getUsername()
                });
            }
        }
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

    }
}
