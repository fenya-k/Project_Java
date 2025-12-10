import API.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

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


    }
}
