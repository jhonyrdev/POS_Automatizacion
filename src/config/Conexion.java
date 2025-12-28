
package config;

import java.sql.*;

public class Conexion {

    private static Connection con = null;

    public static Connection getConnection() {
        if (con != null) {
            try {
                if (!con.isClosed()) {
                    return con;
                }
            } catch (SQLException e) {
            }
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://bua0vrplvw27gjkzu3uo-mysql.services.clever-cloud.com:3306/bua0vrplvw27gjkzu3uo?useSSL=false&serverTimezone=UTC";
            String user = "uev2qvhyw5j0qrmx";
            String pass = "1LxajhJGxF2rHsdMfkko";

            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexión exitosa con BD en Clever Cloud");

        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver MySQL");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la BD");
        }

        return con;
    }

    public static void cerrarConexion() {
        if (con != null) {
            try {
                con.close();
                con = null;
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
