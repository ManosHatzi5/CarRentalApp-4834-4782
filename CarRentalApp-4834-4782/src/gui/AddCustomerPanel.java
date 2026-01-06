package gui;

import api.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AddCustomerPanel extends JPanel {

    private JTextField[] textFields = new JTextField[5];
    private static DefaultTableModel tableModel;
    private JTable customerTable;
    private JPanel mainContainer;
    private static String customerId;

    public AddCustomerPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("ADD NEW CUSTOMER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some padding
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        String[] labels = {
                "AFM:", "First Name:", "Last Name:", "Telephone Number:", "Email Address:"
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            inputPanel.add(label);

            textFields[i] = new JTextField( 13);
            inputPanel.add(textFields[i]);
        }

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> handleCustomerAddition());
        inputPanel.add(confirmButton);

        headerContainer.add(inputPanel, BorderLayout.CENTER);

        this.add(headerContainer, BorderLayout.NORTH);


        // LEFT SECTION SIDEMENU
        JPanel sideMenu = getSideMenu(mainContainer, cl);
        this.add(sideMenu, BorderLayout.WEST);

        // CENTER SECTION TABLE
        tableModel = new DefaultTableModel(labels, 0);
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel getSideMenu(JPanel mainContainer, CardLayout cl) {
        JPanel sideMenu = new JPanel(new GridLayout(10, 1, 0, 10));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton homeButton = new JButton("Home");
        JButton searchCustomerButton = new JButton("Search Customer");
        JButton editCustomerButton = new JButton("Edit Customer");
        JButton addCustomerButton = new JButton("Add Customer");
        JButton historyButton = new JButton("Customer History");
        JButton allCustomersButton = new JButton("All Customers");
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
                JOptionPane.showMessageDialog(this, "Please select a customer to edit first!", "Error", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Please select a customer first!", "Error", JOptionPane.ERROR_MESSAGE);
            else{
                String carId = (String) tableModel.getValueAt(selectedRow, 0);
                CarHistoryPanel.setCarId(carId);
                CustomerStorage.reloadCustomerStorage();
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
        sideMenu.add(logoutButton);
        return sideMenu;
    }

    public static void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Customer> customerList =  new ArrayList<>();
        for(Customer customer : CustomerStorage.getCustomerStorage()){
            Object[] row = {customer.getAfm(), customer.getFirstName(), customer.getLastName(), customer.getTelephone(), customer.getEmail()};
            customerList.add(customer);
            tableModel.addRow(row);
        }
    }

    public void handleCustomerAddition() {
        String[] customerData = new String[5];
        // Getting the User input
        for (int i = 0; i < customerData.length; i++) {
            customerData[i] = textFields[i].getText();
        }
        // Checking for any empty inputs
        for (String customerDatum : customerData) {
            if (customerDatum.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid Customer Details, try Again!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        // Adding the Customer with the logic that is implemented in the api
        Customer customer = new Customer(customerData[0], customerData[1], customerData[2], customerData[3], customerData[4]);
        if (CustomerStorage.addCustomer(customer)) {
            refreshTable();
            for (JTextField textField : textFields) {
                textField.setText("");
            }
            JOptionPane.showMessageDialog(this, "Customer Addition Succeeded!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(this, "Customer Addition Failed, because customer already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
