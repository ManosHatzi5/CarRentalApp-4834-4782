package api;

import java.io.*;
import java.util.ArrayList;

public class CarStorage {
    static ArrayList<Car> carStorage =  new ArrayList<>();

    public static ArrayList<Car> getCarStorage(){ return carStorage; }

    public static boolean addCar(Car car){
        for(Car c : carStorage){
            if (c.getId().equals(car.getId()) || c.getLicensePlate().equals(car.getLicensePlate()) ){
                System.out.println("Car already exists");
                return false;
            }
        }
        carStorage.add(car);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("cars.txt", true))){
            writer.write(car.toString());
            writer.newLine();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static void reloadCarStorage(){
        carStorage.clear();
        try(BufferedReader reader = new BufferedReader(new FileReader("cars.txt"))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 8){
                    String id = parts[0];
                    String licensePlate = parts[1];
                    String brand = parts[2];
                    String type = parts[3];
                    String model = parts[4];
                    String productionYear = parts[5];
                    String color = parts[6];
                    String availability = parts[7];
                    carStorage.add(new Car(id, licensePlate, brand, type, model, productionYear, color, availability));
                }else{
                    System.out.println("Invalid car line format.");
                }
            }

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<Car> searchCar(String id, String licensePlate, String brand, String type, String model,
                                String productionYear, String color, String availability){

        ArrayList<Car> carList = new ArrayList<>();

        String[] carData = new String[8];
        carData[0] = id;
        carData[1] = licensePlate;
        carData[2] = brand;
        carData[3] = type;
        carData[4] = model;
        carData[5] = productionYear;
        carData[6] = color;
        carData[7] = availability;

        // Checking if all inputs are null
        boolean allNull = true;
        for (String s: carData){
            if (s != null){
                allNull = false;
                break;
            }
        }

        if(allNull){
            System.out.println("Search can't be done, all values are null, returning null.");
            return null;
        }

        for (Car car: CarStorage.getCarStorage()){
            boolean stillIdentical = false;
            car.refreshCarData();
            for (int k = 0; k < carData.length; k++){
                if (carData[k] != null){
                    if (carData[k].equals(car.getCarData()[k])){
                        stillIdentical = true;
                    }
                    else{
                        stillIdentical = false;
                        break;
                    }
                }
            }
            if (stillIdentical)
                carList.add(car);
        }

        if(carList.isEmpty()){
            System.out.println("No cars found, returning null.");
            return carList;
        }
        return carList;
    }

    public static void editCar(Car car, String brand, String type, String model, String productionYear, String color, String availability){

        Car editedCar = carStorage.get(Integer.parseInt(car.getId()) - 1); // getting the car that was passed to us from the real carStorage

        if (brand != null) editedCar.setBrand(brand);
        if (type != null) editedCar.setType(type);
        if (model != null) editedCar.setModel(model);
        if (productionYear != null) editedCar.setProductionYear(productionYear);
        if (color != null) editedCar.setColor(color);
        if (availability != null) editedCar.setAvailability(availability);

        saveAllCars();
    }

    // over-writing the cars file based on the carStorage
    public static void saveAllCars(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("cars.txt", false))){
            for (Car car : carStorage){
                writer.write(car.toString());
                writer.newLine();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
