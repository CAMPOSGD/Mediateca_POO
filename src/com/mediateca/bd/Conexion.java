package utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/mediateca_udb";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConexion() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error crítico de conexión: " + e.getMessage());
        }
        return conn;
    }
}