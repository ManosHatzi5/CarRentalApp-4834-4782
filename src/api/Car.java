package api;

public class Car {
    private String id;
    private String licensePlate;
    private String brand;
    private String type;
    private String model;
    private String productionYear;
    private String color;
    private String availability;
    private String[] carData = new String[8];

    public Car(String id, String licensePlate, String brand, String type, String model,
               String productionYear, String color, String availability) {

        this.id = id;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.type = type;
        this.model = model;
        this.productionYear = productionYear;
        this.color = color;
        this.availability = availability;

        carData[0] = id;
        carData[1] = licensePlate;
        carData[2] = brand;
        carData[3] = type;
        carData[4] = model;
        carData[5] = productionYear;
        carData[6] = color;
        carData[7] = availability;
    }

    public void refreshCarData() {
        carData[0] = id;
        carData[1] = licensePlate;
        carData[2] = brand;
        carData[3] = type;
        carData[4] = model;
        carData[5] = productionYear;
        carData[6] = color;
        carData[7] = availability;
    }

    public String[] getCarData() {
        return carData;
    }

    @Override
    public String toString() {
        return id + "," + licensePlate + "," + brand + "," + type + "," + model + "," + productionYear +
                "," + color + "," + availability;
    }

    public void setBrand(String brand){ this.brand = brand; }

    public void setType(String type){ this.type = type; }

    public void setModel(String model){ this.model = model; }

    public void setProductionYear(String productionYear){ this.productionYear = productionYear; }

    public void setColor(String colour){ this.color = colour; }

    public void setAvailability(String availability){ this.availability = availability; }

    public void editAvailability(String availability){ this.availability = availability; }

    public String getId(){ return id; }

    public String getLicensePlate(){ return licensePlate; }

}
