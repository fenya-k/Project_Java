import java.io.*;
import java.util.ArrayList;

public class ClientManager {
    private ArrayList<Client> clients;

    public ClientManager() {
        clients = new ArrayList<>();
    }

    public boolean addClient(Client client) {
        if (!clients.contains(client)) {
            clients.add(client);
            return true;
        }
        return false;
    }

    public boolean removeCar(Client client) {
        if (clients.contains(client)) {
            clients.remove(client);
            return true;
        }
        return false;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public int getSize() {
        return clients.size();
    }

    public void printClients() {
        for (Client client : clients) {
            System.out.println(client.toString());
        }
    }

    public void readCSV() {
        String filename = "clients.csv";
        String line;
        String delimiter = ",";

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); //skips the first line

            while ((line = in.readLine()) != null) {
                String[] data = line.split(delimiter);

                String name = data[0].trim();
                String AFM = data[1].trim();
                String phone = data[2].trim();
                String email = data[3].trim();

                Client client = new Client(name, AFM, phone, email);
                clients.add(client);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.out.println("Error: File not read!");
        }
    }

    public void writeCSV() {
        String filename = "clients.csv";
        String line;

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            String header="name,AFM,phone,email";
            out.write(header);

            for (Client client: clients){

                line = client.getName() + "," +
                        client.getAFM() + "," +
                        client.getPhone() + "," +
                        client.getEmail() ;

                out.write(line);
                out.newLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.out.println("Error: File not read!");
        }
    }


    public Client searchClient(String AFM, String name, String phone, String email) {
        Client clientFound = null;
        for (Client client : clients) {
            if (AFM != null && !AFM.isEmpty()) {
                if (!client.getAFM().equalsIgnoreCase(AFM)) {
                    continue;
                }
            }
            if (name != null && !name.isEmpty()) {
                if (!client.getName().equalsIgnoreCase(name)) {
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
            break;
        }
        return clientFound;
    }
}
