package gui;

import api.CarStorage;
import api.CustomerStorage;
import api.UserStorage;

import javax.swing.*;
import java.awt.*;

public class AddUserPanel extends JPanel {

    JTextField firstNameField = new JTextField(15);
    JTextField lastNameField = new JTextField(15);
    JTextField userField = new JTextField(15);
    JTextField emailField = new JTextField(15);
    JPasswordField passField = new JPasswordField(15);
    JPanel mainContainer;

    public AddUserPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("ADD USER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JLabel informationToUser = new JLabel("Enter the credentials of the user you want to add.", SwingConstants.CENTER);
        informationToUser.setFont(new Font("Arial", Font.BOLD, 15));
        headerContainer.add(informationToUser, BorderLayout.CENTER);

        this.add(headerContainer, BorderLayout.NORTH);


        // CENTER SECTION
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Spacing: Top, Left, Bottom, Right
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make fields stretch slightly if needed

        // First Name
        addFormRow(centerPanel, gbc, 0, "First Name:", firstNameField);

        // Last Name
        addFormRow(centerPanel, gbc, 1, "Last Name:", lastNameField);

        // Username
        addFormRow(centerPanel, gbc, 2, "Username:", userField);

        // Email
        addFormRow(centerPanel, gbc, 3, "Email:", emailField);

        // Password
        addFormRow(centerPanel, gbc, 4, "Password:", passField);

        // Button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; // Span across both columns
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0); // Extra space on top of the button

        JButton addUserButton = new JButton("Add User");
        addUserButton.setPreferredSize(new Dimension(150, 35));
        addUserButton.addActionListener(e -> handleAddUser(cl));

        centerPanel.add(addUserButton, gbc);

        // LEFT SECTION SIDEMENU
        JPanel sideMenu = getSideMenu(mainContainer, cl);

        this.add(sideMenu, BorderLayout.WEST);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    private void handleAddUser(CardLayout cl) {
        UserStorage.reloadUserStorage();

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String user = userField.getText();
        String email = emailField.getText();
        String pass = passField.getText();

        if(UserStorage.addUser(firstName, lastName, user, email, pass)){
            JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            cl.show(mainContainer, "HOME_SCREEN");
        }
        else
            JOptionPane.showMessageDialog(this, "Wrong input or user already exists!", "Error", JOptionPane.ERROR_MESSAGE);

        firstNameField.setText("");
        lastNameField.setText("");
        userField.setText("");
        emailField.setText("");
        passField.setText("");

    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        // 1. Add Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST; // Align labels to the right
        panel.add(new JLabel(labelText), gbc);

        // 2. Add Field
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST; // Align fields to the left
        // Set a standard size for fields
        field.setPreferredSize(new Dimension(200, 30));
        panel.add(field, gbc);
    }

    private JPanel getSideMenu(JPanel mainContainer, CardLayout cl) {
        JPanel sideMenu = new JPanel(new GridLayout(10, 1, 0, 10));
        sideMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton homeButton = new JButton("Home");
        JButton allCarsButton = new JButton("All Cars");
        JButton allCustomersButton = new JButton("All Customers");
        JButton logoutButton = new JButton("Logout");

        homeButton.addActionListener(e -> {
            cl.show(mainContainer, "HOME_SCREEN");
        });

        allCarsButton.addActionListener(e -> {
            CarStorage.reloadCarStorage();
            AllCarsPanel.refreshTable();
            cl.show(mainContainer, "ALL_CARS_SCREEN");
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
        sideMenu.add(allCarsButton);
        sideMenu.add(allCustomersButton);
        sideMenu.add(logoutButton);
        return sideMenu;
    }
}