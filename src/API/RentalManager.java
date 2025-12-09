package API;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalManager implements Manager<Rental> {

    private final ArrayList<Rental> rentals;

    private final CarManager carManager;
    private final ClientManager clientManager;
    //private final EmployeeManager employeeManager;

    public RentalManager(CarManager carManager, ClientManager clientManager/*, EmployeeManager employeeManager */ ) {
        rentals = new ArrayList<>();
        this.carManager = carManager;
        this.clientManager = clientManager;
        //this.employeeManager = employeeManager;
    }

    @Override
    public boolean add(Rental rental) {
        if (!rentals.contains(rental)) {
            rentals.add(rental);
            rental.getRentCar().setCarStatus(CarStatus.RENTED);
            return true;
        }
        System.out.println("Υπάρχει ήδη ενοικίαση με κωδικό " + rental.getRentCode());
        return false;
    }

    @Override
    public boolean remove(Rental rental) {
        if (rentals.contains(rental)) {
            rentals.remove(rental);
            rental.getRentCar().setCarStatus(CarStatus.AVAILABLE);
            return true;
        }
        System.out.println("Δεν υπάρχει ενοικίαση με κωδικό " + rental.getRentCode());
        return false;
    }

    @Override
    public ArrayList<Rental> getAll() {
        return rentals;
    }

    @Override
    public int getSize() {
        return rentals.size();
    }

    @Override
    public void print() {
        for (Rental rental : rentals) {
            System.out.println(rental.toString());
        }
    }

    @Override
    public void readCSV() {
        String filename = "rentals.csv";
        String line;
        String delimiter = ",";

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); //skips the first line because it's a header

            while ((line = in.readLine()) != null) {
                String[] data = line.split(delimiter);

                int rentCode = Integer.parseInt(data[0].trim());
                String plate = data[1].trim();
                String clientAFM = data[2].trim();
                String start = data[3].trim();
                String end = data[4].trim();
                String employeeUsername = data[5].trim();

                LocalDate startDate = LocalDate.parse(start);
                LocalDate endDate = LocalDate.parse(end);

                Client client = clientManager.findByAFM(clientAFM);
                Car rentCar = carManager.findByPlate(plate);

                if (client != null && rentCar != null) {
                    Employee employee = new Employee(employeeUsername, "", "", "", "");
                    //employeeManager.findByUsername(username);
                    Rental rental = new Rental(rentCode, rentCar, client, startDate, endDate, employee);
                    rentals.add(rental);
                } else {
                    System.out.println("Η ενοικίαση " + rentCode + " δεν έγινε γιατί δεν βρέθηκε το όχημα ή ο πελάτης");
                }
            }
        } catch (
                FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (
                IOException e) {
            System.out.println("Error: File not read!");
        }
    }

    @Override
    public void writeCSV() {
        String filename = "rentals.csv";

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            String header = "κωδικός ενοικίασης,πινακίδα,ΑΦΜ πελάτη,ημέρα έναρξης,ημέρα λήξης,όνομα εργαζόμενου";
            out.write(header);
            out.newLine();

            for (Rental rental : rentals) {
                String line = rental.getRentCode() + "," +
                        rental.getRentCar().getPlate() + "," +
                        rental.getClient().getAFM() + "," +
                        rental.getStartDate() + "," +
                        rental.getEndDate() + "," +
                        rental.getEmployee().getName();

                out.write(line);
                out.newLine();
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.out.println("Error: File not read!");
        }
    }
}
