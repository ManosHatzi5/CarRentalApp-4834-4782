package gui;

import api.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class RentCarPanel extends JPanel {

    private static DefaultTableModel tableModel;
    private JTable customerTable;
    private JPanel mainContainer;
    private static String customerId;
    private static String carId;

    public static void setCarId(String givenCarId) {carId = givenCarId;}

    public RentCarPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("RENT CAR", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some padding
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JPanel informationContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        JLabel informationToUser = new JLabel("Select the customer that wants to rent the car and press select.", SwingConstants.CENTER);
        informationToUser.setFont(new Font("Arial", Font.BOLD, 15));
        informationContainer.add(informationToUser, BorderLayout.NORTH);

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(e -> handleSelect(cl));
        informationContainer.add(selectButton);

        headerContainer.add(informationContainer, BorderLayout.CENTER);

        String[] labels = {
                "AFM:", "First Name:", "Last Name:", "Telephone Number:", "Email Address:"
        };

        this.add(headerContainer, BorderLayout.NORTH);

        // LEFT SECTION SIDEMENU
        JPanel sideMenu = getSideMenu(mainContainer, cl);

        this.add(sideMenu, BorderLayout.WEST);

        // CENTER SECTION
        tableModel = new DefaultTableModel(labels, 0);
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        this.add(scrollPane, BorderLayout.CENTER);

    }

    private void handleSelect(CardLayout cl) {

        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1 )
            JOptionPane.showMessageDialog(this, "Please select a customer first.");
        else{
            String customerId = customerTable.getValueAt(selectedRow, 0).toString(); // AFM
            RentFormPanel.setCarId(carId);
            RentFormPanel.setCustomerId(customerId);
            cl.show(mainContainer, "RENT_FORM_SCREEN");
        }
    }

    private JPanel getSideMenu(JPanel mainContainer, CardLayout cl) {
        JPanel sideMenu = new JPanel(new GridLayout(10, 1, 0, 10));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton homeButton = new JButton("Home");
        JButton addCustomerButton = new JButton("Add Customer");
        JButton editCustomerButton = new JButton("Edit Customer");
        JButton searchCustomerButton = new JButton("Search Customer");
        JButton historyButton = new JButton("Customer History");
        JButton allCustomersButton = new JButton("All Customers");
        JButton allCarsButton = new JButton("All Cars");
        JButton logoutButton = new JButton("Logout");

        homeButton.addActionListener(e -> {
            cl.show(mainContainer, "HOME_SCREEN");
        });

        addCustomerButton.addActionListener(e -> {
            CustomerStorage.reloadCustomerStorage();
            AddCustomerPanel.refreshTable();
            cl.show(mainContainer, "ADD_CUSTOMER_SCREEN");
        });


        editCustomerButton.addActionListener(e -> {

            int selectedRow = customerTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a customer to edit first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                customerId = (String) tableModel.getValueAt(selectedRow, 0);
                CustomerStorage.reloadCustomerStorage();
                EditCustomerPanel.refreshTable(customerId);
                cl.show(mainContainer, "EDIT_CUSTOMER_SCREEN");
            }
        });

        searchCustomerButton.addActionListener(e -> {
            CustomerStorage.reloadCustomerStorage();
            SearchCustomerPanel.refreshTable();
            cl.show(mainContainer, "SEARCH_CUSTOMER_SCREEN");
        });

        historyButton.addActionListener(e -> {

            int selectedRow =  customerTable.getSelectedRow();
            if (selectedRow == -1)
                JOptionPane.showMessageDialog(this, "Please select a car first!", "Error", JOptionPane.ERROR_MESSAGE);
            else{
                String customerId = (String) tableModel.getValueAt(selectedRow, 0);
                CustomerHistoryPanel.setCustomerId(customerId);
                CustomerHistoryStorage.reloadCustomerHistoryStorage();
                CustomerHistoryPanel.refreshTable();
                cl.show(mainContainer, "CUSTOMERS_HISTORY_SCREEN");
            }
        });

        allCustomersButton.addActionListener(e -> {
            CustomerStorage.reloadCustomerStorage();
            AllCustomersPanel.refreshTable();
            cl.show(mainContainer, "ALL_CUSTOMERS_SCREEN");
        });

        allCarsButton.addActionListener(e -> {
            CarStorage.reloadCarStorage();
            AllCarsPanel.refreshTable();
            cl.show(mainContainer, "ALL_CARS_SCREEN");
        });

        logoutButton.addActionListener(e -> {
            UserStorage.reloadUserStorage();
            cl.show(mainContainer, "LOGIN_SCREEN");
        });

        sideMenu.add(homeButton);
        sideMenu.add(addCustomerButton);
        sideMenu.add(editCustomerButton);
        sideMenu.add(searchCustomerButton);
        sideMenu.add(historyButton);
        sideMenu.add(allCustomersButton);
        sideMenu.add(allCarsButton);
        sideMenu.add(logoutButton);
        return sideMenu;
    }


    public static void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Customer> customerList = new ArrayList<>();
        for(Customer customer : CustomerStorage.getCustomerStorage()){
            Object[] row = {customer.getAfm(), customer.getFirstName(), customer.getLastName(), customer.getTelephone(), customer.getEmail()};
            customerList.add(customer);
            tableModel.addRow(row);
        }
    }
}
