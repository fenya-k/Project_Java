package API;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Manages all rental transactions in the system.
 * Implements the {@link Manager} interface.
 * Handles validation, adding, editing, removing, searching, file reading/writing.
 */
public class RentalManager implements Manager<Rental> {

    /**
     * The ArrayList of all rentals.
     */
    private final ArrayList<Rental> rentals;

    /**
     * Formatter for parsing dates from CSV (e.g., 01/01/2026).
     */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

    /**
     * Constructor for a new manager. Initializes the list.
     */
    public RentalManager() {
        rentals = new ArrayList<>();
    }

    /**
     * Validation method to check if all the necessary fields have been given before add/edit.
     * Checks for null/empty values.
     *
     * @param code      The rental code
     * @param rentCar   The car object
     * @param client    The client object
     * @param startDate Start date
     * @param endDate   End date
     * @param employee  The employee processing the rental
     * @return "Επιτυχής καταχώρηση." if valid, otherwise an error message
     */
    public String isValidRental(int code, Car rentCar, Client client, LocalDate startDate, LocalDate endDate, Employee employee) {
        StringBuilder fullString = new StringBuilder();

        if (code <= 0) fullString.append("Παρακαλώ καταχωρήστε τον κωδικό.\n");
        if (rentCar == null) fullString.append("Παρακαλώ καταχωρήστε το όχημα.\n");
        if (client == null) fullString.append("Παρακαλώ καταχωρήστε τον πελάτη.\n");
        if (employee == null) fullString.append("Παρακαλώ καταχωρήστε τον υπάλληλο.\n");
        if (startDate == null) fullString.append("Παρακαλώ καταχωρήστε ημερομηνία παραλαβής.\n");
        if (endDate == null) fullString.append("Παρακαλώ καταχωρήστε ημερομηνία επιστροφής.\n");

        if (fullString.isEmpty()) {
            return "Επιτυχής καταχώρηση.";
        } else {
            return fullString.toString();
        }
    }

    /**
     * Adds a new rental to the system.
     * Checks if a rental with the given code exists.
     * Checks if the car is available.
     * Updates the Car status to RENTED if successful.
     *
     * @param rental The rental to add
     * @return true if successful
     */
    @Override
    public boolean add(Rental rental) {
        for (Rental r : rentals) {
            if (r.getRentCode() == rental.getRentCode()) {
                System.out.println("Υπάρχει ήδη ενοικίαση με κωδικό " + rental.getRentCode());
                return false;
            }
        }
        if (!rental.getRentCar().isAvailable()) {
            System.out.println("Vehicle " + rental.getRentCar().getPlate() + " is not available.");
            return false;
        }
        rentals.add(rental);
        rental.getRentCar().setCarStatus(CarStatus.RENTED);
        System.out.println("Η ενοικίαση ολοκληρώθηκε.");
        return true;
    }

    /**
     * Edits an existing rental.
     * The code is used to find the rental and cannot be changed here.
     *
     * @param code      The code
     * @param rentCar   New car
     * @param client    New client
     * @param startDate New start date
     * @param endDate   New end date
     * @param employee  New employee
     * @return true if found and edited, false otherwise
     */
    public boolean edit(int code, Car rentCar, Client client, LocalDate startDate, LocalDate endDate, Employee employee) {
        Rental editedRental = findByCode(code);
        // Safety check
        if (editedRental != null) {
            editedRental.setRentCar(rentCar);
            editedRental.setClient(client);
            editedRental.setStartDate(startDate);
            editedRental.setEndDate(endDate);
            editedRental.setEmployee(employee);
            return true;
        }
        return false;
    }

    /**
     * Removes a rental from the system.
     * Updates the Car status back to AVAILABLE.
     *
     * @param rental The rental to remove
     * @return true if removed
     */
    @Override
    public boolean remove(Rental rental) {
        int rentalsSize = rentals.size();
        // We store the size to avoid recalculating it in the loop.
        // Since we return immediately after removal, standard iteration is safe.
        for (int i = 0; i < rentalsSize; i++) {
            if (rentals.get(i).getRentCode() == rental.getRentCode()) {
                rentals.get(i).getRentCar().setCarStatus(CarStatus.AVAILABLE);
                rentals.remove(i);
                System.out.println("Η ενοικίαση διαγράφηκε.");
                return true;
            }
        }
        System.out.println("Δεν βρέθηκε ενοικίαση με κωδικό " + rental.getRentCode());
        return false;
    }

    /**
     * Retrieves the list of rentals.
     *
     * @return The ArrayList of rentals
     */
    @Override
    public ArrayList<Rental> getList() {
        return rentals;
    }

    /**
     * Retrieves the number of rentals in the list.
     *
     * @return The size of the list
     */
    @Override
    public int getSize() {
        return rentals.size();
    }

    /**
     * Prints the details of all rentals in the list.
     */
    @Override
    public void print() {
        if (rentals.isEmpty()) {
            System.out.println("No rentals found");
        } else {
            for (Rental rental : rentals) {
                System.out.println(rental.toString());
            }
        }
    }

    /**
     * Searches for rentals matching the provided parameters.
     * If a parameter is null, is ignored.
     *
     * @param code     Rental code (0 to ignore)
     * @param plate    Car plate
     * @param AFM      Client AFM
     * @param username Employee username
     * @return List of matching rentals
     */
    public ArrayList<Rental> search(int code, String plate, String AFM, String username) {
        ArrayList<Rental> foundRentals = new ArrayList<>();
        /* Logic explanation:
         * Iterate through the rentals with for loop. For each rental, we check a series of filters.
         * 1. If a search parameter is NOT provided (null/empty), skip the check.
         * 2. If a search parameter IS provided, compare it with the rental's value.
         * - If they match: Proceed to the next filter check.
         * - If they do NOT match: Enter the 'if' body, execute 'continue', and skip to the next rental immediately.
         */
        for (Rental rental : rentals) {
            if (username != null && !username.isEmpty() && !rental.getEmployee().getUsername().equalsIgnoreCase(username)) {
                continue;
            }
            if (plate != null && !plate.isEmpty() && !rental.getRentCar().getPlate().equalsIgnoreCase(plate)) {
                continue;
            }
            if (AFM != null && !AFM.isEmpty() && !rental.getClient().getAFM().equalsIgnoreCase(AFM)) {
                continue;
            }
            if (code > 0 && rental.getRentCode() != code) {
                continue;
            }
            foundRentals.add(rental);
        }
        return foundRentals;
    }

    /**
     * Finds a rental using its code.
     *
     * @param code The rental code to search for
     * @return The Rental object or null
     */
    public Rental findByCode(int code) {
        for (Rental r : rentals) {
            if (r.getRentCode() == code) return r;
        }
        return null;
    }

    // --- READ - WRITE CSV ---

    /**
     * Reads rentals from CSV and populates the list.
     * Requires instances of other managers to link objects (Car, Client, Employee).
     *
     * @param CM       CarManager
     * @param CLM      ClientManager
     * @param EM       EmployeeManager
     * @param filename CSV file path
     */
    public void readCSV(CarManager CM, ClientManager CLM, EmployeeManager EM, String filename) {
        String line;
        String delimiter = ",";

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); // skip header

            while ((line = in.readLine()) != null) {
                String[] data = line.split(delimiter);
                if (data.length < 6) continue; // Safety check

                try {
                    int rentCode = Integer.parseInt(data[0].trim());
                    String plate = data[1].trim();
                    String afm = data[2].trim();
                    LocalDate start = LocalDate.parse(data[3].trim(), formatter);
                    LocalDate end = LocalDate.parse(data[4].trim(), formatter);
                    String empUser = data[5].trim();

                    // Find the real objects
                    Car car = CM.findByPlate(plate);
                    Client client = CLM.findByAFM(afm);
                    Employee emp = EM.findByUsername(empUser);

                    // Only create rental if all parts exist
                    if (car != null && client != null && emp != null) {
                        Rental rental = new Rental(rentCode, car, client, start, end, emp);

                        // Add to main list
                        rentals.add(rental);

                        // Add to the specific car's and client's history
                        car.addRental(rental);
                        client.addRental(rental);

                        // Only mark as RENTED if the rental is still active (today is before or equal to end date)
                        if (end.isAfter(LocalDate.now()) || end.isEqual(LocalDate.now())) {
                            car.setCarStatus(CarStatus.RENTED);
                        }
                    } else {
                        System.out.println("Rental " + rentCode + " skipped: Objects not found.");
                    }

                } catch (NumberFormatException | DateTimeParseException e) {
                    System.err.println("Error parsing rental line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading rentals file.");
        }
    }

    /**
     * Writes the list of rentals to a CSV file.
     * Overwrites the file.
     *
     * @param filename CSV file path
     */
    public void writeCSV(String filename) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            String header = "rentCode,plate,AFM,startDate,endDate,employeeUser";
            out.write(header);
            out.newLine();

            for (Rental rental : rentals) {
                String line = rental.getRentCode() + "," +
                        rental.getRentCar().getPlate() + "," +
                        rental.getClient().getAFM() + "," +
                        rental.getStartDate().format(formatter) + "," +
                        rental.getEndDate().format(formatter) + "," +
                        rental.getEmployee().getUsername();

                out.write(line);
                out.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing rentals file.");
        }
    }
}