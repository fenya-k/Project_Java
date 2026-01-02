package API;

import java.io.*;
import java.util.ArrayList;

public class EmployeeManager implements Manager<Employee> {
    private final ArrayList<Employee> employees;

    public EmployeeManager() {
        employees = new ArrayList<>();
    }

    public Employee login(String username, String password) {
        for (Employee employee : employees) {
            if (employee.getUsername().equals(username) && employee.getPassword().equals(password)) {
                return employee; //success
            }
        }
        return null; //fail
    }

    public String isValidEmployee( String username, String name, String surname, String email) {
        String fullString = "";

        if (name == null || name.isEmpty()) {
            fullString += "Παρακαλώ καταχωρήστε τον όνομα.\n";
        }
        if (surname == null || surname.isEmpty()) {
            fullString += "Παρακαλώ καταχωρήστε το επίθετο.\n";
        }
        if (username == null || username.isEmpty()) {
            fullString += "Παρακαλώ καταχωρήστε το παρατσούκλι.\n";
        }
        if (email == null || email.isEmpty()) {
            fullString += "Παρακαλώ καταχωρήστε το email.\n";
        }
        if (fullString.isEmpty()) {
            return "Επιτυχής καταχώρηση.";
        } else {
            return fullString;
        }
    }

    @Override
    public boolean add(Employee employee) {
        for (Employee e : employees) {
            if (e.getUsername().equalsIgnoreCase(employee.getUsername())) {
                System.out.println("Υπάρχει ήδη εργαζόμενος με username " + employee.getUsername());
                return false;
            }
        }
        employees.add(employee);
        System.out.println("Ο εργαζόμενος προστέθηκε: " + employee.getUsername());
        return true;
    }

    public boolean edit( String username, String name, String surname, String email) {
        Employee editedEmployee = findByUsername(username);
        editedEmployee.setName(name);
        editedEmployee.setSurname(surname);
        editedEmployee.setEmail(email);
        return true;
    }

    @Override
    public boolean remove(Employee employee) {
        int employeesSize = employees.size();
        //η μεταβλητή employeesSize χρησιμοποιείται για να μην υπολογίζεται επανειλημμένα στη for (int i = 0; i < cars.size(); i++)
        //από τη στιγμή που γίνεται return μετά τη διαγραφή δε θα εμφανίσει IndexOutOfBoundsException
        //σε περίπτωση που θέλαμε να διαγράψουμε πολλαπλά αυτοκίνητα με την ίδια πινακίδα θα ήταν λάθος
        for (int i = 0; i < employeesSize; i++) { //χρήση αυτής της for για αποθήκευση της θέσης στο i
            if (employees.get(i).getUsername().equalsIgnoreCase(employee.getUsername())) {
                employees.remove(i);
                System.out.println("Ο εργαζόμενος διαγράφηκε");
                return true;
            }
        }
        System.out.println("Δεν βρέθηκε εργαζόμενος με username " + employee.getUsername());
        return false;
    }

    @Override
    public void print() {
        System.out.println("List of employees");
        if (employees.isEmpty()) {
            System.out.println("No employees found");
        } else {
            for (Employee employee : employees) {
                System.out.println(employee.toString());
            }
        }
    }

    @Override
    public ArrayList<Employee> getList() {
        return new ArrayList<>(this.employees); //encapsulation - defensive copying
    }

    @Override
    public int getSize() {
        return employees.size();
    }

    public void readCSV(String filename) {
        String line;
        String delimiter = ",";
        employees.clear();

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); //skips the first line

            while ((line = in.readLine()) != null) {
                String[] data = line.split(delimiter);
                if (data.length >= 5) {
                    String name = data[0].trim();
                    String surname = data[1].trim();
                    String username = data[2].trim();
                    String email = data[3].trim();
                    String password = data[4].trim();
                    Employee employee = new Employee(name, surname, username, email, password);
                    employees.add(employee);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.err.println("Error: File not read!");
        }
    }

    public void writeCSV(String filename) {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {

            String header = "name,surname,username,email,password";
            out.write(header);
            out.newLine();

            for (Employee employee : employees) {
                String line = employee.getName() + "," +
                        employee.getSurname() + "," +
                        employee.getUsername() + "," +
                        employee.getEmail() + "," +
                        employee.getPassword();

                out.write(line);
                out.newLine();
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.err.println("Error: File not read!");
        }
    }

    public Employee findByUsername(String username) {
        for (Employee employee : employees) {
            if (employee.getUsername().equalsIgnoreCase(username)) {
                return employee;
            }
        }
        return null;
    }

    public boolean changePassword(String username, String oldPass, String newPass) {
        Employee employee = findByUsername(username);

        if (employee == null) {
            System.out.println("User not found");
            return false;
        }

        if (!employee.getPassword().equals(oldPass)) {
            System.out.println("Wrong old password");
            return false;
        }

        employee.setPassword(newPass);
        System.out.println("Password for user " + username + " was changed successfully");
        return true;
    }
}

