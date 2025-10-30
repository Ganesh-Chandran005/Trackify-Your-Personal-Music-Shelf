package view;

import javax.swing.*;
import java.awt.*;
import model.PlaylistModel; // Using PlaylistModel
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class PlaylistPanel extends JPanel {
    private String username;
    private JList<String> playlistList;
    private DefaultListModel<String> playlistModel;
    // Stores the database ID corresponding to the list item
    private List<Integer> playlistIds; 
    private JTextField playlistNameField, descriptionField;
    private JButton addButton, deleteButton;
    private Color spotifyGreen = new Color(30, 215, 96);

    public PlaylistPanel(String username) {
        this.username = username;

        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 18));

        // Top Label
        JLabel title = new JLabel("🎧 Your Playlists", SwingConstants.CENTER);
        title.setForeground(spotifyGreen);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Playlist List
        playlistModel = new DefaultListModel<>();
        playlistList = new JList<>(playlistModel);
        playlistList.setBackground(new Color(24, 24, 24));
        playlistList.setForeground(Color.WHITE);
        playlistList.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(playlistList);
        scrollPane.setBorder(BorderFactory.createLineBorder(spotifyGreen, 1));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel (Add Playlist)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(24, 24, 24));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        bottomPanel.add(new JLabel("Name:"));
        playlistNameField = new JTextField(10);
        bottomPanel.add(playlistNameField);

        bottomPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField(10);
        bottomPanel.add(descriptionField);

        addButton = new JButton("Add Playlist");
        addButton.setBackground(Color.BLACK);
        addButton.setForeground(spotifyGreen);
        addButton.setBorder(BorderFactory.createLineBorder(spotifyGreen, 1));
        bottomPanel.add(addButton);

        deleteButton = new JButton("Delete Playlist");
        deleteButton.setBackground(Color.BLACK);
        deleteButton.setForeground(Color.RED);
        deleteButton.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        bottomPanel.add(deleteButton);

        add(bottomPanel, BorderLayout.SOUTH);

        loadPlaylists();

        // Add new playlist - delegates to Model
        addButton.addActionListener(e -> addPlaylist());

        // Delete playlist - delegates to Model
        deleteButton.addActionListener(e -> deletePlaylist());

        // Open songs on double-click - View logic
        playlistList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { 
                    int selectedIndex = playlistList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        int playlistId = playlistIds.get(selectedIndex);
                        // Open new frame with SongPanel (View)
                        JFrame songFrame = new JFrame("Songs in Playlist");
                        songFrame.setSize(600, 400);
                        songFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        songFrame.add(new SongPanel(username, playlistId)); 
                        songFrame.setVisible(true);
                    }
                }
            }
        });
    }

    private void loadPlaylists() {
        playlistModel.clear();
        playlistIds = new ArrayList<>();
        try {
            // Model call to retrieve data
            List<Entry<Integer, String>> playlists = PlaylistModel.getPlaylists(username);
            
            for (Entry<Integer, String> entry : playlists) {
                playlistIds.add(entry.getKey());
                playlistModel.addElement(entry.getValue());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading playlists: " + e.getMessage());
        }
    }

    private void addPlaylist() {
        String name = playlistNameField.getText().trim();
        String desc = descriptionField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a playlist name!");
            return;
        }

        try {
            // Model call to persist data
            PlaylistModel.addPlaylist(username, name, desc); 

            JOptionPane.showMessageDialog(this, "Playlist added!");
            loadPlaylists();
            playlistNameField.setText("");
            descriptionField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding playlist: " + e.getMessage());
        }
    }

    private void deletePlaylist() {
        int selectedIndex = playlistList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a playlist to delete.");
            return;
        }

        int playlistId = playlistIds.get(selectedIndex);

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this playlist and all its songs?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            // Model call to delete data
            PlaylistModel.deletePlaylist(playlistId);

            JOptionPane.showMessageDialog(this, "Playlist deleted!");
            loadPlaylists();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting playlist: " + e.getMessage());
        }
    }
}