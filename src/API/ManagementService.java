package API;

public class ManagementService {

    private final CarManager carManager;
    private final ClientManager clientManager;
    private final EmployeeManager employeeManager;
    private final RentalManager rentalManager;

    public ManagementService() {
        this.carManager = new CarManager();
        this.clientManager = new ClientManager();
        this.employeeManager = new EmployeeManager();
        this.rentalManager = new RentalManager();
    }

    public void readAllCSV(){
        carManager.readCSV();
        clientManager.readCSV();
        employeeManager.readCSV();
        rentalManager.readCSV(carManager, clientManager, employeeManager);
    }

    public void rentCar(Rental rental){
        rentalManager.add(rental);
        Car rentedCar = carManager.findByPlate(rental.getRentCar().getPlate());
        rentedCar.setCarStatus(CarStatus.RENTED); // update status
        rentedCar.addRental(rental); //update history
        Client client = clientManager.findByAFM(rental.getClient().getAFM());
        client.addRental(rental); //update history
    }

    public void writeAllCSV(){
        carManager.writeCSV();;
        clientManager.writeCSV();
        employeeManager.writeCSV();
        rentalManager.writeCSV();
    }
}
