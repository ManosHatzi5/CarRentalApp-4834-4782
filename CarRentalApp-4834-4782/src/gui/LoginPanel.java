package gui;

import api.UserStorage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JPanel {

    private JPanel mainContainer;
    JTextField userField = new JTextField(15);
    JPasswordField passField = new JPasswordField(15);

    public LoginPanel(JPanel mainContainer) {

        this.mainContainer = mainContainer;
        this.setLayout(new BorderLayout());

        // TOP SECTION
        JLabel titleLabel = new JLabel("User Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBorder(new EmptyBorder(50, 0, 0, 0)); // Padding at top
        this.add(titleLabel, BorderLayout.NORTH);

        // CENTER SECTION
        JPanel centerPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username Label
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(new JLabel("Username:"), gbc);

        // Username Field
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        userField.setPreferredSize(new Dimension(200, 30));
        centerPanel.add(userField, gbc);

        // Password Label
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(new JLabel("Password:"), gbc);

        // Password Field
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passField.setPreferredSize(new Dimension(200, 30));
        centerPanel.add(passField, gbc);

        // Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.insets = new Insets(30, 0, 0, 0);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());

        loginButton.setPreferredSize(new Dimension(100, 35));
        centerPanel.add(loginButton, gbc);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    private void handleLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());
        if(UserStorage.userExists(username, password)) {
            CardLayout cl = (CardLayout) this.mainContainer.getLayout();
            cl.show(this.mainContainer, "HOME_SCREEN");
            UserStorage.setActiveUsername(username);
            userField.setText("");
            passField.setText("");
        }else{
            JOptionPane.showMessageDialog(null, "Username or password is incorrect.");
            userField.setText("");
            passField.setText("");
        }
    }
}