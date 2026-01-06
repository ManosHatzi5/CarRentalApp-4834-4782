package gui;

import api.CarStorage;
import api.Rent;
import api.UserStorage;

import javax.swing.*;
import java.awt.*;

public class RentFormPanel extends JPanel {

    JTextField startDateField = new JTextField(15);
    JTextField endDateField = new JTextField(15);
    JPanel mainContainer;

    private static String carId;
    private static String customerId;

    public static void setCarId(String givenCarId) { carId = givenCarId; }
    public static void setCustomerId(String givenCustomerId) { customerId = givenCustomerId; }

    public static String getCarId() { return carId; }

    public RentFormPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        CardLayout cl = (CardLayout) this.mainContainer.getLayout();
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JPanel headerContainer = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("RENT FORM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JLabel informationToUser = new JLabel("Enter the start and end dates.", SwingConstants.CENTER);
        informationToUser.setFont(new Font("Arial", Font.BOLD, 15));
        headerContainer.add(informationToUser, BorderLayout.CENTER);

        this.add(headerContainer, BorderLayout.NORTH);

        // CENTER SECTION
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Start Date
        addFormRow(centerPanel, gbc, 0, "Start Date (DD/MM/YYYY):", startDateField);

        // End Date
        addFormRow(centerPanel, gbc, 1, "End Date (DD/MM/YYYY):", endDateField);

        // Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0);

        JButton confirmButton = new JButton("Confirm Rent");
        confirmButton.setPreferredSize(new Dimension(150, 35));

        confirmButton.addActionListener(e -> handleConfirmRent(cl));

        centerPanel.add(confirmButton, gbc);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    private void handleConfirmRent(CardLayout cl) {
        Rent.reloadIdRentStorage();
        String[] rentData = new String[7];
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        String employeeUsername = UserStorage.getActiveUsername();
        String rentId;

        if (Rent.getIdRentStorage().isEmpty())
            rentId = "1";
        else
            rentId = (Integer.parseInt(Rent.getIdRentStorage().getLast()) + 1) + "";

        rentData[0] = carId;
        rentData[1] = customerId;
        rentData[2] = startDate;
        rentData[3] = endDate;
        rentData[4] = employeeUsername;
        rentData[5] = rentId;
        rentData[6] = "Ενοικιασμένο";

        if(startDate.isEmpty() || endDate.isEmpty())
            JOptionPane.showMessageDialog(this, "Please enter the start and end dates.", "Error", JOptionPane.ERROR_MESSAGE);

        if(Rent.RentCar(carId, customerId, startDate, endDate, employeeUsername, rentId, "Ενοικιασμένο")){
            JOptionPane.showMessageDialog(this, "Car Rent Succeeded!", "Success", JOptionPane.INFORMATION_MESSAGE);
            CarHistoryPanel.setRentData(rentData);
        }
        else
            JOptionPane.showMessageDialog(this, "Car Rent Failed!", "Error", JOptionPane.ERROR_MESSAGE);

        CarStorage.saveAllCars();
        startDateField.setText("");
        endDateField.setText("");
        CarStorage.reloadCarStorage();
        cl.show(this.mainContainer, "HOME_SCREEN");
    }


    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(labelText), gbc);

        // Field
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        field.setPreferredSize(new Dimension(200, 30));
        panel.add(field, gbc);
    }
}