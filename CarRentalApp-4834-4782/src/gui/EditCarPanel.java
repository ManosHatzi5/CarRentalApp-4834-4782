package gui;

import api.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EditCarPanel extends JPanel {

    private JPanel mainContainer;
    private JTextField[] textFields = new JTextField[8];
    private static DefaultTableModel tableModel;
    private JTable carTable;

    public EditCarPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) mainContainer.getLayout();

        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("EDIT CAR", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some padding
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JLabel informationToUser = new JLabel("Enter the parameters you want to change, and leave the others blank.", SwingConstants.CENTER);
        informationToUser.setFont(new Font("Arial", Font.BOLD, 15));
        headerContainer.add(informationToUser, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        String[] labels = {
                "ID:", "License Plate:", "Brand:", "Type:",
                "Model:", "Production Year:", "Color:", "Availability:"
        };

        for (int i = 0; i < labels.length; i++) {
            if(i == 0 || i == 1)
                continue;
            JLabel label = new JLabel(labels[i]);
            inputPanel.add(label);

            textFields[i] = new JTextField(8);
            inputPanel.add(textFields[i]);
        }
        JButton confirmButton = new JButton("Edit");
        confirmButton.addActionListener(e -> handleCarEdit());
        inputPanel.add(confirmButton);

        headerContainer.add(inputPanel, BorderLayout.SOUTH);

        this.add(headerContainer, BorderLayout.NORTH);


        // LEFT SECTION SIDEMENU
        JPanel sideMenu = getJPanel(mainContainer, cl);

        this.add(sideMenu, BorderLayout.WEST);


        // CENTER SECTION
        tableModel = new DefaultTableModel(labels, 0);
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        carTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(carTable);
        this.add(scrollPane, BorderLayout.CENTER);

    }

    private JPanel getJPanel(JPanel mainContainer, CardLayout cl) {
        JPanel sideMenu = new JPanel(new GridLayout(10, 1, 0, 10));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton homeButton = new JButton("Home");
        JButton addCarButton = new JButton("Add Car");
        JButton searchCarButton = new JButton("Search Car");
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

        searchCarButton.addActionListener(e -> {
            CarStorage.reloadCarStorage();
            SearchCarPanel.refreshTable();
            cl.show(mainContainer, "SEARCH_CAR_SCREEN");
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
        sideMenu.add(searchCarButton);
        sideMenu.add(rentCarButton);
        sideMenu.add(historyButton);
        sideMenu.add(allCarsButton);
        sideMenu.add(logoutButton);
        return sideMenu;
    }

    private void handleCarEdit() {
        String[] carData = new String[6];
        // Getting the user input
        for (int i = 0; i < carData.length; i++) {
            carData[i] = textFields[i+2].getText().trim();
            if (carData[i].isEmpty())
                carData[i] = null;
        }

        String[] newCarData = new String[8];
        for (int i = 0; i < newCarData.length; i++)
            newCarData[i] = tableModel.getValueAt(0, i).toString();

        for (int i = 0; i < carData.length; i++) {
            if (carData[i] != null)
                newCarData[i+2] = carData[i].trim();
        }

        Car car = new Car(newCarData[0], newCarData[1], newCarData[2], newCarData[3],
                newCarData[4], newCarData[5], newCarData[6], newCarData[7]);

        CarStorage.editCar(car, carData[0], carData[1], carData[2], carData[3], carData[4], carData[5]);

        String carId = tableModel.getValueAt(0,0).toString();
        refreshTable(carId);
        for (int i = 2; i < textFields.length; i++)
            textFields[i].setText("");
    }

    public static void refreshTable(String carId) {
        tableModel.setRowCount(0);
        for (Car car : CarStorage.getCarStorage()) {
            if (car.getId().equals(carId)) {
                car.refreshCarData();
                tableModel.addRow(car.getCarData());
                break;
            }
        }
    }
}
