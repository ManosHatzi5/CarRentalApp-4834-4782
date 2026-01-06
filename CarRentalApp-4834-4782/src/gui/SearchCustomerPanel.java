package gui;

import api.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SearchCustomerPanel extends JPanel {

    private JTextField[] textFields = new JTextField[5];
    private static DefaultTableModel tableModel;
    private JTable customerTable;
    private JPanel mainContainer;
    private static String customerId;

    public SearchCustomerPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("CUSTOMER SEARCH", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some padding
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JLabel informationToUser = new JLabel("Enter the criteria on which you want to base your search, and leave blank the other.", SwingConstants.CENTER);
        informationToUser.setFont(new Font("Arial", Font.BOLD, 15));
        headerContainer.add(informationToUser, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        String[] labels = {
                "AFM:", "First Name:", "Last Name:", "Telephone Number:", "Email Address:"
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            inputPanel.add(label);

            textFields[i] = new JTextField(8);
            inputPanel.add(textFields[i]);
        }

        JButton confirmButton = new JButton("Search");
        confirmButton.addActionListener(e -> handleCustomerSearch());
        inputPanel.add(confirmButton);

        headerContainer.add(inputPanel, BorderLayout.SOUTH);

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

    private void handleCustomerSearch() {
        String[] searchData = new String[5];
        // Getting the user input
        for (int i = 0; i < searchData.length; i++) {
            searchData[i] = textFields[i].getText().trim();
            if (searchData[i].isEmpty())
                searchData[i] = null;
        }

        ArrayList<Customer> customerList = CustomerStorage.searchCustomer(searchData[0], searchData[1], searchData[2], searchData[3],
                searchData[4]);

        try{
            if(customerList.isEmpty()){
                JOptionPane.showMessageDialog(mainContainer, "No customers found.");
                refreshTable();
            }
            else
                refreshTable(customerList);
        }
        catch (NullPointerException e){
            JOptionPane.showMessageDialog(mainContainer, "Search cannot be performed without any inputs.", "Error", JOptionPane.ERROR_MESSAGE);
            refreshTable();
        }

        for (JTextField textField : textFields) textField.setText("");
    }

    private JPanel getSideMenu(JPanel mainContainer, CardLayout cl) {
        JPanel sideMenu = new JPanel(new GridLayout(10, 1, 0, 10));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton homeButton = new JButton("Home");
        JButton addCustomerButton = new JButton("Add Customer");
        JButton editCustomerButton = new JButton("Edit Customer");
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
                JOptionPane.showMessageDialog(this, "Please select a customer to edit first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                customerId = (String) tableModel.getValueAt(selectedRow, 0);
                CustomerStorage.reloadCustomerStorage();
                EditCustomerPanel.refreshTable(customerId);
                cl.show(mainContainer, "EDIT_CUSTOMER_SCREEN");
            }
        });

        historyButton.addActionListener(e -> {

            int selectedRow =  customerTable.getSelectedRow();
            if (selectedRow == -1)
                JOptionPane.showMessageDialog(this, "Please select a customer first!", "Error", JOptionPane.ERROR_MESSAGE);
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

        logoutButton.addActionListener(e -> {
            UserStorage.reloadUserStorage();
            cl.show(mainContainer, "LOGIN_SCREEN");
        });

        sideMenu.add(homeButton);
        sideMenu.add(addCustomerButton);
        sideMenu.add(editCustomerButton);
        sideMenu.add(historyButton);
        sideMenu.add(allCustomersButton);
        sideMenu.add(logoutButton);
        return sideMenu;
    }

    private static void refreshTable(ArrayList<Customer> customerList) {
        tableModel.setRowCount(0);
        ArrayList<Customer> customers = new ArrayList<>(customerList);
        for (Customer customer : customers) {
            customer.refreshCustomerData();
            Object[] row = customer.getCustomerData().clone();
            customerList.add(customer);
            tableModel.addRow(row);
        }
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
}
