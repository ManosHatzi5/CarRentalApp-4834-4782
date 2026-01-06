package api;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CarHistoryStorage {

    static ArrayList<String[]> carHistoryStorage = new ArrayList<>();

    public static ArrayList<String[]> getCarHistoryStorage() {return carHistoryStorage;}

    public static void addCarRentHistory(String[] rentData){
        carHistoryStorage.add(rentData);
    }

    public static void reloadCarRentHistoryStorage(){
        carHistoryStorage.clear();
        try(BufferedReader reader = new BufferedReader(new FileReader("carRentHistory.txt"))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 7){
                    carHistoryStorage.add(parts);
                }else{
                    System.out.println("Invalid car rent history line format.");
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    // over-writing the file with the carHistoryStorage
    public static void saveCarRentHistoryStorage(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("carRentHistory.txt", false))){
            for (String[] data : carHistoryStorage){
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
