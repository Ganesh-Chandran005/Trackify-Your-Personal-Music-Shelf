package view;

import javax.swing.*;
import java.awt.*;
import controller.DashboardController;

public class DashboardWindow {

    private JFrame frame;
    private JPanel contentPanel;
    private JLabel welcomeLabel;
    private JPanel homePanel;
    private DashboardController controller;
    
    private static final Color spotifyGreen = new Color(30, 215, 96);
    private static final Color spotifyDark = new Color(18, 18, 18);
    private static final Color spotifyGray = new Color(24, 24, 24);

    public static void showDashboard(String username) {
        SwingUtilities.invokeLater(() -> new DashboardWindow(username).initialize());
    }
    
    public DashboardWindow(String username) {
        this.frame = new JFrame("Spotify Tracker Dashboard");
        // Initialize Controller, passing the View (this) and data (username)
        this.controller = new DashboardController(this, username); 
    }

    private void initialize() {
        frame.setSize(950, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(spotifyDark);

        // Sidebar Panel setup
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(spotifyGray);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Spotify Logo label
        JLabel logoLabel = createLogoLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        sidebar.add(logoLabel);

        // Sidebar Buttons
        JButton homeBtn = createSidebarButton("🏠 Home", spotifyGreen);
        JButton playlistBtn = createSidebarButton("🎵 Playlists", spotifyGreen);
        JButton profileBtn = createSidebarButton("👤 Profile", spotifyGreen);
        JButton logoutBtn = createSidebarButton("🚪 Logout", Color.RED);

        sidebar.add(homeBtn);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(playlistBtn);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(profileBtn);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutBtn);

        // Main Content Area
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(spotifyDark);
        
        setupHomeView(controller.getUsername());

        // Button Actions delegated to the Controller
        homeBtn.addActionListener(e -> controller.showHome());
        playlistBtn.addActionListener(e -> controller.showPlaylists());
        profileBtn.addActionListener(e -> controller.showProfile());
        logoutBtn.addActionListener(e -> controller.logout());

        // Add Panels to Frame
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    
    private void setupHomeView(String username) {
        welcomeLabel = new JLabel("Welcome, " + username + " 🎧", SwingConstants.CENTER);
        welcomeLabel.setForeground(spotifyGreen);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JTextArea infoArea = new JTextArea(
            "🎶 Your Spotify Tracker Dashboard 🎶\n\n" +
            "• View and manage your playlists\n" +
            "• Customize your preferences\n\n" +
            "Click options on the left sidebar to navigate!"
            +"\nAND MANY MORE FUN UPCOMING FEATURES!!"
        );
        infoArea.setEditable(false);
        infoArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoArea.setForeground(Color.WHITE);
        infoArea.setBackground(spotifyDark);
        infoArea.setMargin(new Insets(20, 50, 20, 50));
        
        homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(spotifyDark);
        homePanel.add(infoArea, BorderLayout.CENTER);

        contentPanel.add(welcomeLabel, BorderLayout.NORTH);
        contentPanel.add(homePanel, BorderLayout.CENTER);
    }
    
    // Helper method to create Logo Label
    private JLabel createLogoLabel() {
        JLabel logoLabel;
        try {
            // Path assumption: assets/spotify-logo.png exists in project root
            ImageIcon logoIcon = new ImageIcon("assets/spotify-logo.png");
            Image img = logoIcon.getImage().getScaledInstance(140, 40, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            logoLabel = new JLabel("Spotify Tracker", SwingConstants.CENTER);
            logoLabel.setForeground(spotifyGreen);
            logoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        }
        return logoLabel;
    }

    // Helper method to Create Button
    private JButton createSidebarButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(160, 40));
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(Color.BLACK);
        btn.setForeground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(color, 2));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(25, 25, 25));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.BLACK);
            }
        });
        return btn;
    }
    
    // Getters for the Controller to interact with the View
    public JFrame getFrame() { return frame; }
    public JPanel getContentPanel() { return contentPanel; }
    public JLabel getWelcomeLabel() { return welcomeLabel; }
    public JPanel getHomePanel() { return homePanel; }

    //Testing
    public static void main(String[] args) {
        showDashboard("ABC");
    }
}