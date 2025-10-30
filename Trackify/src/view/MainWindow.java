package view;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Spotify Tracker");
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Spotify colors
            Color spotifyDark = new Color(18, 18, 18);
            Color spotifyGreen = new Color(30, 215, 96);

            frame.getContentPane().setBackground(spotifyDark);

            // Title label
            JLabel label = new JLabel("Welcome to Spotify Tracker!", SwingConstants.CENTER);
            label.setForeground(spotifyGreen);
            label.setFont(new Font("SansSerif", Font.BOLD, 26));
            label.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
            frame.add(label, BorderLayout.NORTH);

            // Center panel for button
            JPanel centerPanel = new JPanel();
            centerPanel.setBackground(spotifyDark);
            centerPanel.setLayout(new GridBagLayout()); 

            // Login Button
            JButton loginButton = new JButton("Login with Spotify");
            loginButton.setBackground(Color.BLACK);
            loginButton.setForeground(spotifyGreen);
            loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
            loginButton.setFocusPainted(false);
            loginButton.setBorder(BorderFactory.createLineBorder(spotifyGreen, 2));
            loginButton.setPreferredSize(new Dimension(200, 50));

            // Hover effect
            loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    loginButton.setForeground(spotifyGreen.brighter());
                    loginButton.setBackground(new Color(24, 24, 24));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    loginButton.setForeground(spotifyGreen);
                    loginButton.setBackground(Color.BLACK);
                }
            });

            // Click action
            loginButton.addActionListener(e -> { 
                frame.dispose(); 
                LoginWindow.showLoginPage(); // View navigation
            });

            // Add button to center panel
            centerPanel.add(loginButton);
            frame.add(centerPanel, BorderLayout.CENTER);
            
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}