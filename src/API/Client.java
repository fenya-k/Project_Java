package API;

public class Client extends Person {
    private String AFM;
    private String phone;

    public Client(String name, String surname, String AFM, String phone, String email) {
        super(name, surname, email);
        this.AFM = AFM;
        this.phone = phone;
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
    public String toString() {
        return "Client: " +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", AFM='" + AFM + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'';
    }
}
