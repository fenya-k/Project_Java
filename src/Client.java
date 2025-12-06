public class Client {
    private String AFM;
    private String name;
    private String phone;
    private String email;

    public Client(String AFM, String name, String phone, String email) {
        this.AFM = AFM;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getAFM() {
        return AFM;
    }

    public void setAFM(String AFM) {
        this.AFM = AFM;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client:" +
                "AFM='" + AFM + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'';
    }
}
