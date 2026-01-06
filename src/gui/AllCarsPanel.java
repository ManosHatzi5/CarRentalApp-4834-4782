package gui;

import api.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AllCarsPanel extends JPanel {

    private JPanel mainContainer;
    private static DefaultTableModel tableModel;
    private JTable carTable;
    private static String carId;

    public static String getCarId(){return carId;};

    public AllCarsPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        // Definition of title
        JLabel titleLabel = new JLabel("ALL CARS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some padding
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        String[] labels = {
                "ID:", "License Plate:", "Brand:", "Type:",
                "Model:", "Production Year:", "Color:", "Availability:"
        };

        headerContainer.add(topPanel, BorderLayout.CENTER);
        headerContainer.add(titleLabel, BorderLayout.NORTH);
        this.add(headerContainer, BorderLayout.NORTH);

        // LEFT SECTION SIDEMENU
        JPanel sideMenu = new JPanel(new GridLayout(10, 1, 0, 10));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton homeButton = new JButton("Home");
        JButton addCarButton = new JButton("Add Car");
        JButton editCarButton = new JButton("Edit Car");
        JButton searchCarButton = new JButton("Search Car");
        JButton rentCarButton = new JButton("Rent Car");
        JButton historyButton = new JButton("Car History");
        JButton allCustomersButton = new JButton("All Customers");
        JButton logoutButton = new JButton("Logout");

        homeButton.addActionListener(e -> {
            cl.show(mainContainer, "HOME_SCREEN");
        });

        addCarButton.addActionListener(e -> {
            CarStorage.reloadCarStorage();
            AddCarPanel.refreshTable();
            cl.show(mainContainer, "ADD_CAR_SCREEN");
        });

        editCarButton.addActionListener(e -> {

            int selectedRow = carTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a car to edit first!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                carId = (String) tableModel.getValueAt(selectedRow, 0);
                CarStorage.reloadCarStorage();
                EditCarPanel.refreshTable(carId);
                cl.show(mainContainer, "EDIT_CAR_SCREEN");
            }
        });

        searchCarButton.addActionListener(e -> {
            CarStorage.reloadCarStorage();
            SearchCarPanel.refreshTable();
            cl.show(mainContainer, "SEARCH_CAR_SCREEN");
        });

        rentCarButton.addActionListener(e -> {

            int selectedRow =  carTable.getSelectedRow();
            if (selectedRow == -1)
                JOptionPane.showMessageDialog(this, "Please select a car to rent first!", "Error", JOptionPane.ERROR_MESSAGE);
            else if(tableModel.getValueAt(selectedRow, 7).equals("Ενοικιασμένο"))
                JOptionPane.showMessageDialog(this, "You can't rent this car at the moment, it is already rented!", "Error", JOptionPane.ERROR_MESSAGE);
            else{
                String carId = (String) tableModel.getValueAt(selectedRow, 0);
                RentCarPanel.setCarId(carId);
                CustomerStorage.reloadCustomerStorage();
                RentCarPanel.refreshTable();
                cl.show(mainContainer, "RENT_CAR_SCREEN");
            }
        });

        historyButton.addActionListener(e -> {

            int selectedRow =  carTable.getSelectedRow();
            if (selectedRow == -1)
                JOptionPane.showMessageDialog(this, "Please select a car first!", "Error", JOptionPane.ERROR_MESSAGE);
            else{
                String carId = (String) tableModel.getValueAt(selectedRow, 0);
                CarHistoryPanel.setCarId(carId);
                CarHistoryStorage.reloadCarRentHistoryStorage();
                CarHistoryPanel.refreshTable();
                cl.show(mainContainer, "CARS_HISTORY_SCREEN");
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
        sideMenu.add(addCarButton);
        sideMenu.add(editCarButton);
        sideMenu.add(searchCarButton);
        sideMenu.add(rentCarButton);
        sideMenu.add(historyButton);
        sideMenu.add(allCustomersButton);
        sideMenu.add(logoutButton);

        this.add(sideMenu, BorderLayout.WEST);

        // CENTER SECTION
        tableModel = new DefaultTableModel(labels, 0);
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        carTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(carTable);
        this.add(scrollPane, BorderLayout.CENTER);

    }

    public static void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Car> carList =  new ArrayList<>();
        for(Car car : CarStorage.getCarStorage()){
            car.refreshCarData();
            Object[] row = car.getCarData().clone();
            carList.add(car);
            tableModel.addRow(row);
        }
    }
}
