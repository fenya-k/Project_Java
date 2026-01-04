package API;

import java.io.*;
import java.util.ArrayList;

/**
 * This is the manager of all clients in the rental system.
 * Implementing the interface {@link Manager}.
 * Handles validation, adding, editing, removing, searching and file reading/writing.
 */
public class ClientManager implements Manager<Client> {

    /**
     * The ArrayList of all the clients in the system.
     */
    private final ArrayList<Client> clients;

    /**
     * Constructor for a new manager. Initializes the ArrayList.
     */
    public ClientManager() {
        clients = new ArrayList<>();
    }

    /**
     * Validation method to check if all the necessary fields have been given before add/edit.
     * Checks for null/empty values.
     * It is used by the {@link ManagementService}.
     *
     * @param name    Client's name
     * @param surname Client's surname
     * @param AFM     Client's AFM
     * @param phone   Client's phone number
     * @param email   Client's email address
     * @return "Επιτυχής καταχώρηση." if valid, otherwise an error message
     */
    public String isValidClient(String name, String surname, String AFM, String phone, String email) {
        String fullString = "";

        if (name == null || name.isEmpty()) {
            fullString += "Παρακαλώ καταχωρήστε το όνομα.\n"; // Fixed "τον" to "το"
        }
        if (surname == null || surname.isEmpty()) {
            fullString += "Παρακαλώ καταχωρήστε το επίθετο.\n";
        }
        if (AFM == null || AFM.isEmpty()) {
            fullString += "Παρακαλώ καταχωρήστε το ΑΦΜ.\n";
        }
        if (phone == null || phone.isEmpty()) {
            fullString += "Παρακαλώ καταχωρήστε το τηλέφωνο.\n";
        }
        if (email == null || email.isEmpty()) {
            fullString += "Παρακαλώ καταχωρήστε το email.\n";
        }

        if (fullString.isEmpty()) {
            return "Επιτυχής καταχώρηση.";
        } else {
            return fullString;
        }
    }

    /**
     * Adds a new client to the system after checking for duplicate afm.
     *
     * @param client The client to be added
     * @return true if added successfully, false if a client with the same afm exists
     */
    @Override
    public boolean add(Client client) {
        for (Client c : clients) {
            if (c.getAFM().equals(client.getAFM())) {
                System.out.println("There is already a client with afm: " + client.getAFM());
                return false;
            }
        }
        clients.add(client);
        System.out.println("The client has been added.");
        return true;
    }

    /**
     * Edits the details of an existing client.
     * The afm is used to find the client and cannot be changed.
     *
     * @param name    New name
     * @param surname New surname
     * @param AFM     The AFM
     * @param phone   New phone number
     * @param email   New email
     * @return true if found and edited, false otherwise
     */
    public boolean edit(String name, String surname, String AFM, String phone, String email) {
        Client editedClient = findByAFM(AFM);
        // Safety Check
        if (editedClient != null) {
            editedClient.setName(name);
            editedClient.setSurname(surname);
            editedClient.setPhone(phone);
            editedClient.setEmail(email);
            return true;
        }
        return false;
    }

    /**
     * Removes a client from the system.
     *
     * @param client The client to remove
     * @return true if removed, false if not found
     */
    @Override
    public boolean remove(Client client) {
        int clientsSize = clients.size();
        // We store the size to avoid recalculating it in the loop.
        // Since we return immediately after removal, standard iteration is safe.
        for (int i = 0; i < clientsSize; i++) {
            if (clients.get(i).getAFM().equals(client.getAFM())) {
                clients.remove(i);
                System.out.println("The client has been removed.");
                return true;
            }
        }
        System.out.println("Cannot find a client with afm: " + client.getAFM());
        return false;
    }

    /**
     * Retrieves the list of all the clients.
     *
     * @return An ArrayList of the clients
     */
    @Override
    public ArrayList<Client> getList() {
        return clients;
    }

    /**
     * Retrieves the number of clients in the list.
     *
     * @return The size of the list
     */
    @Override
    public int getSize() {
        return clients.size();
    }

    /**
     * Prints the details of all clients in the list.
     * Used for debugging
     */
    @Override
    public void print() {
        if (clients.isEmpty()) {
            System.out.println("No clients found");
        } else {
            for (Client client : clients) {
                System.out.println(client.toString());
            }
        }
    }

    /**
     * Searches for clients matching the provided parameters.
     * If a parameter is null, is ignored.
     *
     * @param name    Filter by name
     * @param surname Filter by surname
     * @param AFM     Filter by AFM
     * @param phone   Filter by phone
     * @return A list of clients matching all non-null criteria
     */
    public ArrayList<Client> search(String name, String surname, String AFM, String phone) {

        ArrayList<Client> foundClients = new ArrayList<>();
        /* Logic explanation:
         * Iterate through the clients with for loop. For each client, we check a series of filters (if statements).
         * 1. If a search parameter is NOT provided (null/empty), skip the check and move to the next 'if'.
         * 2. If a search parameter IS provided, compare it with the client's value.
         * - If they match: Proceed to the next filter check.
         * - If they do NOT match: Enter the 'if' body, execute 'continue', and skip to the next client immediately.
         */

        for (Client client : clients) {
            if (name != null && !name.isEmpty()) {
                if (!client.getName().equalsIgnoreCase(name)) {
                    continue;
                }
            }
            if (surname != null && !surname.isEmpty()) {
                if (!client.getSurname().equalsIgnoreCase(surname)) {
                    continue;
                }
            }
            if (AFM != null && !AFM.isEmpty()) {
                if (!client.getAFM().equalsIgnoreCase(AFM)) {
                    continue;
                }
            }
            if (phone != null && !phone.isEmpty()) {
                if (!client.getPhone().equalsIgnoreCase(phone)) {
                    continue;
                }
            }
            foundClients.add(client);
        }
        return foundClients;
    }

    /**
     * Finds a client using its afm.
     *
     * @param AFM The afm to search for
     * @return The Client object if found, null otherwise
     */
    public Client findByAFM(String AFM) {
        for (Client c : clients) {
            if (c.getAFM().equalsIgnoreCase(AFM)) return c;
        }
        return null;
    }

    // --- READ - WRITE ---

    /**
     * Reads clients from a CSV file and populates the list.
     *
     * @param filename The path to the CSV file
     */
    public void readCSV(String filename) {
        String line;
        String delimiter = ",";

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); //skips the first line because it's a header

            while ((line = in.readLine()) != null) {
                Client client = getClient(line, delimiter);
                if (client != null) {
                    clients.add(client);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.err.println("Error: File not read!");
        }
    }

    /**
     * Helper method to parse a client from a CSV line.
     *
     * @param line      The CSV line string
     * @param delimiter The delimiter used in the CSV
     * @return A new Client object, or null if data is incomplete
     */
    private static Client getClient(String line, String delimiter) {
        String[] data = line.split(delimiter);

        // Safety check
        if (data.length < 5) return null;

        String name = data[0].trim();
        String surname = data[1].trim();
        String AFM = data[2].trim();
        String phone = data[3].trim();
        String email = data[4].trim();

        return new Client(name, surname, AFM, phone, email);
    }

    /**
     * Writes the list of clients to a CSV file.
     * Overwrites the file.
     *
     * @param filename The path to the CSV file
     */
    public void writeCSV(String filename) {
        String line;

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            String header = "name,surname,AFM,phone,email";
            out.write(header);
            out.newLine();

            for (Client client : clients) {

                line = client.getName() + "," +
                        client.getSurname() + "," +
                        client.getAFM() + "," +
                        client.getPhone() + "," +
                        client.getEmail();

                out.write(line);
                out.newLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.err.println("Error: File not read!");
        }
    }
}