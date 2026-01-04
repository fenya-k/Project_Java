package API;

import java.io.*;
import java.util.ArrayList;

/**
 * This is the manager of all employees in the rental system.
 * Implementing the interface {@link Manager}.
 * Handles validation, adding, editing, removing, searching, file reading/writing and login.
 */
public class EmployeeManager implements Manager<Employee> {

    /**
     * The ArrayList of all the employees in the system.
     */
    private final ArrayList<Employee> employees;

    /**
     * Constructor for a new manager. Initializes the ArrayList.
     */
    public EmployeeManager() {
        employees = new ArrayList<>();
    }

    /**
     * Validation method to check if all the necessary fields have been given before add/edit.
     * Checks for null/empty values.
     *
     * @param username Employee's username
     * @param name     Employee's first name
     * @param surname  Employee's last name
     * @param email    Employee's email
     * @return "Επιτυχής καταχώρηση." if valid, otherwise an error message
     */
    public String isValidEmployee(String username, String name, String surname, String email) {
        StringBuilder fullString = new StringBuilder();

        if (name == null || name.isEmpty()) fullString.append("Παρακαλώ καταχωρήστε το όνομα.\n");
        if (surname == null || surname.isEmpty()) fullString.append("Παρακαλώ καταχωρήστε το επίθετο.\n");
        if (username == null || username.isEmpty()) fullString.append("Παρακαλώ καταχωρήστε το παρατσούκλι.\n");
        if (email == null || email.isEmpty()) fullString.append("Παρακαλώ καταχωρήστε το email.\n");

        if (fullString.isEmpty()) {
            return "Επιτυχής καταχώρηση.";
        } else {
            return fullString.toString();
        }
    }

    /**
     * Adds a new employee to the system after checking for duplicate username.
     *
     * @param employee The employee to be added
     * @return true if added successfully, false if username exists
     */
    @Override
    public boolean add(Employee employee) {
        for (Employee e : employees) {
            if (e.getUsername().equalsIgnoreCase(employee.getUsername())) {
                System.out.println("There is already an employee with username: " + employee.getUsername());
                return false;
            }
        }
        employees.add(employee);
        System.out.println("The employee has been added: " + employee.getUsername());
        return true;
    }

    /**
     * Edits the details of an existing employee.
     * The username is used to find the employee and cannot be changed here.
     *
     * @param username The username
     * @param name     New name
     * @param surname  New surname
     * @param email    New email
     * @return true if found and edited, false otherwise
     */
    public boolean edit(String username, String name, String surname, String email) {
        Employee editedEmployee = findByUsername(username);
        // Safety Check
        if (editedEmployee != null) {
            editedEmployee.setName(name);
            editedEmployee.setSurname(surname);
            editedEmployee.setEmail(email);
            return true;
        }
        return false;
    }

    /**
     * Removes an employee from the system.
     *
     * @param employee The employee to remove
     * @return true if removed, false if not found
     */
    @Override
    public boolean remove(Employee employee) {
        int employeesSize = employees.size();
        // We store the size to avoid recalculating it in the loop.
        // Since we return immediately after removal, standard iteration is safe.
        for (int i = 0; i < employeesSize; i++) {
            if (employees.get(i).getUsername().equalsIgnoreCase(employee.getUsername())) {
                employees.remove(i);
                System.out.println("The employee has been removed.");
                return true;
            }
        }
        System.out.println("Cannot find employee with username: " + employee.getUsername());
        return false;
    }

    /**
     * Retrieves the list of all the employees.
     *
     * @return An ArrayList of the employees
     */
    @Override
    public ArrayList<Employee> getList() {
        return employees;
    }

    /**
     * Retrieves the number of employees in the list.
     *
     * @return The size of the list
     */
    @Override
    public int getSize() {
        return employees.size();
    }

    /**
     * Prints the details of all employees in the list.
     */
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

    /**
     * Finds an employee using their username.
     *
     * @param username The username to search for
     * @return The Employee object if found, null otherwise
     */
    public Employee findByUsername(String username) {
        for (Employee employee : employees) {
            if (employee.getUsername().equalsIgnoreCase(username)) {
                return employee;
            }
        }
        return null;
    }

    // --- READ - WRITE ---

    /**
     * Reads employees from a CSV file and populates the list.
     *
     * @param filename The path to the CSV file
     */
    public void readCSV(String filename) {
        String line;
        String delimiter = ",";
        // Optional: employees.clear(); if you want to reset list on load

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); //skips the first line

            while ((line = in.readLine()) != null) {
                Employee employee = getEmployee(line, delimiter);
                if (employee != null) {
                    employees.add(employee);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found!");
        } catch (IOException e) {
            System.err.println("Error: File not read!");
        }
    }

    /**
     * Helper method to parse an employee from a CSV line.
     *
     * @param line      The CSV line string
     * @param delimiter The delimiter used in the CSV
     * @return A new Employee object, or null if data is incomplete
     */
    private static Employee getEmployee(String line, String delimiter) {
        String[] data = line.split(delimiter);

        // Safety check
        if (data.length < 5) return null;

        String name = data[0].trim();
        String surname = data[1].trim();
        String username = data[2].trim();
        String email = data[3].trim();
        String password = data[4].trim();

        return new Employee(name, surname, username, email, password);
    }

    /**
     * Writes the list of employees to a CSV file.
     * Overwrites the file.
     *
     * @param filename The path to the CSV file
     */
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

    // --- LOGIN ---

    /**
     * Authenticates an employee using username and password.
     *
     * @param username The entered username
     * @param password The entered password
     * @return The Employee object if login is successful, null otherwise
     */
    public Employee login(String username, String password) {
        for (Employee employee : employees) {
            if (employee.getUsername().equals(username) && employee.getPassword().equals(password)) {
                return employee; //success
            }
        }
        return null; //fail
    }

    /**
     * Changes the password of an employee.
     *
     * @param username The username of the employee
     * @param oldPass  The old password for verification
     * @param newPass  The new password to set
     * @return true if changed successfully, false if verification failed
     */
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