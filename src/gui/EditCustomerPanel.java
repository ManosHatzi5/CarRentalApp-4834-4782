package gui;

import api.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class EditCustomerPanel extends JPanel {

    private JTextField[] textFields = new JTextField[5];
    private static DefaultTableModel tableModel;
    private JTable customerTable;
    private JPanel mainContainer;
    private static String customerId;

    public EditCustomerPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("EDIT CUSTOMER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some padding
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JLabel informationToUser = new JLabel("Enter the parameters you want to change, and leave the others blank.", SwingConstants.CENTER);
        informationToUser.setFont(new Font("Arial", Font.BOLD, 15));
        headerContainer.add(informationToUser, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        String[] labels = {
                "AFM:", "First Name:", "Last Name:", "Telephone Number:", "Email Address:"
        };

        for (int i = 0; i < labels.length; i++) {
            if(i == 0)
                continue;
            JLabel label = new JLabel(labels[i]);
            inputPanel.add(label);

            textFields[i] = new JTextField(8);
            inputPanel.add(textFields[i]);
        }

        JButton confirmButton = new JButton("Edit");
        confirmButton.addActionListener(e -> handleCustomerEdit());
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

    private void handleCustomerEdit() {
        String[] customerData = new String[4];
        // Getting the user input
        for (int i = 0; i < customerData.length; i++) {
            customerData[i] = textFields[i+1].getText().trim();
            if (customerData[i].isEmpty())
                customerData[i] = null;
        }

        String[] newCustomerData = new String[5];
        for (int i = 0; i < newCustomerData.length; i++)
            newCustomerData[i] = tableModel.getValueAt(0, i).toString();

        for (int i = 0; i < customerData.length; i++) {
            if (customerData[i] != null)
                newCustomerData[i+2] = customerData[i].trim();
        }

        Customer customer = new Customer(newCustomerData[0], newCustomerData[1], newCustomerData[2], newCustomerData[3],
                newCustomerData[4]);
        CustomerStorage.editCustomer(customer, customerData[0], customerData[1], customerData[2], customerData[3]);

        String customerId = tableModel.getValueAt(0,0).toString(); // AFM
        refreshTable(customerId);
        for (int i = 1; i < textFields.length; i++)
            textFields[i].setText("");

    }

    public static void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Customer> customerList =  new ArrayList<>();
        for(Customer customer : CustomerStorage.getCustomerStorage()){
            customer.refreshCustomerData();
            customerList.add(customer);
            tableModel.addRow(customer.getCustomerData());
        }
    }

    public static void refreshTable(String customerId) {
        tableModel.setRowCount(0);
        for (Customer customer : CustomerStorage.getCustomerStorage()) {
            if (customer.getAfm().equals(customerId)) {
                customer.refreshCustomerData();
                tableModel.addRow(customer.getCustomerData());
                break;
            }
        }
    }

    private JPanel getSideMenu(JPanel mainContainer, CardLayout cl) {
        JPanel sideMenu = new JPanel(new GridLayout(10, 1, 0, 10));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton homeButton = new JButton("Home");
        JButton addCustomerButton = new JButton("Add Customer");
        JButton searchCustomerButton = new JButton("Search Customer");
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
        sideMenu.add(searchCustomerButton);
        sideMenu.add(historyButton);
        sideMenu.add(allCustomersButton);
        sideMenu.add(logoutButton);
        return sideMenu;
    }
}
