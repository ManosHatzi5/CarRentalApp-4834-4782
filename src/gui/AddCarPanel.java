package gui;

import api.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AddCarPanel extends JPanel {

    private JTextField[] textFields = new JTextField[8];
    private static DefaultTableModel tableModel;
    private JTable carTable;
    private static String carId;
    private JPanel mainContainer;

    public AddCarPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("ADD NEW CAR", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some padding
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        String[] labels = {
                "ID:", "License Plate:", "Brand:", "Type:",
                "Model:", "Production Year:", "Color:", "Availability:"
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            inputPanel.add(label);

            textFields[i] = new JTextField(8);
            inputPanel.add(textFields[i]);
        }

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> handleCarAddition());
        inputPanel.add(confirmButton);

        headerContainer.add(inputPanel, BorderLayout.CENTER);

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
        JButton searchCarButton = new JButton("Search Car");
        JButton editCarButton = new JButton("Edit Car");
        JButton rentCarButton = new JButton("Rent Car");
        JButton historyButton = new JButton("Car History");
        JButton allCarsButton = new JButton("All Cars");
        JButton logoutButton = new JButton("Logout");

        homeButton.addActionListener(e -> {
            cl.show(mainContainer, "HOME_SCREEN");
        });

        searchCarButton.addActionListener(e -> {
            CarStorage.reloadCarStorage();
            SearchCarPanel.refreshTable();
            cl.show(mainContainer, "SEARCH_CAR_SCREEN");
        });


        editCarButton.addActionListener(e -> {

            int selectedRow = carTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a car to edit first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                carId = (String) tableModel.getValueAt(selectedRow, 0);
                CarStorage.reloadCarStorage();
                EditCarPanel.refreshTable(carId);
                cl.show(mainContainer, "EDIT_CAR_SCREEN");
            }
        });

        rentCarButton.addActionListener(e -> {

            int selectedRow =  carTable.getSelectedRow();
            if (selectedRow == -1)
                JOptionPane.showMessageDialog(this, "Please select a car to rent first!", "Error", JOptionPane.ERROR_MESSAGE);
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
        sideMenu.add(searchCarButton);
        sideMenu.add(editCarButton);
        sideMenu.add(rentCarButton);
        sideMenu.add(historyButton);
        sideMenu.add(allCarsButton);
        sideMenu.add(logoutButton);
        return sideMenu;
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

    public void handleCarAddition() {
        String[] carData = new String[8];
        // Getting the user input
        for (int i = 0; i < carData.length; i++) {
            carData[i] = textFields[i].getText();
        }
        // Checking for any empty inputs
        for (String carDatum : carData) {
            if (carDatum.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid Car Details, try Again!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        // Adding the Car with the logic that is implemented in the api
        Car car = new Car(carData[0], carData[1], carData[2], carData[3], carData[4], carData[5], carData[6], carData[7]);
        if (CarStorage.addCar(car)){
            refreshTable();
            for (JTextField textField : textFields) {
                textField.setText("");
            }
            JOptionPane.showMessageDialog(this, "Car Addition Succeeded!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(this, "Car Addition Failed, car already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
