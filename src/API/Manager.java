package API;

import java.util.ArrayList;

/**
 * A generic interface for all managers, providing the necessary methods.
 *
 * @param <T> The type of entity
 */
public interface Manager<T> {
    /**
     * Adds an entity to the list, after validating it doesn't exist already.
     *
     * @param object The entity to be added
     * @return true if successful, false otherwise
     */
    boolean add(T object);

    /**
     * Removes an entity from the list.
     *
     * @param object The entity to be removed
     * @return true if successful, false otherwise
     */
    boolean remove(T object);

    /**
     * Retrieves the list of all the entities.
     *
     * @return An ArrayList of the entities
     */
    ArrayList<T> getList();

    /**
     * Retrieves the number of entities in the list.
     *
     * @return The size of the list
     */
    int getSize();

    /**
     * Prints the details of all entities in the list.
     * Used for debugging
     */
    void print();
}