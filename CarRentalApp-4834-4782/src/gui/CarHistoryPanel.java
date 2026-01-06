package gui;

import api.*;
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CarHistoryPanel extends JPanel {

    private JPanel mainContainer;
    private static DefaultTableModel tableModel;
    private JTable carTable;
    private static String carId;
    private static String[] rentData;

    public static void setRentData(String[] rentData) {CarHistoryPanel.rentData = rentData;}
    public static void setCarId(String givenCarId) {carId = givenCarId;}

    public CarHistoryPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        // Definition of title
        JLabel titleLabel = new JLabel("CAR RENT HISTORY", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        String[] labels = {
                "Car ID:", "Customer AFM:", "Start-Date of Rent:", "End-Date of Rent:",
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
        JButton addCarButton = new JButton("Add Car");
        JButton searchCarButton = new JButton("Search Car");
        JButton allCarsButton = new JButton("All Cars");
        JButton logoutButton = new JButton("Logout");

        homeButton.addActionListener(e -> {
            cl.show(mainContainer, "HOME_SCREEN");
        });

        returnCarButton.addActionListener(e -> handleCarReturn(cl));

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
        sideMenu.add(returnCarButton);
        sideMenu.add(addCarButton);
        sideMenu.add(searchCarButton);
        sideMenu.add(allCarsButton);
        sideMenu.add(logoutButton);
        return sideMenu;
    }

    private void handleCarReturn(CardLayout cl) {
        int selectedRow = carTable.getSelectedRow();
        CustomerHistoryStorage.reloadCustomerHistoryStorage();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to return first!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (tableModel.getValueAt(selectedRow, 6).equals("Διαθέσιμο"))
            JOptionPane.showMessageDialog(this, "You can't return this car because it is not rented at the moment!", "Error", JOptionPane.ERROR_MESSAGE);
        else {
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

        for (String[] data: CarHistoryStorage.getCarHistoryStorage()){
            if(data[0].equals(carId))
                tableModel.addRow(data);
        }
    }
}
