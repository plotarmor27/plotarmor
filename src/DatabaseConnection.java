import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import java.net.ConnectException;
import java.sql.*;

/**
 * Establishes a connection to the MySQL database.
 */
public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://autorack.proxy.rlwy.net:49790/plotarmor";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "nCNEUrvtPOwqVusAoYfhoRjEzbJctvki";

    public static Connection connect() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Create and return a connection to the database
            return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {

            System.out.println("Fehler beim Herstellen der Verbindung zur Datenbank!");
            return null;
        }
    }

}
