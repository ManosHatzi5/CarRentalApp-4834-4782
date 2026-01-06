package api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class InitializeFiles {


    // For every file that we want to create, we first check if it is already made
    // and if it's not (first time running the program) we create them and initialize the values based on
    // the elearning given files
    public InitializeFiles() {
        String fileCarsName = "cars.txt";
        String fileUsersName = "users.txt";
        String fileCustomersName = "customers.txt";
        String fileCarRentHistoryName = "carRentHistory.txt";
        String fileCustomerRentHistoryName = "customerRentHistory.txt";
        String fileIdRentStorageName = "idRentStorage.txt";

        File fileCars = new File(fileCarsName);
        File fileUsers = new File(fileUsersName);
        File fileCustomers = new File(fileCustomersName);
        File fileCarRentHistory = new File(fileCarRentHistoryName);
        File fileCustomerRentHistory = new File(fileCustomerRentHistoryName);
        File fileIdRentStorage = new File(fileIdRentStorageName);


        if (fileCars.exists()) {
            System.out.println("Cars file exists.");
        }
        else{
            String carData = """
                    1,ΙΚΥ1234,Toyota,Sedan,Corolla,2019,Ασημί,Διαθέσιμο
                    2,ΝΒΡ5678,Honda,Hatchback,Civic,2020,Μπλε,Διαθέσιμο
                    3,ΡΤΛ9012,Ford,SUV,Focus,2021,Μαύρο,Διαθέσιμο
                    4,ΧΖΑ3456,Volkswagen,Sedan,Passat,2018,Λευκό,Διαθέσιμο
                    5,ΕΜΚ7890,Nissan,Crossover,Qashqai,2022,Κόκκινο,Διαθέσιμο
                    """;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileCarsName))) {
                writer.write(carData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        if (fileUsers.exists()) {
            System.out.println("Users file exists.");
        }
        else{
            String userData = """
                John,Smith,jsmith,john.smith@test.com,password1
                Mary,Jones,mjones,mary.jones@test.com,password2
                Tom,Brown,tbrown,tom.brown@test.com,password3
                Anna,White,awhite,anna.white@test.com,password4
                Luke,Hall,lhall,luke.hall@test.com,password5
                """;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileUsersName))) {
                writer.write(userData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (fileCustomers.exists()) {
            System.out.println("Customers file exists.");
        }
        else{
            String customerData = """
                187059625,Manos,Chatzipanagiotidis,6980308632,manoshatzi06@gmail.com
                172494219,Dimitris,Tzoukas,6984241242,dimitristzoukas12@gmail.com
                """;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileCustomersName))) {
                writer.write(customerData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if(fileCarRentHistory.exists()){
            System.out.println("Car Rent History file exists.");
        }
        else{
            String carRentHistoryData = "";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileCarRentHistoryName))) {
                writer.write(carRentHistoryData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        if(fileCustomerRentHistory.exists()){
            System.out.println("Customer Rent History file exists.");
        }
        else{
            String customerRentHistoryData = "";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileCustomerRentHistoryName))) {
                writer.write(customerRentHistoryData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if(fileIdRentStorage.exists()){
            System.out.println("Id Rent Storage file exists.");
        }
        else{

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileIdRentStorageName))){
                writer.write("");
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
