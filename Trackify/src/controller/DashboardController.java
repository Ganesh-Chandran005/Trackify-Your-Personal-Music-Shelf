package controller;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import view.PlaylistPanel;
import view.ProfilePanel;
import view.LoginWindow;
import view.DashboardWindow;

public class DashboardController {
    private final DashboardWindow dashboardView;
    private final String username;

    public DashboardController(DashboardWindow dashboardView, String username) {
        this.dashboardView = dashboardView;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    
    // Utility method to change the main content panel in the dashboard view
    private void setContentPanel(JPanel panel) {
        JPanel contentPanel = dashboardView.getContentPanel();
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showHome() {
        // Restore the original welcome/home view components
        JPanel contentPanel = dashboardView.getContentPanel();
        contentPanel.removeAll();
        contentPanel.add(dashboardView.getWelcomeLabel(), BorderLayout.NORTH);
        contentPanel.add(dashboardView.getHomePanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showPlaylists() {
        // Replace content with the Playlist view
        setContentPanel(new PlaylistPanel(username));
    }

    public void showProfile() {
        // Replace content with the Profile view
        setContentPanel(new ProfilePanel(username));
    }

    public void logout() {
        JOptionPane.showMessageDialog(dashboardView.getFrame(), "You have been logged out.");
        dashboardView.getFrame().dispose(); 
        LoginWindow.showLoginPage(); 
    }
}