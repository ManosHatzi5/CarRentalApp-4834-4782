package gui;

import api.CarStorage;
import api.CustomerStorage;
import api.UserStorage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomePanel extends JPanel {

    private JButton btn1, btn2, btn3;
    private JButton btn4, btn5, btn6;
    private JButton btn7, btn8, btn9;

    public HomePanel(JPanel mainContainer) {

        CardLayout cl = (CardLayout) mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("HOME", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 34));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(8, 3, 15, 15));
        gridPanel.setBorder(new EmptyBorder(10, 50, 50, 50)); // Padding: Top, Left, Bottom, Right

        // Row 1
        btn1 = new JButton("All Cars");
        btn2 = new JButton("All Customers");
        btn3 = new JButton("Add Car");
        btn1.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn2.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn3.setFont(new Font("SansSerif", Font.BOLD, 24));

        // Row 2
        btn4 = new JButton("Search Car");
        btn5 = new JButton("Add Customer");
        btn6 = new JButton("Search Customer");
        btn4.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn5.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn6.setFont(new Font("SansSerif", Font.BOLD, 24));

        // Row 3
        btn7 = new JButton("Add User");
        btn8 = new JButton("Remove User");
        btn9 = new JButton("Log Out");
        btn7.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn8.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn9.setFont(new Font("SansSerif", Font.BOLD, 24));

        btn1.addActionListener(e -> {
            CarStorage.reloadCarStorage();
            AllCarsPanel.refreshTable();
            cl.show(mainContainer, "ALL_CARS_SCREEN");
        });

        btn2.addActionListener(e -> {
            CustomerStorage.reloadCustomerStorage();
            AllCustomersPanel.refreshTable();
            cl.show(mainContainer, "ALL_CUSTOMERS_SCREEN");
        });

        btn3.addActionListener(e -> {
            CarStorage.reloadCarStorage();
            AddCarPanel.refreshTable();
            cl.show(mainContainer, "ADD_CAR_SCREEN");
        });

        btn4.addActionListener(e -> {
            CarStorage.reloadCarStorage();
            SearchCarPanel.refreshTable();
            cl.show(mainContainer, "SEARCH_CAR_SCREEN");
        });

        btn5.addActionListener(e -> {
            CustomerStorage.reloadCustomerStorage();
            AddCustomerPanel.refreshTable();
            cl.show(mainContainer, "ADD_CUSTOMER_SCREEN");
        });

        btn6.addActionListener(e -> {
            CarStorage.reloadCarStorage();
            SearchCustomerPanel.refreshTable();
            cl.show(mainContainer, "SEARCH_CUSTOMER_SCREEN");
        });

        btn7.addActionListener(e -> {
            UserStorage.reloadUserStorage();
            cl.show(mainContainer, "ADD_USER_SCREEN");
        });

        btn8.addActionListener(e -> {
            UserStorage.reloadUserStorage();
            RemoveUserPanel.refreshTable();
            cl.show(mainContainer, "REMOVE_USER_SCREEN");
        });

        btn9.addActionListener(e -> {
            UserStorage.reloadUserStorage();
            cl.show(mainContainer, "LOGIN_SCREEN");
        });


        // Buttons
        gridPanel.add(btn1);
        gridPanel.add(btn2);
        gridPanel.add(btn3);

        gridPanel.add(btn4);
        gridPanel.add(btn5);
        gridPanel.add(btn6);

        gridPanel.add(btn7);
        gridPanel.add(btn8);
        gridPanel.add(btn9);

        this.add(gridPanel, BorderLayout.CENTER);
    }
}
