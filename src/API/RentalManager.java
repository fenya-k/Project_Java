package API;

import java.util.ArrayList;

public class RentalManager {
    private ArrayList<Rental> rentals;

    public RentalManager() {
        rentals = new ArrayList<>();
    }

    public void createRental(Rental rental) {
        if (rental.getRentCar().isAvailable()) {
            rentals.add(rental);
           rental.getRentCar().setCarStatus(CarStatus.RENTED);
            System.out.println("Rental created successfully: " + rental.getRentCode());
        } else {
            System.out.println("Error: Car is not available for rent.");
        }
    }

    public ArrayList<Rental> getRentals() {
        return rentals;
    }

    public void printRentals() {
        for (Rental rental : rentals) {
            System.out.println(rental);
        }
    }
}