import API.*;

public class Main {
    public static void main(String[] args) {
        /*
        // TEST
        Car car = new Car("ΙΚΥ1294", "Porche", "Macan", "Corolla", "2019", "Ασημί");
        Car car2 = new Car("ΙΚΥ1294", "Porche", "Macan", "Corolla", "2019", "Ασημί");
        Client client = new Client("φένια", "κομπ", "12345", "6957", "φενια@");
        Client client2 = new Client("erica", "kub", "67890", "69", "erica@");
        Employee employee = new Employee("τζον", "τζο", "γιαννης", "γιαννης@", "121212");

        LocalDate dateout1 = LocalDate.of(2025, 12, 6);
        LocalDate datein1 = LocalDate.of(2025, 12, 10);
        Rental rental = new Rental(car, client, dateout1, datein1, employee);
        LocalDate dateout2 = LocalDate.of(2025, 12, 9);
        LocalDate datein2 = LocalDate.of(2025, 12, 15);
        Rental rental2 = new Rental(car2, client2, dateout2, datein2, employee);

        System.out.println(car);
        System.out.println(client);
        System.out.println(employee);
        System.out.println(rental);
        System.out.println("\n");

        // CAR MANAGER
        CarManager takis = new CarManager();
        takis.readCSV();
        takis.print();
        takis.add(car);
        takis.writeCSV();
        System.out.println("\n");

        ArrayList<Car> foundCars = takis.search(null, "Toyota", null, null, null, null);
        for (Car c : foundCars) {
            System.out.println("Found car: ");
            System.out.println(c);
        }
        System.out.println("\n");

        // CLIENT MANAGER
        ClientManager sakis = new ClientManager();
        //sakis.readCSV();
        sakis.add(client);
        sakis.add(client2);
        sakis.writeCSV();
        sakis.print();
        Client foundClient = sakis.search("erica", null, null, null, null);
        System.out.println(foundClient);
        System.out.println("\n");


        // EMPLOYEE MANAGER


        // RENTAL MANAGER
        RentalManager maria = new RentalManager(takis, sakis);
        maria.add(rental);
        maria.add(rental2);
        maria.print();
        maria.writeCSV();
        System.out.println(maria.getSize());
        maria.remove(rental2);
        System.out.println(maria.getSize());
*/

//        System.out.println("Initializing managers");
//        //CAR MANAGER
//        CarManager carManager = new CarManager();
//        carManager.readCSV();
//        System.out.println("Cars loaded: " + carManager.getSize());
//
//        //CLIENT MANAGER
//        ClientManager clientManager = new ClientManager();
//        clientManager.readCSV();
//        System.out.println("Clients loaded: " + clientManager.getSize());
//
//        //EMPLOYEE MANAGER
//        EmployeeManager employeeManager = new EmployeeManager();
//        System.out.println("Employees loaded: " + employeeManager.getList().size());
//
//        //RENTAL MANAGER
//        RentalManager rentalManager = new RentalManager();
//        rentalManager.readCSV(carManager, clientManager, employeeManager);
//        System.out.println("Rentals loaded: " + rentalManager.getSize());
//
//        System.out.println("Test data(creating)");
//        //test car
//        Car testCar = new Car("test1", "Kia", "Sport", "Rio", "2025", "White");
//        carManager.add(testCar);
//
//        //client test
//        Client testClient = new Client("George", "Takis", "afm12345", "9876543", "george@mail");
//        clientManager.add(testClient);
//
//        Employee testEmployee = employeeManager.login("jsmith", "password1");
//        if (testEmployee == null) {
//            System.out.println("Error(login failed).Using the first employee from list");
//            testEmployee = employeeManager.getList().get(0);
//        } else {
//            System.out.println("Success. Logged in!(as " + testEmployee.getUsername() + ")");
//        }
//
//        System.out.println("Rental process(test)");
//        LocalDate dateOut = LocalDate.now();
//        LocalDate dateIn = LocalDate.now().plusDays(5);
//
//        Rental newRental = new Rental(testCar, testClient, dateOut, dateIn, testEmployee);
//
//        System.out.println("Attempt to add rental");
//        boolean added = rentalManager.add(newRental);
//        if (added) {
//            System.out.println("Rental added. Car status: " + testCar.getCarStatus());
//        } else {
//            System.out.println("Error.Rental not added");
//        }
//
//        System.out.println("All rentals");
//        rentalManager.print();
//
//        carManager.writeCSV();
//        clientManager.writeCSV();
//        rentalManager.writeCSV();
//
//        //new methods testing
//        System.out.println("Changing password(test)");
//        employeeManager.changePassword("jsmith","password1","password123");
//
//        Employee oldPasswordCheck=employeeManager.login("jsmith","password1");
//        if(oldPasswordCheck==null){
//            System.out.println("Old password is no longer valid");
//        }
//
//        Employee newPasswordCheck=employeeManager.login("jsmith","password123");
//        if(newPasswordCheck!=null){
//            System.out.println("New password works");
//        }
//
//        System.out.println("Available Cars List:");
//        carManager.printAvailableCars();



        ManagementService service = new ManagementService();
        service.readAllCSV();


   }
}
