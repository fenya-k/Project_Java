import API.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
/*
        // TEST
        Car car = new Car("ΙΚΥ1294", "Porche", "Macan", "Corolla", "2019", "Ασημί");
        Client client = new Client("12345", "φένια", "κομπατσιάρη", "6957", "φενια@");
        Employee employee = new Employee("τζον", "τζο", "γιαννης", "γιαννης@", "121212");

        LocalDate date1 = LocalDate.of(2025, 12, 6);
        LocalDate date2 = LocalDate.of(2025, 12, 10);
        Rental rental = new Rental(car, client, date1, date2, employee);

        System.out.println(car);
        System.out.println(client);
        System.out.println(employee);
        System.out.println(rental);
        System.out.println("\n");

        // CAR MANAGER
        CarManager takis = new CarManager();
        takis.readCSV();
        takis.printCars();
        takis.addCar(car);
        takis.writeCSV();
        System.out.println("\n");

        ArrayList<Car> foundCars = takis.searchCar(null, "Toyota", null, null, null, null);
        for(Car c : foundCars){
            System.out.println("Found car: ");
            System.out.println(c);
        }
        System.out.println("\n");
*/
        // CLIENT MANAGER
       /* API.ClientManager sakis = new API.ClientManager();
        sakis.readCSV();
        sakis.printClients(); */

        // RENTAL MANAGER
        System.out.println("Test");
        Car carTest=new Car("test1","Toyota","SUV","Corolla","2025","White");
        Client clientTest=new Client("12345","Erica","kkk","1234567","erica@");
        Employee employeeTest=new Employee("Emp1","SSS","emp1SS","emp1@","pass1");

        LocalDate date1=LocalDate.of(2025,12,6);
        LocalDate date2=LocalDate.of(2025,12,15);

        Rental rentalTest=new Rental(carTest,clientTest,date1,date2,employeeTest);


        System.out.println(carTest);
        System.out.println(clientTest);
        System.out.println(employeeTest);
        System.out.println(rentalTest);

        System.out.println("After rent(scar status):"+carTest.getCarStatus());
        System.out.println("--- ТЕСТ CAR MANAGER ---");
        CarManager carManager = new CarManager();
        System.out.println("Cars loaded from file: " + carManager.getSize());

        System.out.println("Trying to add new car");
        carManager.addCar(carTest);
       System.out.println("\n--- ТЕСТ CLIENT MANAGER ---");

        ClientManager clientManager = new ClientManager();

        System.out.println("Clients loaded from file: " + clientManager.getClients().size());

        System.out.println("Trying add a test client");
        clientManager.addClient(clientTest);


        System.out.println("\n--- ТЕСТ EMPLOYEE MANAGER ---");
        EmployeeManager employeeManager = new EmployeeManager();
        System.out.println("Employees loaded from file:  " + employeeManager.getEmployees().size());
        // Тест логина
        Employee loginResult = employeeManager.login("jsmith", "password1");
        if (loginResult != null) {
            System.out.println("Success " + loginResult.getUsername());
        } else {
            System.out.println("Error logging in!");
        }


        System.out.println("\n--- ТЕСТ RENTAL MANAGER(new))--");
        RentalManager rentalManager = new RentalManager();
        System.out.println("Trying renting car through manager...");

        Car carForRent = new Car("RENT-ME", "Fiat", "Compact", "Punto", "2020", "White");


        Rental newRental = new Rental(carForRent, clientTest, LocalDate.now(), LocalDate.now().plusDays(5), loginResult);

       rentalManager.createRental(newRental);

        System.out.println("List of all rents");
        rentalManager.printRentals();
        System.out.println("Car status after rent(through manager) " + carForRent.getCarStatus());




    }
}
