package gui;

import api.CarStorage;
import api.CustomerStorage;
import api.User;
import api.UserStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class RemoveUserPanel extends JPanel {

    private JPanel mainContainer;
    private static DefaultTableModel tableModel;
    private JTable userTable;

    public RemoveUserPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("REMOVE USER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some padding
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JLabel informationToUser = new JLabel("Select from below the user you want to remove.", SwingConstants.CENTER);
        informationToUser.setFont(new Font("Arial", Font.BOLD, 15));
        headerContainer.add(informationToUser, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        String[] labels = {
                "First Name:", "Last Name:", "Username:", "Email:", "Password:"
        };

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> handleUserRemove());
        inputPanel.add(removeButton);

        headerContainer.add(inputPanel, BorderLayout.SOUTH);
        this.add(headerContainer, BorderLayout.NORTH);


        // LEFT SECTION SIDEMENU
        JPanel sideMenu = getSideMenu(mainContainer, cl);

        this.add(sideMenu, BorderLayout.WEST);


        // CENTER SECTION
        tableModel = new DefaultTableModel(labels, 0);
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(userTable);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void handleUserRemove() {
        UserStorage.reloadUserStorage();
        int selectedRow = userTable.getSelectedRow();

        if (selectedRow == -1)
            JOptionPane.showMessageDialog(this, "Please select a user to remove first!", "Error", JOptionPane.ERROR_MESSAGE);
        else{
            String firstName = userTable.getValueAt(selectedRow, 0).toString();
            String lastName = userTable.getValueAt(selectedRow, 1).toString();
            String username = userTable.getValueAt(selectedRow, 2).toString();
            String email = userTable.getValueAt(selectedRow, 3).toString();
            String password = userTable.getValueAt(selectedRow, 4).toString();

            JOptionPane.showMessageDialog(this, "User removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            UserStorage.removeUser(firstName, lastName, username, email, password);
            refreshTable();
        }
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

    public static void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<User> userList = new ArrayList<>();
        for (User user : UserStorage.getUserStorage()) {
            Object[] row = user.getUserData();
            userList.add(user);
            tableModel.addRow(row);
        }
    }
}
