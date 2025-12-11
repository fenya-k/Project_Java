package API;

import java.util.ArrayList;

public interface History {

    boolean addRental(Rental rental);

    void printRentals();

    ArrayList<Rental> returnList();

}