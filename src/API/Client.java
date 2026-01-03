package API;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Client extends Person implements History, ReadWriteCSV {
    private String AFM;
    private String phone;
    ArrayList<Rental> clientRentals;

    public Client(String name, String surname, String AFM, String phone, String email) {
        super(name, surname, email);
        this.AFM = AFM;
        this.phone = phone;
        clientRentals = new ArrayList<>();
    }

    public String getAFM() {
        return AFM;
    }

    public void setAFM(String AFM) {
        this.AFM = AFM;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean addRental(Rental rental) {
        for (Rental r : clientRentals) {
            if (r.getRentCode() == rental.getRentCode()) {
                System.out.println("Υπάρχει ήδη ενοικίαση με κωδικό " + rental.getRentCode());
                return false;
            }
        }
        clientRentals.add(rental);
        return true;
    }


    public void printRentals() {
        if (clientRentals.isEmpty()) {
            System.out.println("No rentals found");
        } else {
            for (Rental rental : clientRentals) {
                System.out.println(rental.toString());
            }
        }
    }

    @Override
    public ArrayList<Rental> returnList() {
        return new ArrayList<>(this.clientRentals); //encapsulation - defensive copying
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
                    String AFMrecord = data[2].trim();

                    if (AFMrecord.equalsIgnoreCase(this.AFM)) {
                        try {
                            int rentCode = Integer.parseInt(data[0].trim());
                            String plate = data[1].trim();
                            LocalDate start = LocalDate.parse(data[3].trim());
                            LocalDate end = LocalDate.parse(data[4].trim());
                            String userEmp = data[5].trim();

                            Car car=CM.findByPlate(plate);
                            Employee employee = EM.findByUsername(userEmp);

                            if (car != null && employee != null) {
                                Rental rental = new Rental(rentCode, car, this, start, end, employee);
                                this.addRental(rental);
                            }
                        } catch (Exception e) {
                            System.out.println("Error");
                        }
                    }
                }
            }
        }catch (IOException e){
            System.err.println("File not found");
        }
    }

    public void writeCSV(String filename,ArrayList<Rental> list) {
        String line;
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename, true))) {

            for (Rental rental : clientRentals) {
                line = rental.getRentCode() + "," +
                        rental.getRentCar().getPlate() + "," +
                        this.getAFM() + "," +
                        rental.getStartDate() + "," +
                        rental.getEndDate() + "," +
                        rental.getEmployee().getUsername();

                out.write(line);
                out.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing car history");
        }
    }
    @Override
    public String toString() {
        return "Client: " +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", AFM='" + AFM + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'';
    }

}
