package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {

    public static boolean authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public static boolean registerUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Handle case where username already exists due to unique constraint
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new SQLException("Username already exists.", e);
            }
            throw e;
        }
    }

    public static boolean changePassword(String username, String oldPass, String newPass) throws SQLException {
        // Authenticate the old password first
        if (!authenticate(username, oldPass)) {
            return false;
        }

        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(sql)) {

            updateStmt.setString(1, newPass);
            updateStmt.setString(2, username);
            return updateStmt.executeUpdate() > 0;
        }
    }

    public static void deleteAccount(String username) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // 1. Delete all songs associated with the user's playlists
            String delSongsSQL = "DELETE FROM songs WHERE playlist_id IN (SELECT id FROM playlists WHERE username = ?)";
            try (PreparedStatement delSongs = conn.prepareStatement(delSongsSQL)) {
                delSongs.setString(1, username);
                delSongs.executeUpdate();
            }

            // 2. Delete all playlists
            String delPlaylistsSQL = "DELETE FROM playlists WHERE username = ?";
            try (PreparedStatement delPlaylists = conn.prepareStatement(delPlaylistsSQL)) {
                delPlaylists.setString(1, username);
                delPlaylists.executeUpdate();
            }

            // 3. Delete user account
            String delUserSQL = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement delUser = conn.prepareStatement(delUserSQL)) {
                delUser.setString(1, username);
                delUser.executeUpdate();
            }

            conn.commit(); // Commit transaction
        }
    }
}