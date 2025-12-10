package API;

import java.io.*;
import java.util.ArrayList;

public class CarManager implements Manager<Car> {
    private final ArrayList<Car> cars;

    public CarManager() {
        cars = new ArrayList<>();
        readCSV();
    }

    @Override
    public boolean add(Car car) {
        for (Car c : cars) {
            if (c.getPlate().equalsIgnoreCase(car.getPlate())) {
                System.out.println("Υπάρχει ήδη αυτοκίνητο με την πινακίδα " + car.getPlate());
                return false;
            }
        }
        cars.add(car);
        System.out.println("Το αυτοκίνητο προστέθηκε");
        return true;
    }

    @Override
    public boolean remove(Car car) {
        int carsSize = cars.size();
        //η μεταβλητή carsSize χρησιμοποιείται για να μην υπολογίζεται επανειλημμένα στη for (int i = 0; i < cars.size(); i++)
        //από τη στιγμή που γίνεται return μετά τη διαγραφή δε θα εμφανίσει IndexOutOfBoundsException
        //σε περίπτωση που θέλαμε να διαγράψουμε πολλαπλά αυτοκίνητα με την ίδια πινακίδα θα ήταν λάθος
        for (int i = 0; i < carsSize; i++) { //χρήση αυτής της for για αποθήκευση της θέσης στο i
            if (cars.get(i).getPlate().equalsIgnoreCase(car.getPlate())) {
                cars.remove(i);
                System.out.println("Το αυτοκίνητο διαγράφηκε");
                return true;
            }
        }
        System.out.println("Δεν βρέθηκε κάποιο αυτοκίνητο με την πινακίδα " + car.getPlate());
        return false;
    }

    @Override
    public ArrayList<Car> getAll() {
        ArrayList<Car> temp = this.cars; //encapsulation - defensive copying
        return temp;
    }

    @Override
    public int getSize() {
        return cars.size();
    }

    @Override
    public void print() {
        for (Car car : cars) {
            System.out.println(car.toString());
        }
    }

    @Override
    public void readCSV() {
        String filename = "DataBase/ManagerFiles/vehicles.csv";
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

    @Override
    public void writeCSV() {
        String filename = "DataBase/ManagerFiles/vehicles.csv";

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            String header = "id,πινακίδα,μάρκα,τύπος,μοντέλο,έτος,χρώμα,κατάσταση";
            out.write(header);
            out.newLine();

            for (Car car : cars) {
                String availability = car.isAvailable() ? "Διαθέσιμο" : "Ενοικιασμένο";

                String line = car.getId() + "," +
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


    public ArrayList<Car> search(String plate, String brand, String type, String model, String color, Boolean available) {
        ArrayList<Car> foundCars = new ArrayList<>();

        /* Τα αυτοκίνητα μπαίνουν στη for με τη σειρά, έπειτα γίνεται έλεγχος σε κάθε if αν έχει δοθεί τιμή για σύγκριση
         αν δεν έχει δοθεί τιμή (null) τότε προχωράει στην επόμενη if χωρίς να μπει στο σώμα της
         αν έχει δοθεί τιμή τότε τη συγκρίνει με αυτή του εκάστοτε αυτοκινήτου και
         -αν είναι ίση τότε προχωράει στην επόμενη if χωρίς να μπει στο σώμα της
         -αν δεν είναι ίση τότε μπαίνει στο σώμα της, κάνει continue και έρχεται το επόμενο αυτοκίνητο */

        for (Car car : cars) { //null proofing (protection from NullPointerExceptions)
            if (plate != null && !plate.isEmpty() && !car.getPlate().equalsIgnoreCase(plate)) {
                continue;
            } //η πινακίδα έρχεται πρώτη ώστε αν είναι διαφορετική δε χρειάζονται οι υπόλοιποι έλεγχοι
            if (brand != null && !brand.isEmpty() && !car.getBrand().equalsIgnoreCase(brand)) {
                continue;
            }
            if (type != null && !type.isEmpty() && !car.getType().equalsIgnoreCase(type)) {
                continue;
            }
            if (model != null && !model.isEmpty() && !car.getModel().equalsIgnoreCase(model)) {
                continue;
            }
            if (color != null && !color.isEmpty() && !car.getColor().equalsIgnoreCase(color)) {
                continue;
            }
            if (available != null && car.isAvailable() != available) {
                continue;
            }
            foundCars.add(car);
        }
        return foundCars;
    }

    public Car findByPlate(String plate) {
        for (Car c : cars) {
            if (c.getPlate().equalsIgnoreCase(plate)) return c;
        }
        return null;
    }
}
