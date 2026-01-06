package gui;

import api.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CustomerHistoryPanel extends JPanel {

    JPanel mainContainer;
    private static DefaultTableModel tableModel;
    private JTable carTable;
    private static String customerId;

    public static void setCustomerId(String customerId) {CustomerHistoryPanel.customerId = customerId;}

    public CustomerHistoryPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        // Definition of title
        JLabel titleLabel = new JLabel("CUSTOMER RENT HISTORY", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some padding
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        String[] labels = {
                "Customer AFM:", "Car ID:", "Start-Date of Rent:", "End-Date of Rent:",
                "Employee Username:", "Rent ID:", "Availability:"
        };

        headerContainer.add(topPanel, BorderLayout.CENTER);
        headerContainer.add(titleLabel, BorderLayout.NORTH);
        this.add(headerContainer, BorderLayout.NORTH);

        // LEFT SECTION SIDEMENU
        JPanel sideMenu = getSideMenu(mainContainer, cl);

        this.add(sideMenu, BorderLayout.WEST);

        // CENTER SECTION
        tableModel = new DefaultTableModel(labels, 0);
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        carTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(carTable);
        this.add(scrollPane, BorderLayout.CENTER);

    }


    private JPanel getSideMenu(JPanel mainContainer, CardLayout cl) {
        JPanel sideMenu = new JPanel(new GridLayout(10, 1, 0, 10));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton homeButton = new JButton("Home");
        JButton returnCarButton = new JButton("Return Car");
        JButton addCustomerButton = new JButton("Add Customer");
        JButton searchCustomerButton = new JButton("Search Customer");
        JButton allCustomersButton = new JButton("All Customers");
        JButton logoutButton = new JButton("Logout");

        homeButton.addActionListener(e -> {
            cl.show(mainContainer, "HOME_SCREEN");
        });

        returnCarButton.addActionListener(e -> handleReturnCar(cl));

        addCustomerButton.addActionListener(e -> {
            CustomerStorage.reloadCustomerStorage();
            AddCustomerPanel.refreshTable();
            cl.show(mainContainer, "ADD_CUSTOMER_SCREEN");
        });

        searchCustomerButton.addActionListener(e -> {
            CarStorage.reloadCarStorage();
            SearchCarPanel.refreshTable();
            cl.show(mainContainer, "SEARCH_CUSTOMER_SCREEN");
        });

        allCustomersButton.addActionListener(e -> {
            CustomerStorage.reloadCustomerStorage();
            AllCarsPanel.refreshTable();
            cl.show(mainContainer, "ALL_CUSTOMERS_SCREEN");
        });

        logoutButton.addActionListener(e -> {
            UserStorage.reloadUserStorage();
            cl.show(mainContainer, "LOGIN_SCREEN");
        });

        sideMenu.add(homeButton);
        sideMenu.add(returnCarButton);
        sideMenu.add(addCustomerButton);
        sideMenu.add(searchCustomerButton);
        sideMenu.add(allCustomersButton);
        sideMenu.add(logoutButton);
        return sideMenu;
    }

    private void handleReturnCar(CardLayout cl) {
        int selectedRow = carTable.getSelectedRow();
        CarHistoryStorage.reloadCarRentHistoryStorage();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer first!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (tableModel.getValueAt(selectedRow, 6).equals("Διαθέσιμο"))
            JOptionPane.showMessageDialog(this, "You can't return this car because it is not rented at the moment!", "Error", JOptionPane.ERROR_MESSAGE);
        else {
            String carId = tableModel.getValueAt(selectedRow, 1).toString();
            ArrayList<Car> oneCarList = CarStorage.searchCar(carId,null,null,null,
                    null,null,null,null);
            Car car = oneCarList.getFirst();

            ReturnCar.returnCar(car);

            String rentId = tableModel.getValueAt(selectedRow, 5).toString();
            tableModel.setValueAt("Διαθέσιμο",  selectedRow, 6);

            for (String[] data: CarHistoryStorage.getCarHistoryStorage()){
                if (data[5].equals(rentId)){
                    data[6] = "Διαθέσιμο";
                    break;
                }
            }
            for (String[] data: CustomerHistoryStorage.getCustomerHistoryStorage()){
                if (data[5].equals(rentId)){
                    data[6] = "Διαθέσιμο";
                    break;
                }
            }
            CarHistoryStorage.saveCarRentHistoryStorage();
            CustomerHistoryStorage.saveCustomerRentHistoryStorage();
            cl.show(mainContainer, "HOME_SCREEN");
        }
    }

    public static void refreshTable() {
        tableModel.setRowCount(0);
        for (String[] data: CustomerHistoryStorage.getCustomerHistoryStorage()){
            if (data[0].equals(customerId))
                tableModel.addRow(data);
        }
    }
}
