package API;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void testConstructorAndInheritance() {
        // Δημιουργία Employee (Name, Surname, Username, Email, Password)
        Employee emp = new Employee("Nikos", "Georgiou", "nickgeo", "nikos@company.com", "secret123");

        // 1. Έλεγχος των πεδίων της κλάσης Employee
        assertEquals("nickgeo", emp.getUsername());
        assertEquals("secret123", emp.getPassword());

        // 2. Έλεγχος των πεδίων που κληρονομεί από το Person
        assertEquals("Nikos", emp.getName());
        assertEquals("Georgiou", emp.getSurname());
        assertEquals("nikos@company.com", emp.getEmail());
    }

    @Test
    void testSetters() {
        Employee emp = new Employee("Nikos", "Georgiou", "nickgeo", "nikos@company.com", "1234");

        // Αλλαγή κωδικού
        emp.setPassword("newPass987");
        assertEquals("newPass987", emp.getPassword());

        // Αλλαγή username
        emp.setUsername("admin_nikos");
        assertEquals("admin_nikos", emp.getUsername());

        // Αλλαγή email (κληρονομείται από Person)
        emp.setEmail("admin@company.com");
        assertEquals("admin@company.com", emp.getEmail());
    }

    @Test
    void testToString() {
        Employee emp = new Employee("Anna", "Vissi", "annav", "anna@music.com", "pass");

        String result = emp.toString();

        // Επιβεβαιώνουμε ότι το toString περιέχει τα βασικά στοιχεία
        // Σημείωση: Στον κώδικά σου το toString ξεκινάει με "API.Employee:"
        assertTrue(result.contains("API.Employee"));
        assertTrue(result.contains("annav")); // username
        assertTrue(result.contains("pass"));  // password
    }
}