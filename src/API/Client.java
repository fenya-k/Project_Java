package API;

import java.io.*;
import java.util.ArrayList;

/**
 * Represents a client with access to the rental system.
 * Manages the client's personal details.
 * Extends the {@link Person} abstract class.
 * Implements the {@link History} interface.
 */
public class Client extends Person implements History {
    private final String AFM;
    private String phone;
    ArrayList<Rental> clientRentals;

    /**
     * Creates a new client
     *
     * @param name    Client's first name
     * @param surname Client's last name
     * @param AFM     Client's afm
     * @param phone   Client's phone
     * @param email   Client's email
     */
    public Client(String name, String surname, String AFM, String phone, String email) {
        super(name, surname, email);
        this.AFM = AFM;
        this.phone = phone;
        clientRentals = new ArrayList<>();
    }

    // --- GETTERS - SETTERS ---

    public String getAFM() {
        return AFM;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // --- LOGIC METHODS ---

    /**
     * From interface History
     * Adds a rental record to the client's history.
     * Checks for duplicate rental codes before adding.
     *
     * @param rental The rental object to add.
     * @return true if added successfully, false if a rental with the same code exists.
     */
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

    /**
     * From interface History
     * Returns a defensive copy of the client's rental history.
     *
     * @return An ArrayList containing the rental records.
     */
    @Override
    public ArrayList<Rental> returnList() {
        return new ArrayList<>(this.clientRentals); //encapsulation - defensive copying
    }

    // --- TO STRING ---

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