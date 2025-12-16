package API;

import java.util.ArrayList;

public interface History {
    /**
     * Adds a rental record to the entity's personal history.
     * @param rental The rental to add.
     * @return true if added successfully, false if duplicate.
     */
    boolean addRental(Rental rental);

    void printRentals();

    ArrayList<Rental> returnList();

}