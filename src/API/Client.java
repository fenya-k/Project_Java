package API;

import java.util.ArrayList;

public class Client extends Person implements History, ReadWriteCSV {
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
    public boolean addRental(Rental rental) {
        for (Rental r : clientRentals) {
            if (r.getRentCode() == rental.getRentCode()) {
                System.out.println("Υπάρχει ήδη ενοικίαση με κωδικό " + rental.getRentCode());
                return false;
            }
        }
        clientRentals.add(rental);
        return true;
    }

    @Override
    public void printRentals() {
        if (clientRentals.isEmpty()) {
            System.out.println("No rentals found");
        } else {
            for (Rental rental : clientRentals) {
                System.out.println(rental.toString());
            }
        }
    }

    @Override
    public ArrayList<Rental> returnList() {
        return new ArrayList<>(this.clientRentals); //encapsulation - defensive copying
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
