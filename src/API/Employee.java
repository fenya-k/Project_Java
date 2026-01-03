package API;

/**
 * Represents an employee with access to the rental system.
 * Manages the employee's personal details and login credentials.
 * Extends the {@link Person} abstract class.
 */
public class Employee extends Person {
    /** Unique username for system login */
    private String username;
    /** Unique password for system login */
    private String password;

    /**
     *Creates a new employee
     *
     * @param name      Employee's first name
     * @param surname   Employee's last name
     * @param username  Employee's username
     * @param email     Employee's email
     * @param password  Employee's password
     */
    public Employee(String name, String surname, String username, String email, String password) {
        super(name, surname, email);
        this.username = username;
        this.password = password;
    }

    // --- GETTERS - SETTERS ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // --- TO STRING ---

    @Override
    public String toString() {
        return "Employee: " +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'';
    }
}
