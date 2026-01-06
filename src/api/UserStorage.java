package api;

import java.io.*;
import java.util.*;

public class UserStorage {

    static ArrayList<User> userStorage = new ArrayList<>();

    private static String activeUsername;

    public static void setActiveUsername(String username) {activeUsername = username;}

    public static String getActiveUsername() {return activeUsername;}

    public static ArrayList<User> getUserStorage() { return userStorage; }


    public static boolean addUser(String firstName, String lastName, String username,
                               String email, String pass){

        String[] userData = {firstName, lastName, username, email, pass};

        for (String userDatum : userData) {
            if (userDatum.trim().isEmpty())
                return false;
        }

        for (User user: userStorage) {
            if (user.getUserData()[2].equals(userData[2])) {
                System.out.println("This user already exists!");
                return false;
            }
        }

        User user = new User(userData[0], userData[1], userData[2], userData[3], userData[4]);
        userStorage.add(user);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(user.toString());
            writer.newLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void reloadUserStorage(){
        userStorage.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String firstName = parts[0];
                    String lastName  = parts[1];
                    String username  = parts[2];
                    String email     = parts[3];
                    String password  = parts[4];
                    userStorage.add(new User(firstName, lastName, username, email, password));
                }else{
                    System.out.println("Invalid user line input.");
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void removeUser(String firstName, String lastName, String username,
                                  String email, String pass){

        String[] userData = {firstName, lastName, username, email, pass};

        ArrayList<User> copyUserStorage = new ArrayList<>(UserStorage.getUserStorage());
        for (User user: copyUserStorage) {
            if (user.getUserData()[2].equals(userData[2].trim()) &&
                    user.getUserData()[4].equals(userData[4].trim()) ) {
                userStorage.remove(user);
                saveUsers();
                return;
            }
        }
    }

    private static void saveUsers(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", false))){
            for (User user: userStorage) {
                writer.write(user.toString());
                writer.newLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean userExists(String username, String password){
        for (User user: userStorage) {
            if (user.getUserData()[2].equals(username) && user.getUserData()[4].equals(password)) {
                return true;
            }
        }
        return false;
    }
}
