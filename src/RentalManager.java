//package api;

import java.io.*;
import java.util.ArrayList;

public class RentalManager {
    private ArrayList<Car> cars;

    public RentalManager() {
        cars = new ArrayList<>();
    }

    public boolean addCar(Car car) {
        if (!cars.contains(car)) {
            cars.add(car);
            return true;
        }
        return false;
    }

    public boolean removeCar(Car car) {
        if (cars.contains(car)) {
            cars.remove(car);
            return true;
        }
        return false;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public int getSize() {
        return cars.size();
    }

    public void printCars() {
        for (Car car : cars) {
            System.out.println(car.toString());
        }
    }

    public void readCSV() {
        String filename = "vehicles_with_plates.csv";
        String line;
        String delimiter = ",";

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); //skips the first line

            while ((line = in.readLine()) != null) {
                String[] data = line.split(delimiter);

                int id = (int) data[0].trim();
                String plate = data[1].trim();
                String brand = data[2].trim();
                String type = data[3].trim();
                String model = data[4].trim();
                String year = data[5].trim();
                String color = data[6].trim();
                String available = data[7].trim();

                Car car = new Car(id, plate, brand, type, model, year, color, available);
                cars.add(car);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.out.println("Error: File not read!");
        }
    }

    public void writeCSV() {
        String filename = "vehicles_with_plates.csv";
        String line;

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            String header="id,πινακίδα,μάρκα,τύπος,μοντέλο,έτος,χρώμα,κατάσταση";
            out.write(header);

            for (Car car: cars){
                String availability = car.isAvailable() ? "Διαθέσιμο" : "Ενοικιασμένο";

                line = car.getId() + "," +
                        car.getPlate() + "," +
                        car.getBrand() + "," +
                        car.getType() + "," +
                        car.getModel() + "," +
                        car.getYear() + "," +
                        car.getColor() + "," +
                        availability;

                out.write(line);
                out.newLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.out.println("Error: File not read!");
        }
    }

    public ArrayList<Car> searchCar(String plate, String brand, String type, String model, String color, Boolean available) {
        ArrayList<Car> foundCars = new ArrayList<>();

        for (Car car : cars) {
            if (plate != null && !plate.isEmpty()) {
                if (!car.getPlate().toUpperCase().contains(plate.toUpperCase())) {
                    continue;
                }
            }
            if (brand != null && !brand.isEmpty()) {
                if (!car.getBrand().equalsIgnoreCase(brand)) {
                    continue;
                }
            }
            if (type != null && !type.isEmpty()) {
                if (!car.getType().equalsIgnoreCase(type)) {
                    continue;
                }
            }
            if (model != null && !model.isEmpty()) {
                if (!car.getModel().toUpperCase().contains(model.toUpperCase())) {
                    continue;
                }
            }
            if (color != null && !color.isEmpty()) {
                if (!car.getColor().equalsIgnoreCase(color)) {
                    continue;
                }
            }
            if (available != null) {
                if (car.isAvailable() != available) {
                    continue;
                }
            }
            foundCars.add(car);
        }
        return foundCars;
    }
}
