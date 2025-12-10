package API;

import java.time.LocalDate;

public class Rental {
    private static int counter = 1;

    private int rentCode;
    private Car rentCar;
    private Client client;
    private LocalDate startDate;
    private LocalDate endDate;
    private Employee employee;

    public Rental(Car rentCar, Client client, LocalDate startDate, LocalDate endDate, Employee employee) {
        this.rentCode = counter++;
        this.rentCar = rentCar;
        this.client = client;
        this.startDate = startDate;
        this.endDate = endDate;
        this.employee = employee;
        // this.rentCar.setCarStatus(CarStatus.RENTED); //Status update moved to RentalManager.
    }

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

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Rental.counter = counter;
    }

    public int getRentCode() {
        return rentCode;
    }

    public void setRentCode(int rentCode) {
        this.rentCode = rentCode;
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

    @Override
    public String toString() {
        return "Rental: " +
                "rentCode=" + rentCode +
                ", rentCar=" + rentCar.getPlate() +
                ", client=" + client.getName() + client.getSurname() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", employee=" + employee.getUsername();
    }
}
