package DAO;

import Model.Usuario;
import config.Conexion;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.*;

public class UsuarioDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean guardar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, correo, pass) VALUES (?, ?, ?)";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());

            String hashed = BCrypt.hashpw(u.getPass(), BCrypt.gensalt());
            ps.setString(3, hashed);

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

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nombre=?, correo=?, pass=? WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getPass());
            ps.setInt(4, u.getId());
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
        String sql = "DELETE FROM usuarios WHERE id=?";
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

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setPass(rs.getString("pass"));
                lista.add(u);
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return lista;
    }

    public Usuario listarPorId(int id) {
        Usuario u = new Usuario();
        String sql = "SELECT * FROM usuarios WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setPass(rs.getString("pass"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return u;
    }

    //Metodos Extras
    public Usuario logeo(String correo, String pass) {
        Usuario u = new Usuario();
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            rs = ps.executeQuery();

            if (rs.next()) {
                String hash = rs.getString("pass");
                if (BCrypt.checkpw(pass, hash)) {
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setCorreo(rs.getString("correo"));
                    u.setPass(hash);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
        return u;
    }

}
