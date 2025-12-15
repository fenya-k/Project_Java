package API;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class ClientTest {

    @Test
    void testConstructorAndInheritedFields() {
        // Δοκιμή δημιουργίας Πελάτη (Name, Surname, AFM, Phone, Email)
        Client client = new Client("Giannis", "Papadopoulos", "123456789", "6900000000", "giannis@email.com");

        // Έλεγχος των πεδίων της κλάσης Client
        assertEquals("123456789", client.getAFM());
        assertEquals("6900000000", client.getPhone());

        // Έλεγχος των πεδίων που κληρονομεί από το Person
        assertEquals("Giannis", client.getName());
        assertEquals("Papadopoulos", client.getSurname());
        assertEquals("giannis@email.com", client.getEmail());

        // Έλεγχος ότι η λίστα ενοικιάσεων αρχικοποιήθηκε
        assertNotNull(client.returnList());
        assertTrue(client.returnList().isEmpty());
    }

    @Test
    void testSetters() {
        Client client = new Client("Giannis", "Papadopoulos", "123456789", "6900000000", "giannis@email.com");

        // Αλλαγή τηλεφώνου
        client.setPhone("2101234567");
        assertEquals("2101234567", client.getPhone());

        // Αλλαγή ονόματος (κληρονομείται από Person)
        client.setName("Giorgos");
        assertEquals("Giorgos", client.getName());
    }

    @Test
    void testAddRentalHistory() {
        Client client = new Client("Giannis", "Papadopoulos", "123456789", "6900000000", "giannis@email.com");

        // Χρειαζόμαστε dummy αντικείμενα για να φτιάξουμε μια ενοικίαση
        // (Δεν μας νοιάζουν τα δεδομένα τους, αρκεί να υπάρχουν)
        Car dummyCar = new Car("ABC-1000", "Fiat", "Small", "Panda", "2020", "White");
        Employee dummyEmployee = new Employee("Emp", "Name", "user", "mail", "pass");

        // Δημιουργία Ενοικίασης
        Rental rental = new Rental(dummyCar, client, LocalDate.now(), LocalDate.now().plusDays(5), dummyEmployee);

        // 1. Δοκιμή επιτυχούς προσθήκης
        boolean added = client.addRental(rental);
        assertTrue(added, "Η ενοικίαση έπρεπε να προστεθεί επιτυχώς");
        assertEquals(1, client.returnList().size());

        // 2. Δοκιμή αποφυγής διπλότυπων (με βάση τον κωδικό ενοικίασης)
        boolean addedAgain = client.addRental(rental);
        assertFalse(addedAgain, "Η ίδια ενοικίαση δεν πρέπει να προστεθεί δεύτερη φορά");
        assertEquals(1, client.returnList().size()); // Το μέγεθος πρέπει να μείνει 1
    }

    @Test
    void testToString() {
        Client client = new Client("Maria", "Demo", "999999999", "6999999999", "maria@test.com");

        String result = client.toString();

        // Ελέγχουμε αν το String περιέχει τα βασικά στοιχεία
        assertTrue(result.contains("Maria"));
        assertTrue(result.contains("999999999"));
    }
}