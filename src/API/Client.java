package API;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Client extends Person implements History {
    private String AFM;
    private String phone;
    ArrayList<Rental> clientRentals;

    public Client(String name, String surname, String AFM, String phone, String email) {
        super(name, surname, email);
        this.AFM = AFM;
        this.phone = phone;
        clientRentals = new ArrayList<>();
    }

    public String getAFM() {
        return AFM;
    }

    public void setAFM(String AFM) {
        this.AFM = AFM;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean addRentalTo(Rental rental) {
        if (!clientRentals.contains(rental)) {
            clientRentals.add(rental);
            return true;
        }
        return false;
    }

    @Override
    public void printRentals() {
        for (Rental rental : clientRentals) {
            System.out.println(rental);
        }
    }

    @Override
    public void writeCSVofRentals() {
            String filename = "clientRentals.csv";

            try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

                String header = "κωδικός ενοικίασης,πινακίδα,ΑΦΜ πελάτη,ημέρα έναρξης,ημέρα λήξης,όνομα εργαζόμενου";
                out.write(header);
                out.newLine();

                for (Rental rental : clientRentals) {
                    String line = rental.getRentCode() + "," +
                            rental.getRentCar().getPlate() + "," +
                            rental.getClient().getAFM() + "," +
                            rental.getStartDate() + "," +
                            rental.getEndDate() + "," +
                            rental.getEmployee().getName();

                    out.write(line);
                    out.newLine();
                }

            } catch (FileNotFoundException e) {
                System.err.println("Error: File not found!");
            } catch (IOException e) {
                System.out.println("Error: File not read!");
            }
        }

    @Override
    public void readCSVofRentals() {

    }

    @Override
    public String toString() {
        return "Client: " +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", AFM='" + AFM + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'';
    }

}
