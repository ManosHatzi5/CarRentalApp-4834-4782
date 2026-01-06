package gui;

import api.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SearchCarPanel extends JPanel {

    JPanel mainContainer;
    private JTextField[] textFields = new JTextField[8];
    private static DefaultTableModel tableModel;
    private JTable carTable;
    private static String carId;

    public SearchCarPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("CAR SEARCH", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some padding
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JLabel informationToUser = new JLabel("Enter the criteria on which you want to base your search, and leave the other blank.", SwingConstants.CENTER);
        informationToUser.setFont(new Font("Arial", Font.BOLD, 15));
        headerContainer.add(informationToUser, BorderLayout.CENTER);

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

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> handleCarSearch());
        inputPanel.add(searchButton);

        headerContainer.add(inputPanel, BorderLayout.SOUTH);

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

    private void handleCarSearch() {
        String[] searchData = new String[8];
        // Getting the user input
        for (int i = 0; i < searchData.length; i++) {
            searchData[i] = textFields[i].getText().trim();
            if (searchData[i].isEmpty())
                searchData[i] = null;
        }

        ArrayList<Car> carList = new ArrayList<>();
        carList = CarStorage.searchCar(searchData[0], searchData[1], searchData[2], searchData[3],
                searchData[4], searchData[5], searchData[6], searchData[7]);

        try{
            if(carList.isEmpty()){
                JOptionPane.showMessageDialog(mainContainer, "No cars found.");
                refreshTable();
            }
            else
                refreshTable(carList);
        }
        catch (NullPointerException e){
            JOptionPane.showMessageDialog(mainContainer, "Search cannot be performed without any inputs.", "Error", JOptionPane.ERROR_MESSAGE);
            refreshTable();
        }

        for (JTextField textField : textFields) textField.setText("");

    }

    private static void refreshTable(ArrayList<Car> carList) {
        tableModel.setRowCount(0);
        ArrayList<Car> cars = new ArrayList<>(carList);
        for (Car car : cars) {
            car.refreshCarData();
            Object[] row = car.getCarData().clone();
            carList.add(car);
            tableModel.addRow(row);
        }
    }

    private JPanel getSideMenu(JPanel mainContainer, CardLayout cl) {
        JPanel sideMenu = new JPanel(new GridLayout(10, 1, 0, 10));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton homeButton = new JButton("Home");
        JButton addCarButton = new JButton("Add Car");
        JButton editCarButton = new JButton("Edit Car");
        JButton rentCarButton = new JButton("Rent Car");
        JButton historyButton = new JButton("Car History");
        JButton allCarsButton = new JButton("All Cars");
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
        sideMenu.add(addCarButton);
        sideMenu.add(editCarButton);
        sideMenu.add(rentCarButton);
        sideMenu.add(historyButton);
        sideMenu.add(allCarsButton);
        sideMenu.add(logoutButton);
        return sideMenu;
    }


    public static void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Car> carList = new ArrayList<>();
        for (Car car : CarStorage.getCarStorage()) {
            car.refreshCarData();
            Object[] row = car.getCarData().clone();
            carList.add(car);
            tableModel.addRow(row);
        }
    }
}
