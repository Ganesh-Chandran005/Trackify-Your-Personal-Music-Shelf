package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.SongModel; // Using SongModel
import java.sql.SQLException;
import java.util.Vector;

public class SongPanel extends JPanel {

    private JTable songTable;
    private JTextField titleField, artistField, durationField, searchField;
    private JButton addButton, searchButton, refreshButton;
    private DefaultTableModel model;
    private int playlistId;
    private Color spotifyGreen = new Color(30, 215, 96);

    public SongPanel(String username, int playlistId) {
        this.playlistId = playlistId;

        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 18));

        // Top Panel (Search Bar)
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(24, 24, 24));
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        topPanel.add(searchLabel);

        searchField = new JTextField(20);
        topPanel.add(searchField);

        searchButton = new JButton("🔍 Search");
        searchButton.setBackground(Color.BLACK);
        searchButton.setForeground(spotifyGreen);
        searchButton.setBorder(BorderFactory.createLineBorder(spotifyGreen, 1));
        topPanel.add(searchButton);

        refreshButton = new JButton("⟳ Refresh");
        refreshButton.setBackground(Color.BLACK);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        topPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);

        // Center Panel (Table)
        model = new DefaultTableModel(new String[]{"Title", "Artist", "Duration"}, 0);
        songTable = new JTable(model);
        songTable.setBackground(new Color(25, 25, 25));
        songTable.setForeground(Color.WHITE);
        songTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        songTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(songTable);
        scrollPane.setBackground(new Color(25, 25, 25));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel (Add Song Form)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(24, 24, 24));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        bottomPanel.add(new JLabel("Title:"));
        titleField = new JTextField(10);
        bottomPanel.add(titleField);

        bottomPanel.add(new JLabel("Artist:"));
        artistField = new JTextField(10);
        bottomPanel.add(artistField);

        bottomPanel.add(new JLabel("Duration:"));
        durationField = new JTextField(5);
        bottomPanel.add(durationField);

        addButton = new JButton("Add Song");
        addButton.setBackground(Color.BLACK);
        addButton.setForeground(spotifyGreen);
        addButton.setBorder(BorderFactory.createLineBorder(spotifyGreen, 1));
        bottomPanel.add(addButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Load Songs Initially
        loadSongs("");

        // Button Actions - delegates to methods which call the Model
        addButton.addActionListener(e -> addSong());
        searchButton.addActionListener(e -> searchSongs());
        refreshButton.addActionListener(e -> loadSongs(""));
    }

    // Load Songs (uses Model to fetch data)
    private void loadSongs(String filter) {
        model.setRowCount(0); 
        try {
            // Model call
            Vector<Vector<Object>> songsData = SongModel.getSongs(playlistId, filter);
            
            for (Vector<Object> row : songsData) {
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading songs: " + e.getMessage());
        }
    }

    // Search Songs (uses Model through loadSongs)
    private void searchSongs() {
        String keyword = searchField.getText().trim();
        loadSongs(keyword);
    }

    // Add New Song (uses Model to persist data)
    private void addSong() {
        String title = titleField.getText().trim();
        String artist = artistField.getText().trim();
        String duration = durationField.getText().trim();

        if (title.isEmpty() || artist.isEmpty() || duration.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            // Model call
            SongModel.addSong(playlistId, title, artist, duration);

            JOptionPane.showMessageDialog(this, "Song added!");
            loadSongs("");
            titleField.setText("");
            artistField.setText("");
            durationField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding song: " + e.getMessage());
        }
    }
}