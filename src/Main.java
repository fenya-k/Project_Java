import API.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

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

        // CLIENT MANAGER
       /* API.ClientManager sakis = new API.ClientManager();
        sakis.readCSV();
        sakis.printClients(); */

        // RENTAL MANAGER


    }
}
