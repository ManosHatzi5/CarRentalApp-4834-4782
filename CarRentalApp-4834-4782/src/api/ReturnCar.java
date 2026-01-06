package api;

public class ReturnCar {

    // change the RAM (the carStorage) and save the changes to the file
    public static void returnCar(Car car){
        car.editAvailability("Διαθέσιμο");
        CarStorage.saveAllCars();
    }

}
