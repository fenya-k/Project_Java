package API;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Car implements History{
    private static int counter = 1;

    private final int id;
    private String plate;
    private String brand;
    private String type;
    private String model;
    private String year;
    private String color;
    private CarStatus carStatus;
    ArrayList<Rental> carRentals;

    public Car(String plate, String brand, String type, String model, String year, String color) {
        this.id = counter++;
        this.plate = plate;
        this.brand = brand;
        this.type = type;
        this.model = model;
        this.year = year;
        this.color = color;
        carStatus = CarStatus.AVAILABLE;
        carRentals = new ArrayList<>();
    }

    public Car(int id, String plate, String brand, String type, String model, String year, String color, CarStatus carStatus) {
        this.id = id;
        this.plate = plate;
        this.brand = brand;
        this.type = type;
        this.model = model;
        this.year = year;
        this.color = color;
        this.carStatus = carStatus;
        carRentals = new ArrayList<>();

        if (id >= counter) {
            counter = id + 1;
        }
    }

    public int getId() {
        return id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CarStatus getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(CarStatus carStatus) {
        this.carStatus = carStatus;
    }

    public boolean isAvailable() {
        return this.carStatus == CarStatus.AVAILABLE;
    }

    @Override
    public boolean addRentalTo(Rental rental){
        if(!carRentals.contains(rental)){
            carRentals.add(rental);
            return true;
        }
        return false;
    }

    @Override
    public void printRentals() {
        for(Rental rental : carRentals){
            System.out.println(rental);
        }
    }

    @Override
    public void writeCSVofRentals() {
        String filename = "DataBase/HistoryCars/carRentals.csv";

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            String header = "κωδικός ενοικίασης,πινακίδα,ΑΦΜ πελάτη,ημέρα έναρξης,ημέρα λήξης,όνομα εργαζόμενου";
            out.write(header);
            out.newLine();

            for (Rental rental : carRentals) {
                String line = rental.getRentCode() + "," +
                        rental.getRentCar().getPlate() + "," +
                        rental.getClient().getAFM() + "," +
                        rental.getStartDate() + "," +
                        rental.getEndDate() + "," +
                        rental.getEmployee().getName();

                out.write(line);
                out.newLine();
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.out.println("Error: File not read!");
        }
    }

    @Override
    public void readCSVofRentals() {

    }

    @Override
    public String toString() {
        return "Car: " +
                "id=" + id +
                ", plate='" + plate + '\'' +
                ", brand='" + brand + '\'' +
                ", type='" + type + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", color='" + color + '\'' +
                ", car status=" + carStatus;
    }
}
