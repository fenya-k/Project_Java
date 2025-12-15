package API;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class RentalTest {

    @Test
    void testConstructorAndGetters() {
        // 1. Προετοιμασία (Δημιουργούμε τα απαραίτητα αντικείμενα)
        Car car = new Car("ABC-1234", "Toyota", "SUV", "RAV4", "2023", "Red");
        Client client = new Client("Giannis", "Papadopoulos", "123456789", "6900000000", "giannis@email.com");
        Employee emp = new Employee("Nikos", "Georgiou", "adminUser", "admin@rent.com", "1234");

        LocalDate start = LocalDate.of(2023, 12, 1);
        LocalDate end = LocalDate.of(2023, 12, 5);

        // 2. Δημιουργία της ενοικίασης
        Rental rental = new Rental(car, client, start, end, emp);

        // 3. Έλεγχοι
        assertEquals("ABC-1234", rental.getRentCar().getPlate()); // Ελέγχουμε αν πήρε το σωστό αμάξι
        assertEquals("123456789", rental.getClient().getAFM());   // Ελέγχουμε τον σωστό πελάτη
        assertEquals("adminUser", rental.getEmployee().getUsername()); // Ελέγχουμε τον υπάλληλο
        assertEquals(start, rental.getStartDate());
        assertEquals(end, rental.getEndDate());
    }

    @Test
    void testSetters() {
        // Προετοιμασία με τυχαία δεδομένα
        Car car = new Car("X-1", "A", "B", "C", "2020", "Blue");
        Client client = new Client("A", "B", "C", "D", "E");
        Employee emp = new Employee("F", "G", "H", "I", "J");
        Rental rental = new Rental(car, client, LocalDate.now(), LocalDate.now(), emp);

        // Αλλαγή ημερομηνίας επιστροφής
        LocalDate newEnd = LocalDate.of(2024, 1, 1);
        rental.setEndDate(newEnd);

        assertEquals(newEnd, rental.getEndDate());
    }

    @Test
    void testToString() {
        // 1. Προετοιμασία συγκεκριμένων δεδομένων για να ξέρουμε τι ψάχνουμε
        Car car = new Car("ZKP-5555", "Fiat", "Small", "Panda", "2020", "White");
        Client client = new Client("Maria", "Dimou", "999999999", "6999999999", "m@test.com");
        Employee emp = new Employee("Kostas", "K", "kos_admin", "k@test.com", "pass");

        LocalDate start = LocalDate.of(2023, 10, 10);
        LocalDate end = LocalDate.of(2023, 10, 15);

        Rental rental = new Rental(car, client, start, end, emp);

        // 2. Εκτέλεση toString
        String result = rental.toString();

        // 3. Έλεγχοι (Verify)
        // Βεβαιωνόμαστε ότι το String περιέχει τα κρίσιμα δεδομένα
        assertTrue(result.contains("Rental:"), "Πρέπει να ξεκινάει ή να περιέχει τη λέξη Rental");
        assertTrue(result.contains("ZKP-5555"), "Πρέπει να εμφανίζει την πινακίδα του οχήματος");
        assertTrue(result.contains("Maria"), "Πρέπει να εμφανίζει το όνομα του πελάτη");
        assertTrue(result.contains("Dimou"), "Πρέπει να εμφανίζει το επίθετο του πελάτη");
        assertTrue(result.contains("kos_admin"), "Πρέπει να εμφανίζει το username του υπαλλήλου");
        assertTrue(result.contains("2023-10-10"), "Πρέπει να εμφανίζει την ημερομηνία έναρξης");
    }
}