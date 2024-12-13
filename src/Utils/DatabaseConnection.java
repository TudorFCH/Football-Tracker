package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/footballdb";
    private static final String USER = "root";
    private static final String PASSWORD = "!parola1234";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Explicitly load the JDBC driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found. Please include the JDBC driver in your classpath.");
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
