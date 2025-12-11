package API;

import java.io.*;
import java.util.ArrayList;

public class ClientManager implements Manager<Client> {
    private final ArrayList<Client> clients;

    public ClientManager() {
        clients = new ArrayList<>();
    }

    @Override
    public boolean add(Client client) {
        for (Client c : clients) {
            if (c.getAFM().equals(client.getAFM())) {
                System.out.println("Υπάρχει ήδη πελάτης με το ΑΦΜ " + client.getAFM());
                return false;
            }
        }
        clients.add(client);
        System.out.println("Ο πελάτης προστέθηκε");
        return true;
    }

    @Override
    public boolean remove(Client client) {
        int clientsSize = clients.size();
        //η μεταβλητή clientsSize χρησιμοποιείται για να μην υπολογίζεται επανειλημμένα στη for (int i = 0; i < clients.size(); i++)
        //από τη στιγμή που γίνεται return μετά τη διαγραφή δε θα εμφανίσει IndexOutOfBoundsException
        //σε περίπτωση που θέλαμε να διαγράψουμε πολλαπλούς πελάτες με το ίδιο ΑΦΜ θα ήταν λάθος
        for (int i = 0; i < clientsSize; i++) { //χρήση αυτής της for για αποθήκευση της θέσης στο i
            if (clients.get(i).getAFM().equals(client.getAFM())) {
                clients.remove(i);
                System.out.println("Ο πελάτης διαγράφηκε");
                return true;
            }
        }
        System.out.println("Δεν βρέθηκε κάποιος πελάτης με το ΑΦΜ " + client.getAFM());
        return false;
    }

    @Override
    public ArrayList<Client> getList() {
        ArrayList<Client> temp = this.clients; //encapsulation - defensive copying
        return temp;
    }

    @Override
    public int getSize() {
        return clients.size();
    }

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

    public void readCSV(String filename) {
        String line;
        String delimiter = ",";

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); //skips the first line because it's a header

            while ((line = in.readLine()) != null) {
                String[] data = line.split(delimiter);

                String name = data[0].trim();
                String surname = data[1].trim();
                String AFM = data[2].trim();
                String phone = data[3].trim();
                String email = data[4].trim();

                Client client = new Client(name, surname, AFM, phone, email);
                clients.add(client);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.out.println("Error: File not read!");
        }
    }

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
            System.out.println("Error: File not read!");
        }
    }


    public Client search(String name, String surname, String AFM, String phone, String email) {

        /* Οι πελάτες μπαίνουν στη for με τη σειρά, έπειτα γίνεται έλεγχος σε κάθε if αν έχει δοθεί τιμή για σύγκριση
         αν δεν έχει δοθεί τιμή (null) τότε προχωράει στην επόμενη if χωρίς να μπει στο σώμα της
         αν έχει δοθεί τιμή τότε τη συγκρίνει με αυτή του εκάστοτε αυτοκινήτου και
         -αν είναι ίση τότε προχωράει στην επόμενη if χωρίς να μπει στο σώμα της
         -αν δεν είναι ίση τότε μπαίνει στο σώμα της, κάνει continue και έρχεται ο επόμενος πελάτης */

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
            if (email != null && !email.isEmpty()) {
                if (!client.getEmail().equalsIgnoreCase(email)) {
                    continue;
                }
            }
            return client;
        }
        return null;
    }

    public Client findByAFM(String AFM) {
        for (Client c : clients) {
            if (c.getAFM().equalsIgnoreCase(AFM)) return c;
        }
        return null;
    }
}
