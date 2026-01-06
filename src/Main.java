
// ERGASIA APO: ΜΑΝΟΣ ΧΑΤΖΗΠΑΝΑΓΙΩΤΊΔΗΣ - ΔΗΜΗΤΡΗΣ ΤΖΟΎΚΑΣ
// ΑΕΜ:         4834                      4782

// NOTE!!!:
//  This project was made using the latest version of Java. If something in this project does not seem
//  to work, please check if you have the same version (25.0.1).

// IMPORTANT!!!:
//  The logic of the program is that the car IDs are starting from 1 and when new cars are added the ID
//  gets incremented by 1. If you add e.g. the 7th car with an ID of 12, the program is likely to give
//  false outputs. Please base your inputs based on that.


import api.*;
import gui.*;
import javax.swing.*;
import java.awt.*;

public class Main {

    static void main(String[] args) {
        new InitializeFiles();
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {

        JFrame frame = new JFrame("Car Rental App 4834-4782");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800); // Set a reasonable default size
        frame.setLocationRelativeTo(null); // Center on screen

        CardLayout cardLayout = new CardLayout();
        JPanel mainContainer = new JPanel(cardLayout);

        HomePanel homePanel = new HomePanel(mainContainer);
        AddCarPanel addCarPanel = new AddCarPanel(mainContainer);
        AddCustomerPanel addCustomerPanel = new AddCustomerPanel(mainContainer);
        AddUserPanel addUserPanel = new AddUserPanel(mainContainer);
        AllCarsPanel allCarsPanel = new AllCarsPanel(mainContainer);
        AllCustomersPanel allCustomersPanel = new AllCustomersPanel(mainContainer);
        CarHistoryPanel carHistoryPanel = new CarHistoryPanel(mainContainer);
        CustomerHistoryPanel customerHistoryPanel = new CustomerHistoryPanel(mainContainer);
        EditCarPanel editCarPanel = new EditCarPanel(mainContainer);
        EditCustomerPanel editCustomerPanel = new EditCustomerPanel(mainContainer);
        LoginPanel loginPanel = new LoginPanel(mainContainer);
        RemoveUserPanel removeUserPanel = new RemoveUserPanel(mainContainer);
        RentCarPanel rentCarPanel = new RentCarPanel(mainContainer);
        SearchCarPanel searchCarPanel = new SearchCarPanel(mainContainer);
        SearchCustomerPanel searchCustomerPanel = new SearchCustomerPanel(mainContainer);
        RentFormPanel rentFormPanel = new RentFormPanel(mainContainer);

        mainContainer.add(homePanel, "HOME_SCREEN");
        mainContainer.add(addCarPanel, "ADD_CAR_SCREEN");
        mainContainer.add(addCustomerPanel, "ADD_CUSTOMER_SCREEN");
        mainContainer.add(allCarsPanel, "ALL_CARS_SCREEN");
        mainContainer.add(addUserPanel, "ADD_USER_SCREEN");
        mainContainer.add(allCustomersPanel, "ALL_CUSTOMERS_SCREEN");
        mainContainer.add(carHistoryPanel, "CARS_HISTORY_SCREEN");
        mainContainer.add(customerHistoryPanel, "CUSTOMERS_HISTORY_SCREEN");
        mainContainer.add(editCarPanel, "EDIT_CAR_SCREEN");
        mainContainer.add(editCustomerPanel, "EDIT_CUSTOMER_SCREEN");
        mainContainer.add(loginPanel, "LOGIN_SCREEN");
        mainContainer.add(removeUserPanel, "REMOVE_USER_SCREEN");
        mainContainer.add(rentCarPanel, "RENT_CAR_SCREEN");
        mainContainer.add(searchCarPanel, "SEARCH_CAR_SCREEN");
        mainContainer.add(searchCustomerPanel, "SEARCH_CUSTOMER_SCREEN");
        mainContainer.add(rentFormPanel, "RENT_FORM_SCREEN");

        frame.add(mainContainer);

        UserStorage.reloadUserStorage();
        cardLayout.show(mainContainer, "LOGIN_SCREEN");

        frame.setVisible(true);
    }
}