package API;

import java.io.*;
import java.security.CodeSigner;
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalManager implements Manager<Rental>, ReadWriteCSV {

    private final ArrayList<Rental> rentals;

    public RentalManager() {
        rentals = new ArrayList<>();
    }

    public String isValidRental(int code, Car rentCar, Client client, LocalDate startDate, LocalDate endDate, Employee employee) {
        String fullString = "";

        if (code <= 0) {
            fullString += "Παρακαλώ καταχωρήστε τον κωδικό.\n";
        }
        if (rentCar == null) {
            fullString += "Παρακαλώ καταχωρήστε το όχημα.\n";
        }
        if (client == null) {
            fullString += "Παρακαλώ καταχωρήστε τον πελάτη.\n";
        }
        if (employee == null) {
            fullString += "Παρακαλώ καταχωρήστε τον υπάλληλο.\n";
        }
        if (startDate == null) {
            fullString += "Παρακαλώ καταχωρήστε ημερομηνία παραλαβής.\n";
        }
        if (endDate == null) {
            fullString += "Παρακαλώ καταχωρήστε ημερομηνία επιστροφής.\n";
        }
        if (fullString.isEmpty()) {
            return "Επιτυχής καταχώρηση.";
        } else {
            return fullString;
        }
    }

    @Override
    public boolean add(Rental rental) {
        for (Rental r : rentals) {
            if (r.getRentCode() == rental.getRentCode()) {
                System.out.println("Υπάρχει ήδη ενοικίαση με κωδικό " + rental.getRentCode());
                return false;
            }
        }
        if (!rental.getRentCar().isAvailable()) {
            System.out.println(rental.getRentCar().toString() + " is not available");
            return false;
        }
        rentals.add(rental);
        rental.getRentCar().setCarStatus(CarStatus.RENTED);
        System.out.println("Η ενοικίαση έγινε");
        return true;
    }

    public boolean edit(int code, Car rentCar, Client client, LocalDate startDate, LocalDate endDate, Employee employee) {
        Rental editedRental = findByCode(code);
        editedRental.setRentCar(rentCar);
        editedRental.setClient(client);
        editedRental.setStartDate(startDate);
        editedRental.setEndDate(endDate);
        editedRental.setEmployee(employee);
        return true;
    }

    @Override
    public boolean remove(Rental rental) {
        int rentalsSize = rentals.size();
        //η μεταβλητή rentalsSize χρησιμοποιείται για να μην υπολογίζεται επανειλημμένα στη for (int i = 0; i < rentals.size(); i++)
        //από τη στιγμή που γίνεται return μετά τη διαγραφή δε θα εμφανίσει IndexOutOfBoundsException
        //σε περίπτωση που θέλαμε να διαγράψουμε πολλαπλές ενοικιάσεις με τον ίδιο κωδικό
        for (int i = 0; i < rentalsSize; i++) { //χρήση αυτής της for για αποθήκευση της θέσης στο i
            if (rentals.get(i).getRentCode() == rental.getRentCode()) {
                rentals.get(i).getRentCar().setCarStatus(CarStatus.AVAILABLE);
                rentals.remove(i);
                System.out.println("Η ενοικίαση διαγράφηκε");
                return true;
            }
        }
        System.out.println("Δεν βρέθηκε κάποια ενοικίαση με τον κωδικό " + rental.getRentCode());
        return false;
    }

    @Override
    public ArrayList<Rental> getList() {
        return rentals; //encapsulation - defensive copying
    }

    @Override
    public int getSize() {
        return rentals.size();
    }

    @Override
    public void print() {
        if (rentals.isEmpty()) {
            System.out.println("No rentals found");
        } else {
            for (Rental rental : rentals) {
                System.out.println(rental.toString());
            }
        }
    }

    public ArrayList<Rental> search (int code, String plate, String AFM, String username) {

        ArrayList<Rental> foundRentals = new ArrayList<>();

        for (Rental rental : rentals) {
            if (username != null && !username.isEmpty() && !rental.getEmployee().getUsername().equalsIgnoreCase(username)) {
                continue;
            }
            if (plate != null && !plate.isEmpty() && !rental.getRentCar().getPlate().equalsIgnoreCase(plate)) {
                continue;
            }
            if (AFM != null && !AFM.isEmpty() && !rental.getClient().getAFM().equalsIgnoreCase(AFM)) {
                continue;
            }
            if (code >0 && rental.getRentCode()!=code) {
                continue;
            }
            foundRentals.add(rental);
        }
        return foundRentals;
    }

    public Rental findByCode(int code) {
        for (Rental r : rentals) {
            if (r.getRentCode() == code) return r;
        }
        return null;
    }

    public ArrayList<Rental> searchByClientAFM(String AFM) {
        ArrayList<Rental> found = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getClient().getAFM().equals(AFM)) {
                found.add(rental);
            }
        }
        return found;
    }
}