package API;


import java.io.*;
import java.util.ArrayList;

public class ClientManager {
    private String filename="clients.csv";
    private ArrayList<Client> clients;

    public ClientManager() {
        clients = new ArrayList<>();
        readCSV();
    }

    public void addClient(Client client) {
        for (Client c: clients){
            if(client.getAFM().equals(client.getAFM())){
                System.out.println("Client with AFM " +client.getAFM()+" already exists");
            return;
            }
        }
        clients.add(client);
        writeCSV();
        System.out.println("Client added successfully");
    }



    public ArrayList<Client> getClients() {
        return clients;
    }

    public int getSize() {
        return clients.size();
    }

    public void printClients() {
        for (Client client: clients) {
            System.out.println(client);
        }
    }

    public void readCSV() {
        String line;
        String delimiter = ",";
        clients.clear();;

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); //skips the first line

            while ((line = in.readLine()) != null) {
                String[] data = line.split(delimiter);
                if(data.length>=5){
                    String afm = data[0].trim();
                    String name = data[1].trim();
                    String surname = data[2].trim();
                    String phone = data[3].trim();
                    String email = data[4].trim();

                    Client client = new Client(afm, name, surname, phone, email);
                    clients.add(client);
                }

            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.out.println("Error: File not read!");
        }
    }

    public void writeCSV() {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            String header="AFM,name,surname,phone,email";
            out.write(header);
            out.newLine();

            for (Client client: clients){
               String line = client.getAFM() + "," +
                        client.getName() + "," +
                        client.getSurname() + "," +
                        client.getPhone() + "," +
                        client.getEmail();
                out.write(line);
                out.newLine();
            }
        }  catch (IOException e) {
            System.out.println("Error: writing file!");
        }
    }

}

