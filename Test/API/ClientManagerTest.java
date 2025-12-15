package API;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

class ClientManagerTest {

    private ClientManager manager;

    // Τρέχει πριν από κάθε τεστ για να έχουμε "καθαρό" manager
    @BeforeEach
    void setUp() {
        manager = new ClientManager();
    }

    @Test
    void testAddAndDuplicate() {
        Client c1 = new Client("Giannis", "Papadopoulos", "123456789", "6900000000", "giannis@test.com");

        // 1. Κανονική προσθήκη
        boolean added = manager.add(c1);
        assertTrue(added, "Ο πελάτης έπρεπε να προστεθεί.");
        assertEquals(1, manager.getSize());

        // 2. Προσπάθεια προσθήκης με ίδιο ΑΦΜ
        Client cDuplicate = new Client("Giorgos", "AlloEponymo", "123456789", "6911111111", "other@test.com");
        boolean addedAgain = manager.add(cDuplicate);

        assertFalse(addedAgain, "Δεν πρέπει να επιτρέπεται πελάτης με ίδιο ΑΦΜ.");
        assertEquals(1, manager.getSize()); // Το μέγεθος παραμένει 1
    }

    @Test
    void testRemove() {
        Client c1 = new Client("Maria", "Dimou", "999999999", "6999999999", "maria@test.com");
        manager.add(c1);

        // 1. Επιτυχής διαγραφή
        boolean removed = manager.remove(c1);
        assertTrue(removed);
        assertEquals(0, manager.getSize());

        // 2. Διαγραφή ανύπαρκτου (προσπαθούμε να διαγράψουμε τον ίδιο ξανά)
        boolean removedAgain = manager.remove(c1);
        assertFalse(removedAgain);
    }

    @Test
    void testFindByAFM() {
        Client c1 = new Client("Eleni", "Nikou", "555555555", "2100000000", "eleni@test.com");
        manager.add(c1);

        // Εύρεση υπάρχοντος
        Client found = manager.findByAFM("555555555");
        assertNotNull(found);
        assertEquals("Eleni", found.getName());

        // Εύρεση ανύπαρκτου
        Client notFound = manager.findByAFM("000000000");
        assertNull(notFound);
    }

    @Test
    void testSearch() {
        // Προσθήκη 3 πελατών
        Client c1 = new Client("Nikos", "Georgiou", "100", "6901", "n@test.com");
        Client c2 = new Client("Nikos", "Papas", "200", "6902", "p@test.com"); // Ίδιο όνομα με c1
        Client c3 = new Client("Maria", "Georgiou", "300", "6903", "m@test.com");
    }
}