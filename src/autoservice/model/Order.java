package autoservice.model;

public class Order {

    public enum Status {
        ПРИНЯТ, В_РАБОТЕ, ГОТОВ, ВЫДАН
    }

    private int id;
    private int carId;
    private String carInfo;
    private String description;
    private String masterName;
    private double cost;
    private String dateCreated;
    private String dateCompleted;
    private Status status;

    public Order(int id, int carId, String carInfo, String description, String masterName, double cost, String dateCreated,
                 String dateCompleted, Status status) {
        this.id = id;
        this.carId = carId;
        this.carInfo = carInfo;
        this.description = description;
        this.masterName = masterName;
        this.cost = cost;
        this.dateCreated = dateCreated;
        this.dateCompleted = dateCompleted;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getCarId() {
        return carId;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public String getDescription() {
        return description;
    }

    public String getMasterName() {
        return masterName;
    }

    public double getCost() {
        return cost;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setDateCreated(String date) {
        this.dateCreated = date;
    }

    public void setDateCompleted(String date) {
        this.dateCompleted = date;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}