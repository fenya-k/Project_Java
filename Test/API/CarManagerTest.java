package API;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach; // Χρήσιμο για να έχουμε καθαρό Manager σε κάθε test
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

public class CarManagerTest {

    private CarManager manager;

    // Αυτή η μέθοδος τρέχει ΠΡΙΝ από κάθε @Test.
    // Έτσι έχουμε έναν άδειο, φρέσκο manager κάθε φορά.
    @BeforeEach
    void setUp() {
        manager = new CarManager();
    }

    @Test
    void testAddAndDuplicate() {
        Car car1 = new Car("ABC-1234", "Toyota", "SUV", "RAV4", "2023", "Red");

        // 1. Κανονική προσθήκη
        boolean added = manager.add(car1);
        assertTrue(added, "Το αυτοκίνητο έπρεπε να προστεθεί.");
        assertEquals(1, manager.getSize());

        // 2. Προσπάθεια προσθήκης ίδιου αυτοκινήτου (ίδια πινακίδα)
        Car carDuplicate = new Car("ABC-1234", "Άλλη Μάρκα", "Άλλο", "Άλλο", "2023", "Blue");
        boolean addedAgain = manager.add(carDuplicate);

        assertFalse(addedAgain, "Δεν πρέπει να επιτρέπεται προσθήκη με ίδια πινακίδα.");
        assertEquals(1, manager.getSize()); // Το μέγεθος πρέπει να μείνει 1
    }

    @Test
    void testRemove() {
        Car car1 = new Car("ABC-1234", "Toyota", "SUV", "RAV4", "2023", "Red");
        manager.add(car1);

        // 1. Επιτυχής διαγραφή
        boolean removed = manager.remove(car1);
        assertTrue(removed);
        assertEquals(0, manager.getSize());

        // 2. Διαγραφή ανύπαρκτου
        boolean removedAgain = manager.remove(car1);
        assertFalse(removedAgain);
    }

    @Test
    void testFindByPlate() {
        Car car1 = new Car("ZKP-100", "Fiat", "Small", "Panda", "2020", "White");
        manager.add(car1);

        // Εύρεση υπάρχοντος
        Car found = manager.findByPlate("ZKP-100");
        assertNotNull(found);
        assertEquals("Fiat", found.getBrand());

        // Εύρεση ανύπαρκτου
        Car notFound = manager.findByPlate("XXX-000");
        assertNull(notFound);
    }

    @Test
    void testSearchComplex() {
        // Προσθέτουμε 3 αυτοκίνητα για το σενάριο αναζήτησης
        Car c1 = new Car("P1", "Toyota", "Sedan", "Corolla", "2020", "Red");
        Car c2 = new Car("P2", "Toyota", "SUV", "RAV4", "2021", "Black"); // Διαφορετικό χρώμα/τύπος
        Car c3 = new Car("P3", "Fiat", "Small", "Panda", "2019", "Red");   // Ίδιο χρώμα με c1, άλλη μάρκα

        manager.add(c1);
        manager.add(c2);
        manager.add(c3);

        // Σενάριο 1: Αναζήτηση με Μάρκα "Toyota" (Πρέπει να βρει 2)
        // search(plate, brand, type, model, color, available) -> βάζουμε null στα αδιάφορα
        ArrayList<Car> toyotaList = manager.search(null, "Toyota", null, null, null, null);
        assertEquals(2, toyotaList.size());

        // Σενάριο 2: Αναζήτηση με Χρώμα "Red" (Πρέπει να βρει 2)
        ArrayList<Car> redList = manager.search(null, null, null, null, "Red", null);
        assertEquals(2, redList.size());

        // Σενάριο 3: Αναζήτηση "Toyota" ΚΑΙ "Red" (Πρέπει να βρει 1 - το Corolla)
        ArrayList<Car> specificList = manager.search(null, "Toyota", null, null, "Red", null);
        assertEquals(1, specificList.size());
        assertEquals("P1", specificList.get(0).getPlate());
    }

    @Test
    void testCSVReadWrite() {
        // Προσοχή: Εδώ θα δημιουργήσουμε ένα προσωρινό αρχείο για το τεστ
        String testFile = "test_cars.csv";

        Car c1 = new Car("TEST-1", "A", "B", "C", "2022", "Blue");
        c1.setCarStatus(CarStatus.RENTED); // Έστω ότι είναι νοικιασμένο
        manager.add(c1);

        // 1. Εγγραφή στο αρχείο
        manager.writeCSV(testFile);

        // 2. Δημιουργούμε ΝΕΟ manager για να δούμε αν θα τα διαβάσει
        CarManager newManager = new CarManager();
        newManager.readCSV(testFile);

        // 3. Έλεγχος
        assertEquals(1, newManager.getSize());
        Car loadedCar = newManager.findByPlate("TEST-1");
        assertNotNull(loadedCar);
        assertEquals("Blue", loadedCar.getColor());
        assertEquals(CarStatus.RENTED, loadedCar.getCarStatus()); // Ελέγχουμε αν κράτησε και το status

        // 4. Καθαρισμός (Σβήνουμε το αρχείο test για να μην αφήνουμε σκουπίδια)
        File file = new File(testFile);
        if(file.exists()) {
            file.delete();
        }
    }
}