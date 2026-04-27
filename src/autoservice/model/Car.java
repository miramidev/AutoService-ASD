package autoservice.model;

public class Car {
    private int id;
    private String brand;
    private String model;
    private String licensePlate;
    private int year;
    private String ownerName;
    private String ownerPhone;

    public Car(int id, String brand, String model, String licensePlate, int year, String ownerName, String ownerPhone) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.year = year;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getYear() {
        return year;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setLicensePlate(String lp) {
        this.licensePlate = lp;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }
}