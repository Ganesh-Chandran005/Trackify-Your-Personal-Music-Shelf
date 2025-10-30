package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class SongModel {

    // Retrieves songs from a playlist, optionally filtering by title or artist
    public static Vector<Vector<Object>> getSongs(int playlistId, String filter) throws SQLException {
        Vector<Vector<Object>> songsData = new Vector<>();
        String sql;
        
        // Build query based on whether a filter keyword is provided
        if (filter == null || filter.trim().isEmpty()) {
            sql = "SELECT title, artist, duration FROM songs WHERE playlist_id = ?";
        } else {
            sql = "SELECT title, artist, duration FROM songs WHERE playlist_id = ? AND (title LIKE ? OR artist LIKE ?)";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, playlistId);
            if (filter != null && !filter.trim().isEmpty()) {
                String likeFilter = "%" + filter + "%";
                stmt.setString(2, likeFilter);
                stmt.setString(3, likeFilter);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("title"));
                row.add(rs.getString("artist"));
                row.add(rs.getString("duration"));
                songsData.add(row);
            }
        }
        return songsData;
    }

    public static boolean addSong(int playlistId, String title, String artist, String duration) throws SQLException {
        String sql = "INSERT INTO songs (playlist_id, title, artist, duration) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playlistId);
            stmt.setString(2, title);
            stmt.setString(3, artist);
            stmt.setString(4, duration);
            return stmt.executeUpdate() > 0;
        }
    }
}