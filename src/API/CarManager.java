package API;

import java.io.*;
import java.util.ArrayList;

/**
 * This is the manager of all vehicles in the rental system.
 * Implementing the interface {@link Manager}.
 * Handles validation, adding, editing, removing, searching and file reading/writing.
 */
public class CarManager implements Manager<Car> {
    /**
     * The ArrayList of all the vehicles in the system.
     */
    private final ArrayList<Car> cars;

    /**
     * Constructor for a new manager. Initializes the ArrayList.
     */
    public CarManager() {
        cars = new ArrayList<>();
    }

    /**
     * Validation method to check if all the necessary fields have been given before add/edit.
     * Checks for null/empty values and the year of manufacturing.
     *
     * @param plate Vehicle plate
     * @param brand Vehicle brand
     * @param type  Vehicle type
     * @param model Vehicle model
     * @param year  Manufacturing year (as String to check format)
     * @param color Vehicle color
     * @return "Τα στοιχεία είναι πλήρη." if valid, otherwise an error message
     */
    public String isValidCar(String plate, String brand, String type, String model, String year, String color) {
        StringBuilder fullString = new StringBuilder();

        if (plate == null || plate.isEmpty()) fullString.append("Παρακαλώ καταχωρήστε τον αριθμό πινακίδας.\n");
        if (brand == null || brand.isEmpty()) fullString.append("Παρακαλώ καταχωρήστε τη μάρκα.\n");
        if (type == null || type.isEmpty()) fullString.append("Παρακαλώ καταχωρήστε τον τύπο.\n");
        if (model == null || model.isEmpty()) fullString.append("Παρακαλώ καταχωρήστε το μοντέλο.\n");
        if (color == null || color.isEmpty()) fullString.append("Παρακαλώ καταχωρήστε το χρώμα.\n");

        if (year == null || year.isEmpty()) {
            fullString.append("Παρακαλώ καταχωρήστε το έτος κυκλοφορίας.\n");
        } else {
            try {
                int yearInt = Integer.parseInt(year);
                if (yearInt <= 0) {
                    fullString.append("Το έτος κυκλοφορίας πρέπει να είναι θετικός αριθμός.\n");
                } else if (yearInt < 1886) {
                    fullString.append("Το πρώτο αυτοκίνητο κατασκευάστηκε το 1886.\n Στο έτος που δόθηκε δεν υπήρχαν αυτοκίνητα.\n");
                } else if (yearInt > 2025) {
                    fullString.append("Το αυτοκίνητο δεν μπορεί να έχει κατασκευαστεί στο μέλλον.\n");
                }
            } catch (NumberFormatException e) {
                fullString.append("Το έτος κυκλοφορίας πρέπει να είναι αριθμός.\n");
            }
        }

        if (fullString.isEmpty()) {
            return "Τα στοιχεία είναι πλήρη.";
        } else {
            return fullString.toString();
        }
    }

    /**
     * Adds a new vehicle to the system after checking for duplicate license plates.
     *
     * @param car The vehicle to be added
     * @return true if added successfully, false if a car with the same plate exists
     */
    @Override
    public boolean add(Car car) {
        for (Car c : cars) {
            if (c.getPlate().equalsIgnoreCase(car.getPlate())) {
                System.out.println("There is already a car with plate: " + car.getPlate());
                return false;
            }
        }
        cars.add(car);
        System.out.println("The car has been added.");
        return true;
    }

    /**
     * Edits the characteristics of an existing vehicle.
     * The plate is used to find the car and cannot be changed.
     *
     * @param plate The plate
     * @param brand New brand
     * @param type  New type
     * @param model New model
     * @param year  New year
     * @param color New color
     * @return true if found and edited, false otherwise
     */
    public boolean edit(String plate, String brand, String type, String model, String year, String color) {
        Car editedCar = findByPlate(plate);
        // Safety Check
        if (editedCar != null) {
            editedCar.setBrand(brand);
            editedCar.setType(type);
            editedCar.setModel(model);
            editedCar.setYear(year);
            editedCar.setColor(color);
            return true;
        }
        return false;
    }

    /**
     * Removes a vehicle from the system.
     *
     * @param car The vehicle to remove
     * @return true if removed, false if not found
     */
    @Override
    public boolean remove(Car car) {
        int carsSize = cars.size();
        // We store the size to avoid recalculating it in the loop,
        // although since we return immediately after removal, standard iteration is also safe no (IndexOutOfBoundsException)
        for (int i = 0; i < carsSize; i++) {
            if (cars.get(i).getPlate().equalsIgnoreCase(car.getPlate())) {
                cars.remove(i);
                System.out.println("The car has been removed.");
                return true;
            }
        }
        System.out.println("Cannot find a car with plate: " + car.getPlate());
        return false;
    }

    /**
     * Retrieves the list of all the vehicles.
     *
     * @return An ArrayList of the vehicles
     */
    @Override
    public ArrayList<Car> getList() {
        return cars;
    }

    /**
     * Retrieves the number of vehicles in the list.
     *
     * @return The size of the list
     */
    @Override
    public int getSize() {
        return cars.size();
    }

    /**
     * Prints the details of all vehicles in the list.
     * Used for debugging
     */
    @Override
    public void print() {
        if (cars.isEmpty()) {
            System.out.println("No cars found");
        } else {
            for (Car car : cars) {
                System.out.println(car.toString());
            }
        }
    }


    /**
     * Searches for vehicles matching the provided parameters.
     * If a parameter is null, is ignored.
     *
     * @param plate     Filter by plate
     * @param brand     Filter by brand
     * @param type      Filter by type
     * @param model     Filter by model
     * @param color     Filter by color
     * @param available Filter by availability status
     * @return A list of cars matching all non-null criteria
     */
    public ArrayList<Car> search(String plate, String brand, String type, String model, String color, Boolean available) {
        ArrayList<Car> foundCars = new ArrayList<>();
        /* Logic explanation:
         * Iterate through the cars with for loop. For each car, we check a series of filters (if statements).
         * 1. If a search parameter is NOT provided (null/empty), skip the check and move to the next 'if'.
         * 2. If a search parameter IS provided, compare it with the car's value.
         * - If they match: Proceed to the next filter check.
         * - If they do NOT match: Enter the 'if' body, execute 'continue', and skip to the next car immediately.
         */
        for (Car car : cars) { //null proofing (protection from NullPointerExceptions)
            if (plate != null && !plate.isEmpty() && !car.getPlate().equalsIgnoreCase(plate)) {
                continue;
            } //the plate comes first so if its different no more checks are needed
            if (brand != null && !brand.isEmpty() && !car.getBrand().equalsIgnoreCase(brand)) {
                continue;
            }
            if (type != null && !type.isEmpty() && !car.getType().equalsIgnoreCase(type)) {
                continue;
            }
            if (model != null && !model.isEmpty() && !car.getModel().equalsIgnoreCase(model)) {
                continue;
            }
            if (color != null && !color.isEmpty() && !car.getColor().equalsIgnoreCase(color)) {
                continue;
            }
            if (available != null && car.isAvailable() != available) {
                continue;
            }
            foundCars.add(car);
        }
        return foundCars;
    }

    /**
     * Finds a vehicle using its plate.
     *
     * @param plate The plate to search for
     * @return The Car object if found, null otherwise
     */
    public Car findByPlate(String plate) {
        for (Car c : cars) {
            if (c.getPlate().equalsIgnoreCase(plate)) return c;
        }
        return null;
    }

    // --- READ - WRITE ---

    /**
     * Reads vehicles from a CSV file and populates the list.
     *
     * @param filename The path to the CSV file
     */
    public void readCSV(String filename) {
        String line;
        String delimiter = ",";

        cars.clear();

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); //skips the first line because it's a header

            while ((line = in.readLine()) != null) {
                Car car = getCar(line, delimiter);
                if (car != null) {
                    cars.add(car);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.err.println("Error: File not read!");
        }
    }

    /**
     * Helper method to parse a vehicle from a CSV.
     *
     * @param line      The CSV line string
     * @param delimiter The delimiter used in the CSV
     * @return A new Car object, or null if the data is incomplete
     */
    private static Car getCar(String line, String delimiter) {
        String[] data = line.split(delimiter);

        int id = Integer.parseInt(data[0].trim());
        String plate = data[1].trim();
        String brand = data[2].trim();
        String type = data[3].trim();
        String model = data[4].trim();
        String year = data[5].trim();
        String color = data[6].trim();
        String statusGreek = data[7].trim();

        CarStatus status;
        if (statusGreek.equalsIgnoreCase("Διαθέσιμο")) {
            status = CarStatus.AVAILABLE;
        } else {
            status = CarStatus.RENTED;
        }

        return new Car(id, plate, brand, type, model, year, color, status);
    }

    /**
     * Writes the list of vehicles to a CSV file.
     * Overwrites the file.
     *
     * @param filename The path to the CSV file
     */
    public void writeCSV(String filename) {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            String header = "id,πινακίδα,μάρκα,τύπος,μοντέλο,έτος,χρώμα,κατάσταση";
            out.write(header);
            out.newLine();

            for (Car car : cars) {
                String availability = car.isAvailable() ? "Διαθέσιμο" : "Ενοικιασμένο";

                String line = car.getId() + "," +
                        car.getPlate() + "," +
                        car.getBrand() + "," +
                        car.getType() + "," +
                        car.getModel() + "," +
                        car.getYear() + "," +
                        car.getColor() + "," +
                        availability;

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