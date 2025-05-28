package db;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection {
    public static Connection conn = null;
    private static final String URL = "jdbc:mysql://localhost:3306/e-gringotts";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Establishes a connection to the database
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (conn == null) {
            Class.forName("com.mysql.jdbc.Driver");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
