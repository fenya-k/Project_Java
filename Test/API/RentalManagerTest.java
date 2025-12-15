package API;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDate;

class RentalManagerTest {

    private RentalManager manager;
    private Car car;
    private Client client;
    private Employee employee;
    private Rental rental;

    @BeforeEach
    void setUp() {
        manager = new RentalManager();

        // Δημιουργία βοηθητικών αντικειμένων για κάθε τεστ
        car = new Car("ABC-1234", "Toyota", "SUV", "RAV4", "2023", "Red");
        // Βεβαιωνόμαστε ότι το αμάξι είναι αρχικά AVAILABLE
        car.setCarStatus(CarStatus.AVAILABLE);

        client = new Client("Giannis", "Papadopoulos", "123456789", "6900000000", "g@test.com");
        employee = new Employee("Admin", "User", "admin", "admin@test.com", "1234");

        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(3);

        rental = new Rental(car, client, start, end, employee);
    }

    @Test
    void testAddRentalSuccess() {
        // 1. Προσθήκη ενοικίασης
        boolean added = manager.add(rental);

        assertTrue(added, "Η ενοικίαση έπρεπε να προστεθεί.");
        assertEquals(1, manager.getSize());

        // 2. ΕΛΕΓΧΟΣ ΚΛΕΙΔΙ: Άλλαξε το Status του αυτοκινήτου;
        assertEquals(CarStatus.RENTED, car.getCarStatus(), "Το αυτοκίνητο πρέπει να γίνει RENTED μετά την ενοικίαση.");
    }

    @Test
    void testAddRentalDuplicate() {
        manager.add(rental); // Προσθήκη πρώτης φοράς

        // Προσπάθεια προσθήκης της ίδιας ενοικίασης (ίδιο rentCode)
        boolean addedAgain = manager.add(rental);

        assertFalse(addedAgain, "Δεν πρέπει να επιτρέπεται διπλή προσθήκη ίδιου ID.");
        assertEquals(1, manager.getSize());
    }

    @Test
    void testAddRentalUnavailableCar() {
        // Σενάριο: Το αυτοκίνητο είναι ήδη νοικιασμένο (π.χ. από άλλη ενοικίαση)
        car.setCarStatus(CarStatus.RENTED);

        // Προσπάθεια δημιουργίας νέας ενοικίασης για το ίδιο αμάξι
        Rental newRental = new Rental(car, client, LocalDate.now(), LocalDate.now(), employee);

        boolean added = manager.add(newRental);

        assertFalse(added, "Δεν πρέπει να νοικιάζεται αυτοκίνητο που δεν είναι AVAILABLE.");
        assertEquals(0, manager.getSize());
    }

    @Test
    void testRemoveRental() {
        // Προσθέτουμε πρώτα την ενοικίαση
        manager.add(rental);
        assertEquals(CarStatus.RENTED, car.getCarStatus()); // Βεβαίωση ότι νοικιάστηκε

        // 1. Διαγραφή
        boolean removed = manager.remove(rental);

        assertTrue(removed, "Η ενοικίαση έπρεπε να διαγραφεί.");
        assertEquals(0, manager.getSize());

        // 2. ΕΛΕΓΧΟΣ ΚΛΕΙΔΙ: Ελευθερώθηκε το αυτοκίνητο;
        assertEquals(CarStatus.AVAILABLE, car.getCarStatus(), "Το αυτοκίνητο πρέπει να γίνει AVAILABLE μετά τη διαγραφή της ενοικίασης.");
    }

    @Test
    void testRemoveNonExistent() {
        boolean removed = manager.remove(rental); // Η λίστα είναι άδεια
        assertFalse(removed);
    }

    // Σημείωση: Το τεστ αυτό προϋποθέτει ότι έχεις υλοποιήσει τις μεθόδους readCSV/writeCS
    // στον RentalManager (που φαίνεται ότι τις έχεις στο implements ReadWriteCSV)
    /*
    @Test
    void testCSVReadWrite() {
        String testFile = "test_rentals.csv";
        manager.add(rental); // Προσθέτουμε 1 ενοικίαση

        // 1. Εγγραφή
        manager.writeCSV(testFile);

        // 2. Ανάγνωση σε ΝΕΟ manager
        // ΠΡΟΣΟΧΗ: Επειδή ο RentalManager στον Constructor δεν παίρνει πια CarManager/ClientManager,
        // πρέπει η readCSV σου να δουλεύει αυτόνομα ή να έχεις αλλάξει τη λογική της.
        // Αν η readCSV χρειάζεται Managers για να βρει τα αντικείμενα, αυτό το τεστ ίσως αποτύχει
        // αν δεν έχεις ρυθμίσει σωστά τη σύνδεση.
        // Για την ώρα ελέγχουμε τη βασική ροή:

        /* * Επειδή η readCSV σου πιθανόν ψάχνει CarManager/ClientManager για να βρει τα αντικείμενα
         * και ο κενός Constructor RentalManager() δεν τους έχει, αυτό το κομμάτι είναι δύσκολο να
         * τεσταριστεί χωρίς Dependency Injection.
         * * Θα αφήσω τον κώδικα σε σχόλια. Αν έχεις φτιάξει την readCSV να δουλεύει, βγάλε τα σχόλια.
         */

        /*
        RentalManager newManager = new RentalManager();
        newManager.readCSV(testFile);

        // Αν η readCSV δουλεύει σωστά:
        assertEquals(1, newManager.getSize());
        */

        /* Καθαρισμός
        File file = new File(testFile);
        if(file.exists()) file.delete();
    }
    */
}