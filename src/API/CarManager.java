package API;//package api;

import java.io.*;
import java.util.ArrayList;

public class CarManager {
    private ArrayList<Car> cars;

    public CarManager() {
        cars = new ArrayList<>();
    }

    public boolean addCar(Car car) {
        for(Car c : cars){
            if (c.getPlate().equals(car.getPlate())) {
                System.out.println("Υπάρχει ήδη αυτοκίνητο με την πινακίδα " + car.getPlate());
                return false;
            }
        }
        cars.add(car);
        System.out.println("Το αυτοκίνητο προστέθηκε");
        return true;
    }

    public boolean removeCar(Car car) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getPlate().equals(car.getPlate())) {
                cars.remove(i);
                System.out.println("Το αυτοκίνητο διαγράφηκε");
                return true;
            }
        }
        System.out.println("Δεν βρέθηκε κάποιο αυτοκίνητο με την πινακίδα " + car.getPlate());
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
            in.readLine(); //skips the first line because it's a header

            while ((line = in.readLine()) != null) {
                String[] data = line.split(delimiter);

                int id = Integer.parseInt(data[0].trim());
                String plate = data[1].trim();
                String brand = data[2].trim();
                String type = data[3].trim();
                String model = data[4].trim();
                String year = data[5].trim();
                String color = data[6].trim();
                String statusGreek = data[7].trim();

                CarStatus status;
                if (statusGreek.equalsIgnoreCase("Διαθέσιμο")) {
                    status = CarStatus.AVAILABLE;
                } else {
                    status = CarStatus.RENTED;
                }

                Car car = new Car(id, plate, brand, type, model, year, color, status);
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
            out.newLine();

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
                if (!car.getPlate().equalsIgnoreCase(plate)) {
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
                if (!car.getModel().equalsIgnoreCase(model)) {
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
