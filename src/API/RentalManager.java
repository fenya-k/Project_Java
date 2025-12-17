package API;

import java.io.*;
import java.util.ArrayList;

public class RentalManager implements Manager<Rental>, ReadWriteCSV {

    private final ArrayList<Rental> rentals;

    public RentalManager() {
        rentals = new ArrayList<>();
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

    public ArrayList<Rental> searchByClientAFM(String AFM) {
        ArrayList<Rental> found = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getClient().getAFM().equals(AFM)) {
                found.add(rental);
            }
        }
        return found;
    }

    @Override
    public ArrayList<Rental> getList() {
        return new ArrayList<>(this.rentals); //encapsulation - defensive copying
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

    public ArrayList<Rental> search(String plate, String AFM) {
        ArrayList<Rental> foundRentals = new ArrayList<>();

        for (Rental rental : rentals) { //null proofing (protection from NullPointerExceptions)
            if (plate != null && !plate.isEmpty() && !rental.getRentCar().getPlate().equalsIgnoreCase(plate)) {
                continue;
            }
            if (AFM != null && !AFM.isEmpty() && !rental.getClient().getAFM().equalsIgnoreCase(AFM)) {
                continue;
            }
            foundRentals.add(rental);
        }
        return foundRentals;
    }

}
