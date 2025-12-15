package API;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class HistoryInterfaceTest {

    // Αυτή η μέθοδος είναι το "κοινό τεστ".
    // Δέχεται ΟΠΟΙΟΔΗΠΟΤΕ αντικείμενο είναι τύπου History (είτε Car, είτε Client)
    // και ελέγχει αν δουλεύουν σωστά οι μέθοδοι του Interface.
    void verifyHistoryBehavior(History historyEntity) {

        // 1. Βεβαιωνόμαστε ότι η λίστα είναι αρχικά άδεια (και όχι null)
        assertNotNull(historyEntity.returnList());
        assertTrue(historyEntity.returnList().isEmpty());

        // 2. Δημιουργούμε μια ψεύτικη ενοικίαση
        // (Χρειαζόμαστε dummy objects απλά για να γεμίσουμε τα κενά)
        Car dummyCar = new Car("TEST-1", "A", "B", "C", "2020", "Color");
        Client dummyClient = new Client("Name", "Sur", "123", "000", "mail");
        Employee dummyEmp = new Employee("Emp", "Nam", "user", "mail", "pass");

        Rental rental = new Rental(dummyCar, dummyClient, LocalDate.now(), LocalDate.now(), dummyEmp);

        // 3. Δοκιμάζουμε την addRental (Μέθοδος του Interface)
        boolean added = historyEntity.addRental(rental);

        assertTrue(added, "Η μέθοδος addRental του Interface απέτυχε");
        assertEquals(1, historyEntity.returnList().size(), "Η λίστα έπρεπε να έχει 1 στοιχείο");

        // 4. Δοκιμάζουμε την printRentals (απλά καλούμε για να δούμε ότι δεν σκάει)
        assertDoesNotThrow(() -> historyEntity.printRentals());
    }

    @Test
    void testCarAsHistory() {
        // Δημιουργούμε Car, αλλά το αντιμετωπίζουμε ως History
        History carHistory = new Car("ABC-1234", "Toyota", "SUV", "RAV4", "2023", "Red");

        // Τρέχουμε τον κοινό έλεγχο
        verifyHistoryBehavior(carHistory);
    }

    @Test
    void testClientAsHistory() {
        // Δημιουργούμε Client, αλλά το αντιμετωπίζουμε ως History
        History clientHistory = new Client("Giannis", "P", "123456789", "69000", "g@test.com");

        // Τρέχουμε τον ίδιο ακριβώς έλεγχο!
        verifyHistoryBehavior(clientHistory);
    }
}