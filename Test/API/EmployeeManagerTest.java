package API;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

class EmployeeManagerTest {

    private EmployeeManager manager;

    @BeforeEach
    void setUp() {
        manager = new EmployeeManager();
    }

    @Test
    void testAddAndDuplicate() {
        Employee e1 = new Employee("Nikos", "Papas", "admin", "admin@test.com", "1234");

        // 1. Κανονική προσθήκη
        assertTrue(manager.add(e1));
        assertEquals(1, manager.getSize());

        // 2. Προσπάθεια προσθήκης με ίδιο username (case insensitive check στο add?)
        // Στον κώδικά σου το add έχει: e.getUsername().equalsIgnoreCase(...)
        Employee eDuplicate = new Employee("Maria", "Lappa", "ADMIN", "maria@test.com", "5555");

        assertFalse(manager.add(eDuplicate), "Δεν πρέπει να επιτρέπεται ίδιο username (ακόμα και με κεφαλαία)");
        assertEquals(1, manager.getSize());
    }

    @Test
    void testRemove() {
        Employee e1 = new Employee("Nikos", "Papas", "user1", "u1@test.com", "1234");
        manager.add(e1);

        // 1. Επιτυχής διαγραφή
        assertTrue(manager.remove(e1));
        assertEquals(0, manager.getSize());

        // 2. Διαγραφή ανύπαρκτου
        assertFalse(manager.remove(e1));
    }

    @Test
    void testLogin() {
        // Προσθήκη υπαλλήλου: username="user", password="password123"
        Employee e1 = new Employee("Test", "User", "user", "test@mail.com", "password123");
        manager.add(e1);

        // 1. Επιτυχές Login
        Employee loggedIn = manager.login("user", "password123");
        assertNotNull(loggedIn, "Το login έπρεπε να πετύχει");
        assertEquals("user", loggedIn.getUsername());

        // 2. Λάθος Password
        Employee wrongPass = manager.login("user", "wrongPASS");
        assertNull(wrongPass, "Το login έπρεπε να αποτύχει με λάθος κωδικό");

        // 3. Λάθος Username
        Employee wrongUser = manager.login("wrongUser", "password123");
        assertNull(wrongUser, "Το login έπρεπε να αποτύχει με ανύπαρκτο χρήστη");
    }

    @Test
    void testChangePassword() {
        Employee e1 = new Employee("Test", "User", "userChanged", "test@mail.com", "oldPass");
        manager.add(e1);

        // 1. Αλλαγή κωδικού με ΛΑΘΟΣ παλιό κωδικό
        boolean resultFail = manager.changePassword("userChanged", "wrongOld", "newPass");
        assertFalse(resultFail);
        assertEquals("oldPass", e1.getPassword()); // Ο κωδικός δεν πρέπει να αλλάξει

        // 2. Αλλαγή κωδικού με ΣΩΣΤΟ παλιό κωδικό
        boolean resultSuccess = manager.changePassword("userChanged", "oldPass", "newSecret");
        assertTrue(resultSuccess);

        // Επιβεβαίωση ότι άλλαξε
        assertEquals("newSecret", e1.getPassword());

        // Δοκιμή Login με τον νέο κωδικό
        assertNotNull(manager.login("userChanged", "newSecret"));
    }

    @Test
    void testFindByUsername() {
        manager.add(new Employee("A", "B", "findMe", "email", "pass"));

        // Εύρεση (Case Insensitive σύμφωνα με τον κώδικά σου)
        Employee found = manager.findByUsername("FINDME");
        assertNotNull(found);
        assertEquals("findMe", found.getUsername());

        assertNull(manager.findByUsername("GhostUser"));
    }

    @Test
    void testCSVReadWrite() {
        String testFile = "test_employees.csv";

        Employee e1 = new Employee("John", "Doe", "jdoe", "john@test.com", "secret");
        manager.add(e1);

        // 1. Εγγραφή
        manager.writeCSV(testFile);

        // 2. Ανάγνωση σε ΝΕΟ manager
        EmployeeManager newManager = new EmployeeManager();
        newManager.readCSV(testFile);

        // 3. Έλεγχος
        assertEquals(1, newManager.getSize());
        Employee loaded = newManager.findByUsername("jdoe");
        assertNotNull(loaded);
        assertEquals("secret", loaded.getPassword());
        assertEquals("john@test.com", loaded.getEmail());

        // 4. Καθαρισμός
        File file = new File(testFile);
        if(file.exists()) file.delete();
    }
}