package view;

import javax.swing.*;
import java.awt.*;
import model.UserModel; // Using UserModel
import java.sql.SQLException;

public class ProfilePanel extends JPanel {
    private String username;
    private JPasswordField oldPasswordField, newPasswordField;
    private JButton changePasswordButton, deleteAccountButton;

    public ProfilePanel(String username) {
        this.username = username;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(18, 18, 18));
        Color spotifyGreen = new Color(30, 215, 96);

        JLabel title = new JLabel("👤 Your Profile", SwingConstants.CENTER);
        title.setForeground(spotifyGreen);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel usernameLabel = new JLabel("Logged in as: " + username);
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Change Password Section
        JLabel oldPassLabel = new JLabel("Current Password:");
        oldPassLabel.setForeground(Color.WHITE);
        oldPasswordField = new JPasswordField(15);
        oldPasswordField.setMaximumSize(new Dimension(250, 30));

        JLabel newPassLabel = new JLabel("New Password:");
        newPassLabel.setForeground(Color.WHITE);
        newPasswordField = new JPasswordField(15);
        newPasswordField.setMaximumSize(new Dimension(250, 30));

        changePasswordButton = new JButton("🔑 Change Password");
        changePasswordButton.setBackground(Color.BLACK);
        changePasswordButton.setForeground(spotifyGreen);
        changePasswordButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        changePasswordButton.setBorder(BorderFactory.createLineBorder(spotifyGreen, 2));
        changePasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePasswordButton.addActionListener(e -> changePassword());

        // Delete Account Section
        deleteAccountButton = new JButton("🗑 Delete Account");
        deleteAccountButton.setBackground(Color.BLACK);
        deleteAccountButton.setForeground(Color.RED);
        deleteAccountButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        deleteAccountButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        deleteAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteAccountButton.addActionListener(e -> deleteAccount());

        // Layout Components
        add(title);
        add(usernameLabel);
        add(Box.createVerticalStrut(10));
        add(oldPassLabel);
        add(oldPasswordField);
        add(Box.createVerticalStrut(10));
        add(newPassLabel);
        add(newPasswordField);
        add(Box.createVerticalStrut(15));
        add(changePasswordButton);
        add(Box.createVerticalStrut(25));
        add(deleteAccountButton);
    }

    private void changePassword() {
        String oldPass = new String(oldPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());

        if (oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both password fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Model call for business logic
            if (UserModel.changePassword(username, oldPass, newPass)) {
                JOptionPane.showMessageDialog(this, "Password changed successfully!");
                oldPasswordField.setText("");
                newPasswordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect current password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete your account? This will remove all your playlists and songs.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            // Model call for business logic
            UserModel.deleteAccount(username);
            
            JOptionPane.showMessageDialog(this, "Account deleted successfully.");
            // Close the dashboard window and redirect to login
            SwingUtilities.getWindowAncestor(this).dispose(); 
            LoginWindow.showLoginPage(); 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting account: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}