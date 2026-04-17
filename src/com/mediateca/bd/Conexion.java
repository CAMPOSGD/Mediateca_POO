
/*
package utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private Connection conexion = null;
    private final String url = "jdbc:mysql://localhost:3306/mediateca_udb";
    private final String usuario = "root";
    private final String password = "1234"; // O la clave que uses en MySQL

    public Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, usuario, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el Driver de MySQL.");
        } catch (SQLException e) {
            System.out.println("Error: Fallo al conectar a la base de datos: " + e.getMessage());
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar: " + e.getMessage());
        }
    }
}

*/

package utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    // Asegúrate de que el nombre "mediateca_udb" sea el correcto
    private static final String URL = "jdbc:mysql://localhost:3306/mediateca_udb";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public Connection getConexion() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("❌ Error crítico de conexión: " + e.getMessage());
        }
        return conn;
    }

    public void cerrarConexion() {
        // Método de soporte
    }

    // --- CÓDIGO DE PRUEBA ---
    // Puedes darle "Run" directamente a este archivo para probar la conexión
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de conexión...");
        Conexion db = new Conexion();

        try (Connection cn = db.getConexion()) {
            if (cn != null) {
                System.out.println("✅ ¡CONEXIÓN EXITOSA A MYSQL!");

                // Vamos a intentar contar cuántos libros tienes
                String sql = "SELECT COUNT(*) AS total FROM libro";
                try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                    if (rs.next()) {
                        int total = rs.getInt("total");
                        System.out.println("✅ Query ejecutado correctamente.");
                        System.out.println("👉 Tienes " + total + " libros registrados en la tabla 'libro'.");

                        if (total == 0) {
                            System.out.println("💡 Nota: Tu tabla está vacía. Es normal que no veas datos al buscar.");
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("❌ La conexión funciona, pero el query falló. Revisa el nombre de la tabla.");
                    ex.printStackTrace();
                }
            } else {
                System.out.println("❌ No se pudo conectar a la base de datos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}