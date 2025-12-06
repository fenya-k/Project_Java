public class Car {
    private int id;
    private String plate;
    private String brand;
    private String type;
    private String model;
    private String year;
    private String color;
    private Boolean available;

    public Car(String plate, String brand, String type, String model, String year, String color, String available) {
        this.id=-1;
        this.plate = plate;
        this.brand = brand;
        this.type = type;
        this.model = model;
        this.year = year;
        this.color = color;
        if(available.equals("Διαθέσιμο")){
            this.available=true;
        }
        else if(available.equals("Ενοικιασμένο")){
            this.available=false;
        }
        else{
            this.available=null;
        }
    }
    public Car(int id, String plate, String brand, String type, String model, String year, String color, String available) {
        this.id=id;
        this.plate = plate;
        this.brand = brand;
        this.type = type;
        this.model = model;
        this.year = year;
        this.color = color;
        if(available.equals("Διαθέσιμο")){
            this.available=true;
        }
        else if(available.equals("Ενοικιασμένο")){
            this.available=false;
        }
        else{
            this.available=null;
        }
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Car:" +
                "id='" + id + '\'' +
                ", plate='" + plate + '\'' +
                ", brand='" + brand + '\'' +
                ", type='" + type + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", color='" + color + '\'' +
                ", available=" + available + '\'';
    }


}
