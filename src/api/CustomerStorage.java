package api;

import java.io.*;
import java.util.ArrayList;

public class CustomerStorage {
    static ArrayList<Customer> customerStorage = new ArrayList<>();

    public static ArrayList<Customer> getCustomerStorage(){ return customerStorage; }

    public static boolean addCustomer(Customer customer){
        for (Customer c : customerStorage){
            if (c.getAfm().equals(customer.getAfm())){
                System.out.println("Customer already exists.");
                return false;
            }
        }
        customerStorage.add(customer);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))){
            writer.write(customer.toString());
            writer.newLine();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }

    public static void editCustomer(Customer customer, String firstName, String lastName, String telephone, String email){

        Customer editedCustomer = customer;
        for (Customer c : customerStorage){
            if (c.getAfm().equals(customer.getAfm())){
                editedCustomer = c;
            }
        }

        if (firstName != null) editedCustomer.setFirstName(firstName);
        if (lastName != null) editedCustomer.setLastName(lastName);
        if (telephone != null) editedCustomer.setTelephone(telephone);
        if (email != null) editedCustomer.setEmail(email);

        saveAllCustomers();
    }

    public static void reloadCustomerStorage(){
        customerStorage.clear();
        try(BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 5){
                    String afm = parts[0];
                    String firstName = parts[1];
                    String lastName = parts[2];
                    String telephone = parts[3];
                    String email = parts[4];
                    Customer customer = new Customer(afm, firstName, lastName, telephone, email);
                    customerStorage.add(customer);
                }else{
                    System.out.println("Invalid customer line format.");
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<Customer> searchCustomer(String afm, String firstName, String lastName, String telephone, String email){

        ArrayList<Customer> customerList = new ArrayList<>();

        String[] customerData = new String[5];
        customerData[0] = afm;
        customerData[1] = firstName;
        customerData[2] = lastName;
        customerData[3] = telephone;
        customerData[4] = email;

        // Checking if all inputs are null
        boolean allNull = true;
        for (String s: customerData){
            if (s != null){
                allNull = false;
                break;
            }
        }

        if (allNull){
            System.out.println("Search can't be done, all values are null, returning null.");
            return null;
        }

        for (Customer customer: CustomerStorage.getCustomerStorage()){
            boolean stillIdentical = false;
            customer.refreshCustomerData();
            for (int k = 0; k < customerData.length; k++){
                if (customerData[k] != null){
                    if (customerData[k].equals(customer.getCustomerData()[k])){
                        stillIdentical = true;
                    }
                    else{
                        stillIdentical = false;
                        break;
                    }
                }
            }
            if (stillIdentical)
                customerList.add(customer);
        }

        if(customerList.isEmpty()){
            System.out.println("No cars found, returning null.");
            return customerList;
        }
        return customerList;
    }

    // over-writing the customer file with the customerStorage
    private static void saveAllCustomers(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", false))){
            for(Customer customer : customerStorage){
                writer.write(customer.toString());
                writer.newLine();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
