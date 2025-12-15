package API;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class ManagementServiceTest {

    private ManagementService service;
    private Car car;
    private Client client;
    private Employee employee;
    private Rental rental;

    @BeforeEach
    void setUp() {
        // 1. Δημιουργούμε το Service (που φτιάχνει εσωτερικά τους Managers)
        service = new ManagementService();

        // 2. Ετοιμάζουμε δεδομένα
        car = new Car("SRV-1000", "Toyota", "SUV", "RAV4", "2023", "Black");
        client = new Client("Test", "Client", "123456789", "69000", "test@mail.com");
        employee = new Employee("Admin", "Staff", "admin", "admin@mail.com", "pass");

        // 3. ΣΗΜΑΝΤΙΚΟ: Προσθέτουμε τα δεδομένα στους εσωτερικούς managers του Service
        // Αν δεν το κάνουμε αυτό, η rentCar θα κρασάρει γιατί δεν θα βρίσκει το αμάξι/πελάτη.
        service.getCarManager().add(car);
        service.getClientManager().add(client);
        service.getEmployeeManager().add(employee);

        // 4. Ετοιμάζουμε μια ενοικίαση (χωρίς να την προσθέσουμε ακόμα)
        rental = new Rental(car, client, LocalDate.now(), LocalDate.now().plusDays(5), employee);
    }

    @Test
    void testRentCarLogic() {
        // --- ΕΚΤΕΛΕΣΗ (Action) ---
        service.rentCar(rental);

        // --- ΕΛΕΓΧΟΙ (Assertions) ---

        // 1. Ελέγχουμε αν η ενοικίαση μπήκε στον RentalManager
        assertEquals(1, service.getRentalManager().getSize(), "Η ενοικίαση πρέπει να υπάρχει στον RentalManager");

        // 2. Ελέγχουμε αν άλλαξε το Status του οχήματος (μέσω του CarManager)
        Car storedCar = service.getCarManager().findByPlate("SRV-1000");
        assertEquals(CarStatus.RENTED, storedCar.getCarStatus(), "Το όχημα πρέπει να είναι RENTED");

        // 3. Ελέγχουμε αν ενημερώθηκε το Ιστορικό του Οχήματος
        assertFalse(storedCar.returnList().isEmpty(), "Το ιστορικό του οχήματος δεν πρέπει να είναι άδειο");
        assertEquals(rental.getRentCode(), storedCar.returnList().get(0).getRentCode());

        // 4. Ελέγχουμε αν ενημερώθηκε το Ιστορικό του Πελάτη
        Client storedClient = service.getClientManager().findByAFM("123456789");
        assertFalse(storedClient.returnList().isEmpty(), "Το ιστορικό του πελάτη δεν πρέπει να είναι άδειο");
    }

    @Test
    void testReturnCarLogic() {
        // Πρώτα κάνουμε την ενοικίαση (Setup state)
        service.rentCar(rental);

        // Βεβαιωνόμαστε ότι νοικιάστηκε
        assertEquals(CarStatus.RENTED, car.getCarStatus());

        // --- ΕΚΤΕΛΕΣΗ (Action) ---
        service.returnCar(rental);

        // --- ΕΛΕΓΧΟΙ (Assertions) ---

        // 1. Η ενοικίαση πρέπει να φύγει από τη λίστα των ενεργών ενοικιάσεων
        assertEquals(0, service.getRentalManager().getSize());

        // 2. Το αμάξι πρέπει να γίνει ξανά AVAILABLE
        Car storedCar = service.getCarManager().findByPlate("SRV-1000");
        assertEquals(CarStatus.AVAILABLE, storedCar.getCarStatus());

        // 3. Το ιστορικό όμως πρέπει να ΜΕΙΝΕΙ (δεν σβήνεται κατά την επιστροφή)
        assertFalse(storedCar.returnList().isEmpty(), "Το ιστορικό δεν πρέπει να χάνεται στην επιστροφή");
    }

    /* * ΠΡΟΣΟΧΗ: Τα τεστ για readAllCSV και writeAllCSV είναι επικίνδυνα να τρέξουν
     * εδώ αν δεν έχουμε ρυθμίσει test-files, γιατί θα γράψουν στα πραγματικά σου αρχεία
     * μέσα στο φάκελο DataBase/ManagerFiles/ κλπ.
     * * Καλύτερα να μην τα συμπεριλάβουμε στο Unit Test αν δεν αλλάξουμε τα paths σε test paths.
     */
}