package DAO;

import Model.Cliente;
import config.Conexion;
import java.sql.*;
import java.util.*;

public class ClienteDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean guardar(Cliente c) {
        String sql = "INSERT INTO "
                + "clientes (dni, nombre, telefono, direccion, razon) "
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);

            if (con == null) {
                throw new SQLException("No se pudo obtener conexión");
            }

            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getDireccion());
            ps.setString(5, c.getRazon());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    public boolean actualizar(Cliente c) {
        String sql = "UPDATE clientes SET dni=?, nombre=?, telefono=?, direccion=?, razon=? WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);

            if (con == null) {
                throw new SQLException("No se pudo obtener conexión");
            }

            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getDireccion());
            ps.setString(5, c.getRazon());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM clientes WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);

            if (con == null) {
                throw new SQLException("No se pudo obtener conexión");
            }

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }

    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (con == null) {
                throw new SQLException("No se pudo obtener conexión");
            }

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setDni(rs.getString("dni"));
                c.setNombre(rs.getString("nombre"));
                c.setTelefono(rs.getString("telefono"));
                c.setDireccion(rs.getString("direccion"));
                c.setRazon(rs.getString("razon"));
                c.setFecha(rs.getTimestamp("fecha"));
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return lista;
    }

    public Cliente listarPorId(int id) {
        Cliente c = new Cliente();
        String sql = "SELECT * FROM clientes WHERE id=?";

        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            if (con == null) {
                throw new SQLException("No se pudo obtener conexión");
            }
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                c.setId(rs.getInt("id"));
                c.setDni(rs.getString("dni"));
                c.setNombre(rs.getString("nombre"));
                c.setTelefono(rs.getString("telefono"));
                c.setDireccion(rs.getString("direccion"));
                c.setRazon(rs.getString("razon"));
                c.setFecha(rs.getTimestamp("fecha"));
            }

        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

        return c;
    }

    //Metodos Extras
    public String obtenerNombrePorDni(String dni) {
        String nombre = null;
        String sql = "SELECT nombre FROM clientes WHERE dni = ?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, dni);
            rs = ps.executeQuery();
            if (rs.next()) {
                nombre = rs.getString("nombre");
            }
        } catch (SQLException e) {
            System.out.println(
                    "Error al buscar cliente por DNI: " + e.getMessage());
        }
        return nombre;
    }

    public int obtenerIdPorDni(String dni) {
        int id = -1;
        String sql = "SELECT id FROM clientes WHERE dni = ?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, dni);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ID del cliente: " + e.getMessage());
        }
        return id;
    }

}
