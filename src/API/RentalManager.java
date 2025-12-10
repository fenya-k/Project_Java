package API;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalManager implements Manager<Rental> {

    private final String filename = "DataBase/ManagerFiles/rentals.csv";
    private final ArrayList<Rental> rentals;

    public RentalManager() {
        rentals = new ArrayList<>();
    }

    @Override
    public boolean add(Rental rental) {
        for(Rental r : rentals){
            if(r.getRentCode() == rental.getRentCode()){
                System.out.println("Υπάρχει ήδη ενοικίαση με κωδικό " + rental.getRentCode());
                return false;
            }
        }
        if (!rental.getRentCar().isAvailable()) {
            System.out.println(rental.getRentCar().toString() + " is not available");
            return false;
        }
        rentals.add(rental);
        rental.getRentCar().setCarStatus(CarStatus.RENTED);
        System.out.println("Η ενοικίαση έγινε");
        return true;
    }

    @Override
    public boolean remove(Rental rental) {
        int rentalsSize = rentals.size();
        //η μεταβλητή rentalsSize χρησιμοποιείται για να μην υπολογίζεται επανειλημμένα στη for (int i = 0; i < rentals.size(); i++)
        //από τη στιγμή που γίνεται return μετά τη διαγραφή δε θα εμφανίσει IndexOutOfBoundsException
        //σε περίπτωση που θέλαμε να διαγράψουμε πολλαπλές ενοικιάσεις με τον ίδιο κωδικό
        for (int i = 0; i < rentalsSize; i++) { //χρήση αυτής της for για αποθήκευση της θέσης στο i
            if (rentals.get(i).getRentCode() == rental.getRentCode()) {
                rentals.get(i).getRentCar().setCarStatus(CarStatus.AVAILABLE);
                rentals.remove(i);
                System.out.println("Η ενοικίαση διαγράφηκε");
                return true;
            }
        }
        System.out.println("Δεν βρέθηκε κάποια ενοικίαση με τον κωδικό " + rental.getRentCode());
        return false;
    }

    @Override
    public ArrayList<Rental> getList() {
        return rentals;
    }

    @Override
    public int getSize() {
        return rentals.size();
    }

    @Override
    public void print() {
        for (Rental rental : rentals) {
            System.out.println(rental.toString());
        }
    }

    public void readCSV(CarManager carManager, ClientManager clientManager, EmployeeManager employeeManager) {
        String line;
        String delimiter = ",";

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); //skips the first line because it's a header

            while ((line = in.readLine()) != null) {
                String[] data = line.split(delimiter);

                int rentCode = Integer.parseInt(data[0].trim());
                String plate = data[1].trim();
                String clientAFM = data[2].trim();
                String start = data[3].trim();
                String end = data[4].trim();
                String employeeUsername = data[5].trim();

                LocalDate startDate = LocalDate.parse(start);
                LocalDate endDate = LocalDate.parse(end);

                Client client = clientManager.findByAFM(clientAFM);
                Car rentCar = carManager.findByPlate(plate);
                Employee employee = employeeManager.findByUsername(employeeUsername);

                if (client != null && rentCar != null && employee != null) {
                    Rental rental = new Rental(rentCode, rentCar, client, startDate, endDate, employee);
                    rentals.add(rental);
                    if (endDate.isAfter(LocalDate.now())) {
                        rentCar.setCarStatus(CarStatus.RENTED);
                    }
                } else {
                    System.out.println("Η ενοικίαση " + rentCode + " δεν έγινε γιατί δεν βρέθηκε το όχημα ή ο πελάτης");
                }
            }
        } catch (
                FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (
                IOException e) {
            System.out.println("Error: File not read!");
        }
    }

    @Override
    public void writeCSV() {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            String header = "κωδικός ενοικίασης,πινακίδα,ΑΦΜ πελάτη,ημέρα έναρξης,ημέρα λήξης,όνομα εργαζόμενου";
            out.write(header);
            out.newLine();

            for (Rental rental : rentals) {
                String line = rental.getRentCode() + "," +
                        rental.getRentCar().getPlate() + "," +
                        rental.getClient().getAFM() + "," +
                        rental.getStartDate() + "," +
                        rental.getEndDate() + "," +
                        rental.getEmployee().getUsername();

                out.write(line);
                out.newLine();
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.out.println("Error: File not read!");
        }
    }

    // να υλοποιηθεί

//    public ArrayList<Rental> search(String plate, String brand, String type, String model, String color, Boolean available) {
//        ArrayList<Car> foundCars = new ArrayList<>();
//
//        /* Τα αυτοκίνητα μπαίνουν στη for με τη σειρά, έπειτα γίνεται έλεγχος σε κάθε if αν έχει δοθεί τιμή για σύγκριση
//         αν δεν έχει δοθεί τιμή (null) τότε προχωράει στην επόμενη if χωρίς να μπει στο σώμα της
//         αν έχει δοθεί τιμή τότε τη συγκρίνει με αυτή του εκάστοτε αυτοκινήτου και
//         -αν είναι ίση τότε προχωράει στην επόμενη if χωρίς να μπει στο σώμα της
//         -αν δεν είναι ίση τότε μπαίνει στο σώμα της, κάνει continue και έρχεται το επόμενο αυτοκίνητο */
//
//        for (Car car : cars) { //null proofing (protection from NullPointerExceptions)
//            if (plate != null && !plate.isEmpty() && !car.getPlate().equalsIgnoreCase(plate)) {
//                continue;
//            } //η πινακίδα έρχεται πρώτη ώστε αν είναι διαφορετική δε χρειάζονται οι υπόλοιποι έλεγχοι
//            if (brand != null && !brand.isEmpty() && !car.getBrand().equalsIgnoreCase(brand)) {
//                continue;
//            }
//            if (type != null && !type.isEmpty() && !car.getType().equalsIgnoreCase(type)) {
//                continue;
//            }
//            if (model != null && !model.isEmpty() && !car.getModel().equalsIgnoreCase(model)) {
//                continue;
//            }
//            if (color != null && !color.isEmpty() && !car.getColor().equalsIgnoreCase(color)) {
//                continue;
//            }
//            if (available != null && car.isAvailable() != available) {
//                continue;
//            }
//            foundCars.add(car);
//        }
//        return foundCars;
//    }

}
