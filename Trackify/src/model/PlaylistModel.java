package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class PlaylistModel {

    // Returns a list of [Playlist ID, Playlist Name] pairs
    public static List<Entry<Integer, String>> getPlaylists(String username) throws SQLException {
        List<Entry<Integer, String>> playlists = new ArrayList<>();
        String sql = "SELECT id, name FROM playlists WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                playlists.add(new SimpleEntry<>(rs.getInt("id"), rs.getString("name")));
            }
        }
        return playlists;
    }

    public static boolean addPlaylist(String username, String name, String description) throws SQLException {
        String sql = "INSERT INTO playlists (username, name, description) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, name);
            stmt.setString(3, description);
            return stmt.executeUpdate() > 0;
        }
    }

    public static void deletePlaylist(int playlistId) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // 1. Delete all songs in the playlist
            String delSongs = "DELETE FROM songs WHERE playlist_id = ?";
            try (PreparedStatement delSongsStmt = conn.prepareStatement(delSongs)) {
                delSongsStmt.setInt(1, playlistId);
                delSongsStmt.executeUpdate();
            }

            // 2. Delete the playlist itself
            String delPlaylist = "DELETE FROM playlists WHERE id = ?";
            try (PreparedStatement delPlaylistStmt = conn.prepareStatement(delPlaylist)) {
                delPlaylistStmt.setInt(1, playlistId);
                delPlaylistStmt.executeUpdate();
            }
            
            conn.commit(); // Commit transaction
        }
    }
}