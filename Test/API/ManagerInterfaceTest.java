package API;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class ManagerInterfaceTest {

    // --- Η ΓΕΝΙΚΗ ΜΕΘΟΔΟΣ ΕΛΕΓΧΟΥ (GENERIC) ---
    // Αυτή η μέθοδος δεν ξέρει αν ελέγχει Car ή Client.
    // Ξέρει μόνο ότι ελέγχει έναν Manager<T> και ένα αντικείμενο T.
    <T> void verifyManagerContract(Manager<T> manager, T item) {

        // 1. Αρχικός έλεγχος: Η λίστα πρέπει να είναι άδεια (σε νέο manager)
        assertNotNull(manager.getList());
        assertEquals(0, manager.getSize());

        // 2. Δοκιμή ADD
        boolean added = manager.add(item);
        assertTrue(added, "Η μέθοδος add() απέτυχε");

        // Επιβεβαίωση ότι μπήκε στη λίστα
        assertEquals(1, manager.getSize());
        assertFalse(manager.getList().isEmpty());
        // Σημείωση: Εδώ ελέγχουμε αν η λίστα περιέχει το αντικείμενο.
        // Αν έχεις κάνει Override την equals(), θα συγκρίνει τιμές. Αν όχι, μνήμη.
        // Και στις δύο περιπτώσεις, επειδή είναι το ίδιο object instance, θα το βρει.
        assertTrue(manager.getList().contains(item));

        // 3. Δοκιμή PRINT (Απλά ελέγχουμε ότι δεν πετάει Exception)
        assertDoesNotThrow(() -> manager.print());

        // 4. Δοκιμή REMOVE
        boolean removed = manager.remove(item);
        assertTrue(removed, "Η μέθοδος remove() απέτυχε");

        // Επιβεβαίωση ότι βγήκε
        assertEquals(0, manager.getSize());
        assertTrue(manager.getList().isEmpty());
    }

    // --- ΤΕΣΤ ΓΙΑ CAR MANAGER ---
    @Test
    void testCarManagerImplementation() {
        CarManager carManager = new CarManager();
        Car car = new Car("TEST-CAR", "A", "B", "C", "2020", "Blue");

        // Καλώ τη γενική μέθοδο
        verifyManagerContract(carManager, car);
    }

    // --- ΤΕΣΤ ΓΙΑ CLIENT MANAGER ---
    @Test
    void testClientManagerImplementation() {
        ClientManager clientManager = new ClientManager();
        Client client = new Client("Test", "User", "123456789", "000", "mail");

        verifyManagerContract(clientManager, client);
    }

    // --- ΤΕΣΤ ΓΙΑ EMPLOYEE MANAGER ---
    @Test
    void testEmployeeManagerImplementation() {
        EmployeeManager empManager = new EmployeeManager();
        Employee emp = new Employee("Name", "Sur", "userTest", "mail", "pass");

        verifyManagerContract(empManager, emp);
    }

    // --- ΤΕΣΤ ΓΙΑ RENTAL MANAGER ---
    @Test
    void testRentalManagerImplementation() {
        RentalManager rentalManager = new RentalManager();

        // Προσοχή: Για να μπει το Rental, το αμάξι πρέπει να είναι AVAILABLE
        Car car = new Car("RENT-TEST", "A", "B", "C", "2020", "Red");
        car.setCarStatus(CarStatus.AVAILABLE);

        Client client = new Client("A", "B", "C", "D", "E");
        Employee emp = new Employee("F", "G", "H", "I", "J");

        Rental rental = new Rental(car, client, LocalDate.now(), LocalDate.now(), emp);

        verifyManagerContract(rentalManager, rental);
    }
}