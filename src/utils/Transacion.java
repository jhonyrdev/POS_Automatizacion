
package utils;

import config.Conexion;
import java.math.BigDecimal;
import java.sql.*;
import javax.swing.JTable;

/* @author Jhonny */
public class Transacion {
    public int registrarVentaCompleta(int idCliente, 
                                        int idVendedor, 
                                        BigDecimal total, 
                                        JTable tablaDetalle) {
        Connection con = null;
        CallableStatement csVenta = null;
        CallableStatement csDetalle = null;
        int idVenta = -1;

        try {
            con = Conexion.getConnection();
            con.setAutoCommit(false); 
            csVenta = con.prepareCall("{CALL crear_venta(?, ?, ?, ?)}");
            csVenta.setInt(1, idCliente);
            csVenta.setInt(2, idVendedor);
            csVenta.setBigDecimal(3, total);
            csVenta.registerOutParameter(4, Types.INTEGER);
            csVenta.execute();

            idVenta = csVenta.getInt(4); 

            csDetalle = con.prepareCall("{CALL agregar_detalle_venta(?, ?, ?, ?)}");

            for (int i = 0; i < tablaDetalle.getRowCount(); i++) {
                String codigo = tablaDetalle.getValueAt(i, 0).toString();
                int cantidad = Integer.parseInt(tablaDetalle.getValueAt(i, 2).toString());
                BigDecimal precio = new BigDecimal(tablaDetalle.getValueAt(i, 3).toString());

                int idProducto = obtenerIdProductoPorCodigo(con, codigo);
                if (idProducto == -1) {
                    throw new SQLException(
                            "No se encontró el producto con código: " + codigo);
                }

                csDetalle.setInt(1, idVenta);
                csDetalle.setInt(2, idProducto);
                csDetalle.setInt(3, cantidad);
                csDetalle.setBigDecimal(4, precio);
                csDetalle.execute();
            }

            con.commit();
            System.out.println(
                    "Venta registrada correctamente con ID: " + idVenta);

        } catch (SQLException e) {
            System.out.println(
                    "Error al registrar venta: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                    System.out.println(
                            "Transacción revertida (ROLLBACK)");
                } catch (SQLException ex) {
                    System.out.println(
                            "Error al hacer rollback: " + ex.getMessage());
                }
            }
        } finally {
            try {
                if (csDetalle != null) csDetalle.close();
                if (csVenta != null) csVenta.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return idVenta;
    }

    private int obtenerIdProductoPorCodigo(Connection con, String codigo) 
            throws SQLException {
        String sql = "SELECT id FROM productos WHERE codigo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }
}
