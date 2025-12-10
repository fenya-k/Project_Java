package API;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EmployeeManager {
    private String filename="users.csv";
    private ArrayList<Employee> employees;

    public EmployeeManager(){
        employees=new ArrayList<>();
        readCSV();
    }
    public Employee login(String username,String password) {
        for (Employee employee : employees) {
            if (employee.getUsername().equals(username) && employee.getPassword().equals(password)) {
                return employee; //success
            }
        }
        return null; //fail
    }

    public void readCSV(){
        String line;
        String delimiter = ",";
        employees.clear();

        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            in.readLine(); //skips the first line

            while ((line = in.readLine()) != null) {
                String[] data = line.split(delimiter);
                if(data.length>=5) {
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
            System.out.println("Error: File not read!");
        }
    }
    public ArrayList<Employee> getEmployees(){
        return employees;
    }

    public Employee findByUsername(String username){
        for (Employee employee: employees){
            if(employee.getUsername().equalsIgnoreCase(username)){
                return employee;
            }
        }
        return null;
    }
}

