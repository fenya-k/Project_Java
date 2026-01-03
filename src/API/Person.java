package API;

/**
 * Abstract class representing a person.
 * The base for specific roles such as {@link Client} and {@link Employee}.
 */
public abstract class Person {
    protected String name;
    protected String surname;
    protected String email;

    /**
     * Creates a new person
     *
     * @param name    Person's first name
     * @param surname Person's last name
     * @param email   Person's email
     */
    public Person(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    // --- GETTERS - SETTERS ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
