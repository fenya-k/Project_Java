package API;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public interface ReadWriteCSV {

    default void readCSV(CarManager carManager, ClientManager clientManager, EmployeeManager employeeManager, String filename, ArrayList<Rental> list) {
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
                Employee employee = employeeManager.findByUsername(employeeUsername);

                if (client != null && rentCar != null && employee != null) {
                    Rental rental = new Rental(rentCode, rentCar, client, startDate, endDate, employee);
                    list.add(rental);
                    if (endDate.isAfter(LocalDate.now())) {
                        rentCar.setCarStatus(CarStatus.RENTED);
                    }
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


    default void writeCSV(String filename, ArrayList<Rental> list) {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            String header = "κωδικός ενοικίασης,πινακίδα,ΑΦΜ πελάτη,ημέρα έναρξης,ημέρα λήξης,όνομα εργαζόμενου";
            out.write(header);
            out.newLine();

            for (Rental rental : list) {
                String line = rental.getRentCode() + "," +
                        rental.getRentCar().getPlate() + "," +
                        rental.getClient().getAFM() + "," +
                        rental.getStartDate() + "," +
                        rental.getEndDate() + "," +
                        rental.getEmployee().getUsername();

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
