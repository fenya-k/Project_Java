package API;

public interface History {

    boolean addRental(Rental rental);

    void printRentals();

    void writeCSVofRentals();

    void readCSVofRentals();
}