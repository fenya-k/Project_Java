package API;

import java.io.*;
import java.time.LocalDate;
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


    // να υλοποιηθεί

//    public ArrayList<Rental> search(String plate, String brand, String type, String model, String color, Boolean available) {
//        ArrayList<Car> foundCars = new ArrayList<>();
//
//        /* Τα αυτοκίνητα μπαίνουν στη for με τη σειρά, έπειτα γίνεται έλεγχος σε κάθε if αν έχει δοθεί τιμή για σύγκριση
//         αν δεν έχει δοθεί τιμή (null) τότε προχωράει στην επόμενη if χωρίς να μπει στο σώμα της
//         αν έχει δοθεί τιμή τότε τη συγκρίνει με αυτή του εκάστοτε αυτοκινήτου και
//         -αν είναι ίση τότε προχωράει στην επόμενη if χωρίς να μπει στο σώμα της
//         -αν δεν είναι ίση τότε μπαίνει στο σώμα της, κάνει continue και έρχεται το επόμενο αυτοκίνητο */
//
//        for (Car car : cars) { //null proofing (protection from NullPointerExceptions)
//            if (plate != null && !plate.isEmpty() && !car.getPlate().equalsIgnoreCase(plate)) {
//                continue;
//            } //η πινακίδα έρχεται πρώτη ώστε αν είναι διαφορετική δε χρειάζονται οι υπόλοιποι έλεγχοι
//            if (brand != null && !brand.isEmpty() && !car.getBrand().equalsIgnoreCase(brand)) {
//                continue;
//            }
//            if (type != null && !type.isEmpty() && !car.getType().equalsIgnoreCase(type)) {
//                continue;
//            }
//            if (model != null && !model.isEmpty() && !car.getModel().equalsIgnoreCase(model)) {
//                continue;
//            }
//            if (color != null && !color.isEmpty() && !car.getColor().equalsIgnoreCase(color)) {
//                continue;
//            }
//            if (available != null && car.isAvailable() != available) {
//                continue;
//            }
//            foundCars.add(car);
//        }
//        return foundCars;
//    }

}
