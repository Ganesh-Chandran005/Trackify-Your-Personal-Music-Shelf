package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

public class DBConnection {
    // Path to the SQLite database file. Must be correct relative to the project root.
    private static final String DB_FILE_PATH = "src/spotify_users.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE_PATH;

    public static Connection getConnection() throws SQLException {
        // Ensure the SQLite JDBC driver is loaded
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found. Add the library to your build path.");
            throw new SQLException("Database driver not available.", e);
        }
        
        // Check if the database file exists
        if (!new File(DB_FILE_PATH).exists()) {
             System.err.println("Database file not found at: " + new File(DB_FILE_PATH).getAbsolutePath());
        }
        
        // Attempt to connect
        return DriverManager.getConnection(URL);
    }
}