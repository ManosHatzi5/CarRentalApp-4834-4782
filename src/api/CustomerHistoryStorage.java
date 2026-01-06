package api;

import java.io.*;
import java.util.ArrayList;

public class CustomerHistoryStorage {

    static ArrayList<String[]> customerHistoryStorage = new ArrayList<>();

    public static ArrayList<String[]> getCustomerHistoryStorage() {return customerHistoryStorage;}

    public static void addCustomerRentHistory(String[] rentData){
        customerHistoryStorage.add(rentData);
    }

    public static void reloadCustomerHistoryStorage(){
        customerHistoryStorage.clear();
        try(BufferedReader reader = new BufferedReader(new FileReader("customerRentHistory.txt"))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 7){
                    customerHistoryStorage.add(parts);
                }else{
                    System.out.println("Invalid customer rent history line format.");
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    // over-writing the customer history file with the customerHistoryStorage
    public static void saveCustomerRentHistoryStorage(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("customerRentHistory.txt", false))){
            for (String[] data : customerHistoryStorage){
                for(String s : data){
                    if(s.equals(data[6])){
                        writer.write(s.trim());
                        break;
                    }
                    writer.write(s.trim()+",");
                }
                writer.newLine();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
