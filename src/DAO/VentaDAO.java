
package DAO;

import Model.*;
import config.Conexion;
import java.sql.*;
import java.util.*;

public class VentaDAO {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean guardar(Venta venta) {
        String sql = "INSERT INTO ventas (id_cliente, id_vendedor, total) VALUES (?, ?, ?)";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, venta.getCliente().getId());
            ps.setInt(2, venta.getVendedor().getId());
            ps.setBigDecimal(3, venta.getTotal());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar venta: " + e.toString());
            return false;
        } finally {
            try { con.close(); } catch (SQLException e) { System.out.println(e.toString()); }
        }
    }

    public boolean actualizar(Venta venta) {
        String sql = "UPDATE ventas SET id_cliente=?, id_vendedor=?, total=? WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, venta.getCliente().getId());
            ps.setInt(2, venta.getVendedor().getId());
            ps.setBigDecimal(3, venta.getTotal());
            ps.setInt(4, venta.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar venta: " + e.toString());
            return false;
        } finally {
            try { con.close(); } catch (SQLException e) { System.out.println(e.toString()); }
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM ventas WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar venta: " + e.toString());
            return false;
        } finally {
            try { con.close(); } catch (SQLException e) { System.out.println(e.toString()); }
        }
    }

    public List<Venta> listar() {
    List<Venta> lista = new ArrayList<>();
    String sql = """
        SELECT v.id, v.total, v.fecha,
               c.id AS id_cliente, c.nombre AS nombre_cliente,
               u.id AS id_vendedor, u.nombre AS nombre_vendedor
        FROM ventas v
        INNER JOIN clientes c ON v.id_cliente = c.id
        INNER JOIN usuarios u ON v.id_vendedor = u.id
    """;

    try {
        con = Conexion.getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Venta v = new Venta();
            v.setId(rs.getInt("id"));
            v.setTotal(rs.getBigDecimal("total"));
            v.setFecha(rs.getTimestamp("fecha"));

            Cliente c = new Cliente();
            c.setId(rs.getInt("id_cliente"));
            c.setNombre(rs.getString("nombre_cliente"));
            v.setCliente(c);

            Usuario u = new Usuario();
            u.setId(rs.getInt("id_vendedor"));
            u.setNombre(rs.getString("nombre_vendedor"));
            v.setVendedor(u);

            lista.add(v);
        }
    } catch (SQLException e) {
        System.out.println("Error al listar ventas: " + e.getMessage());
    }

    return lista;
}


    public Venta listarPorId(int id) {
        Venta v = null;
        String sql = "SELECT * FROM ventas WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                v = new Venta();
                v.setId(rs.getInt("id"));
                v.setTotal(rs.getBigDecimal("total"));
                v.setFecha(rs.getTimestamp("fecha"));

                Cliente c = new Cliente();
                c.setId(rs.getInt("id_cliente"));
                v.setCliente(c);

                Usuario u = new Usuario();
                u.setId(rs.getInt("id_vendedor"));
                v.setVendedor(u);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar venta: " + e.toString());
        }
        return v;
    }
}
