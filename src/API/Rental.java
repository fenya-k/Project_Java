package API;

import java.time.LocalDate;

/**
 * Represents a rental in the rental system.
 * Brings together a {@link Car}, a {@link Client}, an {@link Employee}
 * and the start date / end date.
 */
public class Rental {
    /**
     * Static counter that is updated automatically with every new rental
     */
    private static int counter = 1;
    /**
     * Unique code given by the counter for every rental
     */
    private final int rentCode;
    private Car rentCar;
    private Client client;
    private LocalDate startDate;
    private LocalDate endDate;
    private Employee employee;

    /**
     * Constructor for creating a NEW rental.
     * It is used by the RentalManager, ManagementService and does not update car status.
     * Automatically assigns a new code using the static counter.
     *
     * @param rentCar   The car to be rented.
     * @param client    The client to rent the car.
     * @param startDate The date the rental begins.
     * @param endDate   The date the rental ends.
     * @param employee  The employee processing the rental.
     */
    public Rental(Car rentCar, Client client, LocalDate startDate, LocalDate endDate, Employee employee) {
        this.rentCode = counter++;
        this.rentCar = rentCar;
        this.client = client;
        this.startDate = startDate;
        this.endDate = endDate;
        this.employee = employee;
    }

    /**
     * Constructor for loading a rental from the csv file.
     * It updates the static counter to ensure unique codes for future creations.
     *
     * @param rentCode  The existing code of the rental.
     * @param rentCar   The rented car.
     * @param client    The client.
     * @param startDate The start date.
     * @param endDate   The end date.
     * @param employee  The employee.
     */
    public Rental(int rentCode, Car rentCar, Client client, LocalDate startDate, LocalDate endDate, Employee employee) {
        this.rentCode = rentCode;
        this.rentCar = rentCar;
        this.client = client;
        this.startDate = startDate;
        this.endDate = endDate;
        this.employee = employee;

        if (rentCode >= counter) {
            counter = rentCode + 1;
        }
    }

    // --- GETTERS - SETTERS ---

    public int getRentCode() {
        return rentCode;
    }

    public Car getRentCar() {
        return rentCar;
    }

    public void setRentCar(Car rentCar) {
        this.rentCar = rentCar;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    // --- TO STRING ---

    @Override
    public String toString() {
        return "Rental: " +
                "rentCode=" + rentCode +
                ", rentCar=" + rentCar.getPlate() +
                ", client=" + client.getName() + " " + client.getSurname() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", employee=" + employee.getUsername();
    }
}