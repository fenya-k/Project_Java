package API;

import java.util.ArrayList;

/**
 * This interface has to be used by classes that need a rental history like Car and Client
 * Store and retrieve history are the necessary methods to be implemented
 */
public interface History {

    /**
     * Adds a new rental to the history
     * after checking if it already exists
     * @param rental The rental to be added
     * @return true if successful, false otherwise
     */
    boolean addRental(Rental rental);

    /**
     * Retrieves the history list
     * @return An ArrayList of the rental history
     */
    ArrayList<Rental> returnList();

}