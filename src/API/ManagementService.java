package API;

import java.time.LocalDate;

/**
 * The central service that coordinates all Managers.
 * It acts as the bridge between the GUI and API.
 */
public class ManagementService {

    //FILE PATHS
    private final String filenameCars = "DataBase/ManagerFiles/vehicles.csv";
    private final String filenameClients = "DataBase/ManagerFiles/clients.csv";
    private final String filenameEmployees = "DataBase/ManagerFiles/users.csv";
    private final String filenameRentals = "DataBase/ManagerFiles/rentals.csv";

    //ALL MANAGERS
    private final CarManager carManager;
    private final ClientManager clientManager;
    private final EmployeeManager employeeManager;
    private final RentalManager rentalManager;

    /**
     * Constructor, initializes all managers.
     */
    public ManagementService() {
        this.carManager = new CarManager();
        this.clientManager = new ClientManager();
        this.employeeManager = new EmployeeManager();
        this.rentalManager = new RentalManager();
    }

    /**
     * Reads all CSV files.
     * The order is important: Entities first (Cars, Clients, Employees), then Rentals (which link them).
     */
    public void readAllCSV() {
        carManager.readCSV(filenameCars);
        clientManager.readCSV(filenameClients);
        employeeManager.readCSV(filenameEmployees);
        rentalManager.readCSV(carManager, clientManager, employeeManager, filenameRentals);
    }

    /**
     * Saves all data to CSV files.
     */
    public void writeAllCSV() {
        carManager.writeCSV(filenameCars);
        clientManager.writeCSV(filenameClients);
        employeeManager.writeCSV(filenameEmployees);
        rentalManager.writeCSV(filenameRentals);
    }

    // --- CAR MANAGEMENT ---

    //ADDS A NEW CAR
    public String addNewCar(String plate, String brand, String type, String model, String year, String color) {
        //checks according to the parameters if the car is valid
        String check = carManager.isValidCar(plate, brand, type, model, year, color);
        //prints the message from the manager
        System.out.println(check);
        if (!check.equals("Τα στοιχεία είναι πλήρη.")) {
            return check;
        }
        //if car is valid, a new car is created
        Car newCar = new Car(plate, brand, type, model, year, color);
        //adds the car and gets confirmation from the manager
        boolean isAdded = carManager.add(newCar);
        if (isAdded) {
            return "Επιτυχής καταχώρηση.";
        } else {
            return "Υπάρχει ήδη αυτοκίνητο με αυτή την πινακίδα.";
        }
    }

    //EDITS AN EXISTING CAR
    public String editExistingCar(String plate, String brand, String type, String model, String year, String color) {
        String check = carManager.isValidCar(plate, brand, type, model, year, color);
        System.out.println(check);
        if (!check.equals("Τα στοιχεία είναι πλήρη.")) {
            return check;
        }
        boolean success = carManager.edit(plate, brand, type, model, year, color);
        if (success) {
            return "Επιτυχής επεξεργασία.";
        } else {
            return "Δεν βρέθηκε αυτοκίνητο με αυτή την πινακίδα.";
        }
    }

    // --- CLIENT MANAGEMENT ---

    //ADDS A NEW CLIENT
    public String addNewClient(String name, String surname, String AFM, String phone, String email) {
        //checks according to the parameters if the client is valid
        String check = clientManager.isValidClient(name, surname, AFM, phone, email);
        System.out.println(check);
        if (!check.equals("Επιτυχής καταχώρηση.")) {
            return check;
        }
        //if client is valid, a new client is created
        Client newClient = new Client(name, surname, AFM, phone, email);
        boolean isAdded = clientManager.add(newClient);

        if (isAdded) {
            return "Επιτυχής καταχώρηση.";
        } else {
            return "Υπάρχει ήδη πελάτης με αυτό το ΑΦΜ.";
        }
    }

    //EDITS AN EXISTING CLIENT
    public String editExistingClient(String name, String surname, String AFM, String phone, String email) {
        String check = clientManager.isValidClient(name, surname, AFM, phone, email);
        System.out.println(check);
        if (!check.equals("Επιτυχής καταχώρηση.")) {
            return check;
        }
        boolean success = clientManager.edit(name, surname, AFM, phone, email);
        if (success) {
            return "Επιτυχής επεξεργασία.";
        } else {
            return "Δεν βρέθηκε πελάτης με αυτό το ΑΦΜ.";
        }
    }

    // --- EMPLOYEE MANAGEMENT ---

    //EDITS AN EXISTING EMPLOYEE
    public String editExistingEmployee(String username, String name, String surname, String email) {
        String check = employeeManager.isValidEmployee(username, name, surname, email);
        System.out.println(check);
        if (!check.equals("Επιτυχής καταχώρηση.")) {
            return check;
        }
        boolean success = employeeManager.edit(username, name, surname, email);
        if (success) {
            return "Επιτυχής επεξεργασία.";
        } else {
            return "Δεν βρέθηκε εργαζόμενος με αυτό το username.";
        }
    }

    // --- RENTAL MANAGEMENT ---

    //EDITS AN EXISTING RENTAL
    public String editExistingRental(int code, Car rentCar, Client client, LocalDate startDate, LocalDate endDate, Employee employee) {
        String check = rentalManager.isValidRental(code, rentCar, client, startDate, endDate, employee);
        System.out.println(check);
        if (!check.equals("Επιτυχής καταχώρηση.")) {
            return check;
        }
        boolean success = rentalManager.edit(code, rentCar, client, startDate, endDate, employee);
        if (success) {
            return "Επιτυχής επεξεργασία.";
        } else {
            return "Δεν βρέθηκε ενοικίαση με αυτόν τον κωδικό.";
        }
    }

    /**
     * Performs the car rental process.
     * 1. Adds rental to the global list.
     * 2. Adds rental to the car's history.
     * 3. Adds rental to the client's history.
     * @param rental The rental object to be processed.
     */
    //MAKES A RENTAL AND UPDATES HISTORY
    public void rentCar(Rental rental) {
        boolean isAdded = rentalManager.add(rental);

        if (isAdded) {
            Car rentedCar = carManager.findByPlate(rental.getRentCar().getPlate());
            Client client = clientManager.findByAFM(rental.getClient().getAFM());

            if (rentedCar != null) rentedCar.addRental(rental); //update history
            if (client != null) client.addRental(rental); //update history
        } else {
            System.out.println("Η ενοικίαση απέτυχε (Το όχημα δεν είναι διαθέσιμο ή υπάρχει ήδη κωδικός).");
        }
    }

    //MAKES A RETURN NO HISTORY UPDATE NEEDED
    public void returnCar(Rental rental) {
        // rentalManager.remove() automatically sets the car status to AVAILABLE.
        rentalManager.remove(rental);
    }

    // --- GETTERS FOR TESTS ---

    public CarManager getCarManager() {
        return carManager;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public EmployeeManager getEmployeeManager() {
        return employeeManager;
    }

    public RentalManager getRentalManager() {
        return rentalManager;
    }
}