package DAO;

import Model.Proveedor;
import config.Conexion;
import java.sql.*;
import java.util.*;

public class ProveedorDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean guardar(Proveedor p) {
        String sql = "INSERT INTO "
                + "proveedor (ruc, nombre, telefono, direccion, razon) "
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getRuc());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getDireccion());
            ps.setString(5, p.getRazon());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public boolean actualizar(Proveedor p) {
        String sql = "UPDATE proveedor "
                + "SET ruc=?, nombre=?, telefono=?, direccion=?, razon=? "
                + "WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getRuc());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getDireccion());
            ps.setString(5, p.getRazon());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM proveedor WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public List<Proveedor> listar() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedor";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Proveedor p = new Proveedor();
                p.setId(rs.getInt("id"));
                p.setRuc(rs.getString("ruc"));
                p.setNombre(rs.getString("nombre"));
                p.setTelefono(rs.getString("telefono"));
                p.setDireccion(rs.getString("direccion"));
                p.setRazon(rs.getString("razon"));
                p.setFecha(rs.getTimestamp("fecha"));
                lista.add(p);
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return lista;
    }

    public Proveedor listarPorId(int id) {
        Proveedor p = new Proveedor();
        String sql = "SELECT * FROM proveedor WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                p.setId(rs.getInt("id"));
                p.setRuc(rs.getString("ruc"));
                p.setNombre(rs.getString("nombre"));
                p.setTelefono(rs.getString("telefono"));
                p.setDireccion(rs.getString("direccion"));
                p.setRazon(rs.getString("razon"));
                p.setFecha(rs.getTimestamp("fecha"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return p;
    }

    //Metodos extras
    public Proveedor buscarPorNombre(String nombre) {
        Proveedor p = null;
        String sql = "SELECT * FROM proveedor WHERE nombre = ?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            if (rs.next()) {
                p = new Proveedor();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            System.out.println(""
                    + "Error al buscar proveedor por nombre: " + ex.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
        return p;
    }

}
