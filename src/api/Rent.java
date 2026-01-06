package api;

import java.io.*;
import java.util.ArrayList;

public class Rent {

    static ArrayList<String> idRentStorage =  new ArrayList<>();

    public static ArrayList<String> getIdRentStorage() {return idRentStorage;}

     public static void reloadIdRentStorage(){
         idRentStorage.clear();
         try(BufferedReader reader = new BufferedReader(new FileReader("idRentStorage.txt"))){
             String line;
             while((line = reader.readLine()) != null){
                 idRentStorage.add(line);
             }
         }
         catch(IOException e){
             e.printStackTrace();
         }
     }

    public static boolean RentCar(String carId, String customerId, String startDate, String endDate,
                           String employeeUsername, String rentId, String availability){

        // the method .searchCar() returns an arraylist of cars, but here we want to rent a single car
        // we know that the carId is unique, so even if we search for all cars that have this carId
        // we are sure that it will always return max one car
        ArrayList<Car> oneCarList = CarStorage.searchCar(carId,null,null,null,
                null,null,null,null);

        Car car = oneCarList.getFirst();

        saveIdStorage(rentId);

        car.editAvailability("Ενοικιασμένο");

        String[] rentData = new String[7];

        rentData[0] = carId;
        rentData[1] = customerId;
        rentData[2] = startDate;
        rentData[3] = endDate;
        rentData[4] = employeeUsername;
        rentData[5] = rentId;
        rentData[6] = "Ενοικιασμένο";

        // Adding the rent data to each history storage, car/customer,
        // and saving the changes to the corresponding history file.
        CarHistoryStorage.addCarRentHistory(rentData);
        saveCarHistory(rentData);

        // I change the order between car ID and customer AFM so the one we are referring to
        // is always first. So because carHistory is based on each car, car ID is first, and now
        // for customerHistory, customer AFM will go first and change "seats" with car ID.

        String temp;
        temp = rentData[0];        // temp <- Car ID
        rentData[0] = rentData[1]; // 1st cell <- Customer AFM
        rentData[1] = temp;        // 2nd cell <- Car ID

        CustomerHistoryStorage.addCustomerRentHistory(rentData);
        saveCustomerHistory(rentData);
        return true;
    }

    // over-writing the car history file
    private static void saveCarHistory(String[] rentData){
        String lineForFile = String.join(",", rentData);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("carRentHistory.txt", true))){
            writer.write(lineForFile);
            writer.newLine();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    // over-writing the customer history file
    private static void saveCustomerHistory(String[] rentData){
        String lineForFile = String.join(",", rentData);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("customerRentHistory.txt", true))){
            writer.write(lineForFile);
            writer.newLine();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    // adding the changes of rent to the storage, and also saving it into the ID rent file
    public static void saveIdStorage(String id){
        idRentStorage.add(id);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("idRentStorage.txt", true))){
            writer.write(id);
            writer.newLine();
        }
        catch(Exception e){
            e.printStackTrace();
        }



    }
}
