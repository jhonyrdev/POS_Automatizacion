package DAO;

import Model.*;
import config.Conexion;
import java.sql.*;
import java.util.*;

public class DetalleDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean guardar(Detalle detalle) {
        String sql = "INSERT INTO detalle (id_producto, cantidad, precio, id_venta) VALUES (?, ?, ?, ?)";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, detalle.getProducto().getId());
            ps.setInt(2, detalle.getCantidad());
            ps.setBigDecimal(3, detalle.getPrecio());
            ps.setInt(4, detalle.getVenta().getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar detalle: " + e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public boolean actualizar(Detalle detalle) {
        String sql = "UPDATE detalle SET id_producto=?, cantidad=?, precio=?, id_venta=? WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, detalle.getProducto().getId());
            ps.setInt(2, detalle.getCantidad());
            ps.setBigDecimal(3, detalle.getPrecio());
            ps.setInt(4, detalle.getVenta().getId());
            ps.setInt(5, detalle.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar detalle: " + e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM detalle WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar detalle: " + e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public List<Detalle> listar() {
        List<Detalle> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Detalle d = new Detalle();
                d.setId(rs.getInt("id"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setPrecio(rs.getBigDecimal("precio"));

                Producto p = new Producto();
                p.setId(rs.getInt("id_producto"));
                d.setProducto(p);

                Venta v = new Venta();
                v.setId(rs.getInt("id_venta"));
                d.setVenta(v);

                lista.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar detalles: " + e.toString());
        }
        return lista;
    }

    public Detalle listarPorId(int id) {
        Detalle d = null;
        String sql = "SELECT * FROM detalle WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                d = new Detalle();
                d.setId(rs.getInt("id"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setPrecio(rs.getBigDecimal("precio"));

                Producto p = new Producto();
                p.setId(rs.getInt("id_producto"));
                d.setProducto(p);

                Venta v = new Venta();
                v.setId(rs.getInt("id_venta"));
                d.setVenta(v);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar detalle: " + e.toString());
        }
        return d;
    }

    public int obtenerCantidadTotalPorVenta(int idVenta) {
        String sql = "SELECT SUM(cantidad) AS totalItems FROM detalle WHERE id_venta=?";
        int total = 0;

        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idVenta);
            rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("totalItems");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cantidad total: " + e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }
        return total;
    }

}
