package API;

public class Car {
    private static int counter = 1;

    private int id;
    private String plate;
    private String brand;
    private String type;
    private String model;
    private String year;
    private String color;
    private CarStatus carStatus;

    public Car(String plate, String brand, String type, String model, String year, String color) {
        this.id = counter++;
        this.plate = plate;
        this.brand = brand;
        this.type = type;
        this.model = model;
        this.year = year;
        this.color = color;
        carStatus = CarStatus.AVAILABLE;
    }

    public Car(int id, String plate, String brand, String type, String model, String year, String color, CarStatus carStatus) {
        this.id = id;
        this.plate = plate;
        this.brand = brand;
        this.type = type;
        this.model = model;
        this.year = year;
        this.color = color;
        this.carStatus = carStatus;

        if (id >= counter) {
            counter = id + 1;
        }
    }

    public int getId() {
        return id;
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

    public CarStatus getCarStatus() { return carStatus; }

    public void setCarStatus(CarStatus carStatus) { this.carStatus = carStatus; }

    public boolean isAvailable() {
        return this.carStatus == CarStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return "API.Car: " +
                "id=" + id +
                ", plate='" + plate + '\'' +
                ", brand='" + brand + '\'' +
                ", type='" + type + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", color='" + color + '\'' +
                ", car status=" + carStatus;
    }

    public static class RentalManager{


    }
}
