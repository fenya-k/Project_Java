package API;

public class ManagementService {

    //FILE NAMES
    private final String filenameCars = "DataBase/ManagerFiles/vehicles.csv";
    private final String filenameClients = "DataBase/ManagerFiles/clients.csv";
    private final String filenameEmployees = "DataBase/ManagerFiles/users.csv";
    private final String filenameRentals = "DataBase/ManagerFiles/rentals.csv";
    private final String filenameCarRentals = "DataBase/HistoryCars/carRentals.csv";
    private final String filenameClientRentals = "DataBase/HistoryClients/clientRentals.csv";

    //ALL MANAGERS
    private final CarManager carManager;
    private final ClientManager clientManager;
    private final EmployeeManager employeeManager;
    private final RentalManager rentalManager;

    //CONSTRUCTOR
    public ManagementService() {
        this.carManager = new CarManager();
        this.clientManager = new ClientManager();
        this.employeeManager = new EmployeeManager();
        this.rentalManager = new RentalManager();
    }

    //READS ALL CSV OF ENTITIES AND HISTORY
    public void readAllCSV() {
        carManager.readCSV(filenameCars);
        clientManager.readCSV(filenameClients);
        employeeManager.readCSV(filenameEmployees);
        rentalManager.readCSV(carManager, clientManager, employeeManager, filenameRentals, rentalManager.getList());
        for (Car car : carManager.getList()) {
            car.readCSV(carManager, clientManager, employeeManager, filenameCarRentals, car.returnList());
        }
        for (Client client : clientManager.getList()) {
            client.readCSV(carManager, clientManager, employeeManager, filenameClientRentals, client.returnList());
        }
    }

    /**
     * Performs the car rental process.
     * 1. Adds rental to the global list.
     * 2. Updates the car status to RENTED.
     * 3. Adds rental to the car's history.
     * 4. Adds rental to the client's history.
     * * @param rental The rental object to be processed.
     */
    //MAKES A RENTAL AND UPDATES HISTORY
    public void rentCar(Rental rental) {
        rentalManager.add(rental);
        Car rentedCar = carManager.findByPlate(rental.getRentCar().getPlate());
        rentedCar.setCarStatus(CarStatus.RENTED); // update status
        rentedCar.addRental(rental); //update history
        Client client = clientManager.findByAFM(rental.getClient().getAFM());
        client.addRental(rental); //update history
    }

    //MAKES A RETURN NO HISTORY UPDATE NEEDED
    public void returnCar(Rental rental) {
        rentalManager.remove(rental);
        Car rentedCar = carManager.findByPlate(rental.getRentCar().getPlate());
        rentedCar.setCarStatus(CarStatus.AVAILABLE); // update status
    }

    //WRITES ALL CSV OF ENTITIES AND HISTORY
    public void writeAllCSV() {
        carManager.writeCSV(filenameCars);
        clientManager.writeCSV(filenameClients);
        employeeManager.writeCSV(filenameEmployees);
        rentalManager.writeCSV(filenameRentals, rentalManager.getList());
        for (Car car : carManager.getList()) {
            car.writeCSV(filenameCarRentals, car.returnList());
        }
        for (Client client : clientManager.getList()) {
            client.writeCSV(filenameClientRentals, client.returnList());
        }
    }

    // GETTERS FOR TESTS
    public CarManager getCarManager() { return carManager; }
    public ClientManager getClientManager() { return clientManager; }
    public EmployeeManager getEmployeeManager() { return employeeManager; }
    public RentalManager getRentalManager() { return rentalManager; }
}
