package API;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Car implements History, ReadWriteCSV {
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
    public boolean addRental(Rental rental) {
        for (Rental r : carRentals) {
            if (r.getRentCode() == rental.getRentCode()) {
                System.out.println("Υπάρχει ήδη ενοικίαση με κωδικό " + rental.getRentCode());
                return false;
            }
        }
        carRentals.add(rental);
        return true;
    }

    @Override
    public void printRentals() {
        if (carRentals.isEmpty()) {
            System.out.println("No rentals found for: " + plate);
        } else {
            System.out.println("Rental history for: " + plate);
            for (Rental rental : carRentals) {
                System.out.println(rental.toString());
            }
        }
    }

    @Override
    public ArrayList<Rental> returnList() {
        return new ArrayList<>(this.carRentals); //encapsulation - defensive copying
    }

    @Override
    public  void readCSV(CarManager CM,ClientManager CLM,EmployeeManager EM,String filename, ArrayList<Rental> list) {
        String line;
        String delimiter = ",";

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); //skips the first line because it's a header

            while ((line = in.readLine()) != null) {
                String[] data = line.split(delimiter);

                if (data.length >= 6) {
                    String plateRecord = data[1].trim();

                    if (plateRecord.equalsIgnoreCase(this.plate)) {
                        try {
                            int rentCode = Integer.parseInt(data[0].trim());
                            String AFMclient = data[2].trim();
                            LocalDate start = LocalDate.parse(data[3].trim());
                            LocalDate end = LocalDate.parse(data[4].trim());
                            String userEmp = data[5].trim();

                            Client client = CLM.findByAFM(AFMclient);
                            Employee employee = EM.findByUsername(userEmp);

                            if (client != null && employee != null) {
                                Rental rental = new Rental(rentCode, this, client, start, end, employee);
                                this.addRental(rental);
                            }
                        } catch (Exception e) {
                            System.out.println("Error");
                        }
                    }
                }
            }
        }catch (IOException e){
            System.out.println("File not found");
        }
    }

    public void writeCSV(String filename,ArrayList<Rental> list) {
        String line;
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename, true))) {

            for (Rental rental : carRentals) {
                line = rental.getRentCode() + "," +
                        this.getPlate() + "," +
                        rental.getClient().getAFM() + "," +
                        rental.getStartDate() + "," +
                        rental.getEndDate() + "," +
                        rental.getEmployee().getUsername();

                out.write(line);
                out.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing car history");
        }
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
