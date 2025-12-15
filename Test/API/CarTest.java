package API;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CarTest {

    @Test
    void testConstructorAndGetters() {
        // Test the standard constructor
        Car car = new Car("ABC-1234", "Toyota", "SUV", "RAV4", "2023", "Red");

        assertEquals("ABC-1234", car.getPlate());
        assertEquals("Toyota", car.getBrand());
        assertEquals("Red", car.getColor());

        // Check default status
        assertEquals(CarStatus.AVAILABLE, car.getCarStatus());
        assertTrue(car.isAvailable());

        // Check that the list is initialized (not null)
        assertNotNull(car.returnList());
        assertTrue(car.returnList().isEmpty());
    }

    @Test
    void testIdCounterLogic() {
        // Test that loading a car with a high ID updates the static counter
        // Create a car with ID 100
        Car carLoaded = new Car(100, "XYZ-9999", "Fiat", "Small", "Panda", "2020", "White", CarStatus.AVAILABLE);

        // Create a BRAND NEW car. It should get ID 101, not 1 or 2.
        Car carNew = new Car("NEW-0001", "Tesla", "Sedan", "Model 3", "2024", "Black");

        assertEquals(100, carLoaded.getId());
        assertEquals(101, carNew.getId());
    }

    @Test
    void testStatusChange() {
        Car car = new Car("ABC-1234", "Toyota", "SUV", "RAV4", "2023", "Red");

        // Change to RENTED
        car.setCarStatus(CarStatus.RENTED);

        assertFalse(car.isAvailable());
        assertEquals(CarStatus.RENTED, car.getCarStatus());

        // Change back to AVAILABLE
        car.setCarStatus(CarStatus.AVAILABLE);
        assertTrue(car.isAvailable());
    }

    @Test
    void testAddRentalHistory() {
        Car car = new Car("ABC-1234", "Toyota", "SUV", "RAV4", "2023", "Red");

        // Create dummy objects for the Rental (Client/Employee can be null if not used in equals/hashcode)
        // Note: We assume Rental constructor allows nulls for this test, or you create dummy Client/Employee
        Client dummyClient = new Client("Test", "User", "123456789", "000", "test@test.com");
        Employee dummyEmployee = new Employee("Emp", "Loyee", "admin", "mail", "pass");

        Rental rental1 = new Rental(car, dummyClient, LocalDate.now(), LocalDate.now().plusDays(3), dummyEmployee);

        // 1. Test Adding
        boolean added = car.addRental(rental1);
        assertTrue(added, "Rental should be added successfully");
        assertEquals(1, car.returnList().size());

        // 2. Test Duplicate Logic (Adding the exact same rental)
        boolean addedAgain = car.addRental(rental1);
        assertFalse(addedAgain, "Duplicate rental should not be added");
        assertEquals(1, car.returnList().size());
    }

    @Test
    void testReturnListEncapsulation() {
        Car car = new Car("ABC-1234", "Toyota", "SUV", "RAV4", "2023", "Red");
        ArrayList<Rental> list = car.returnList();

        assertNotNull(list);
    }

    @Test
    void testToString() {
        // 1. Δημιουργούμε ένα αυτοκίνητο με συγκεκριμένα στοιχεία
        Car car = new Car("ABC-1234", "Toyota", "SUV", "RAV4", "2023", "Red");

        // 2. Παίρνουμε το αποτέλεσμα της toString
        String result = car.toString();

        // 3. Ελέγχουμε αν περιέχει τις σημαντικές πληροφορίες
        assertTrue(result.contains("Car:"));          // Επιβεβαιώνουμε ότι ξεκινάει σωστά
        assertTrue(result.contains("ABC-1234"));      // Πρέπει να έχει την πινακίδα
        assertTrue(result.contains("Toyota"));        // Πρέπει να έχει τη μάρκα
        assertTrue(result.contains("RAV4"));          // Πρέπει να έχει το μοντέλο
        assertTrue(result.contains("AVAILABLE"));     // Πρέπει να δείχνει το status (αρχικά είναι AVAILABLE)
    }
}