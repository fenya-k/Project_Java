package API;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Provides default methods to read and write rental data to a CSV file
 * It is used by Car, Client and RentalManager
 */
public interface ReadWriteCSV {

    /**
     * Reads the rental history from a CSV file.
     * Uses "d/M/yyyy" date format.
     *
     * @param carManager      Manager to find car by plate
     * @param clientManager   Manager to find client by afm
     * @param employeeManager Manager to find employee by username
     * @param filename        Path to the CSV file.
     * @param list            The list to populate with data from the CSV file
     */
    default void readCSV(CarManager carManager, ClientManager clientManager, EmployeeManager employeeManager, String filename, ArrayList<Rental> list) {
        String line;
        String delimiter = ",";
        //formatter for the date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            //skips the first line as it is a header
            in.readLine();

            //reads a line and stores it in the String line
            while ((line = in.readLine()) != null) {
                //uses delimiter to split the line into the String array data
                String[] data = line.split(delimiter);

                //validates that the line has sufficient data
                if (data.length < 6) continue;

                //parses data from the array and stores them in corresponding variables
                int rentCode = Integer.parseInt(data[0].trim());
                String plate = data[1].trim();
                String clientAFM = data[2].trim();
                String start = data[3].trim();
                String end = data[4].trim();
                String employeeUsername = data[5].trim();

                //parses the dates with formatter and stores them in corresponding variables
                LocalDate startDate = LocalDate.parse(start, formatter);
                LocalDate endDate = LocalDate.parse(end, formatter);

                //calls the managers to find car, client and employee
                Car rentCar = carManager.findByPlate(plate);
                Client client = clientManager.findByAFM(clientAFM);
                Employee employee = employeeManager.findByUsername(employeeUsername);

                //if nothing is null
                if (client != null && rentCar != null && employee != null) {
                    //creates the new rental with the constructor
                    Rental rental = new Rental(rentCode, rentCar, client, startDate, endDate, employee);
                    //adds the rental to the provided list
                    list.add(rental);
                    //checks the date and decided if the car is available or rented
                    if (endDate.isAfter(LocalDate.now()) || endDate.isEqual(LocalDate.now())) {
                        rentCar.setCarStatus(CarStatus.RENTED);
                    }
                } else {
                    //message if the rental was not found due to missing objects
                    System.out.println("Rental " + rentCode + " skipped: Car or Client not found.");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.err.println("Error: File not read!");
        }
    }

    /**
     * Writes the rental history to a CSV file.
     * It overwrites the file content (does not append).
     *
     * @param filename Path to the CSV file.
     * @param list     The list with data to be saved to CSV file
     */
    default void writeCSV(String filename, ArrayList<Rental> list) {
        //formatter for the date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            //writes the header
            String header = "κωδικός ενοικίασης,πινακίδα,ΑΦΜ πελάτη,ημέρα έναρξης,ημέρα λήξης,όνομα εργαζόμενου";
            out.write(header);
            out.newLine();

            //iterates the list to write every rental
            for (Rental rental : list) {

                //formats the dates with formatter and stores them in corresponding variables
                String sDate = rental.getStartDate().format(formatter);
                String eDate = rental.getEndDate().format(formatter);

                //creates the final String to be written
                String line = rental.getRentCode() + "," +
                        rental.getRentCar().getPlate() + "," +
                        rental.getClient().getAFM() + "," +
                        sDate + "," +
                        eDate + "," +
                        rental.getEmployee().getUsername();
                //writes the String and moves to the next line
                out.write(line);
                out.newLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.err.println("Error: File not read!");
        }
    }
}