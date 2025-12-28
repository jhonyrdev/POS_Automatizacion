package DAO;

import Model.*;
import config.Conexion;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ProductoDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean guardar(Producto p) {
        String sql = "INSERT INTO productos (codigo, nombre, id_proveedor, stock, precio) VALUES (?, ?, ?, ?, ?)";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getProveedor().getId());
            ps.setInt(4, p.getStock());
            ps.setBigDecimal(5, p.getPrecio());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al guardar producto: " + ex.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET codigo=?, nombre=?, id_proveedor=?, stock=?, precio=? WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getProveedor().getId());
            ps.setInt(4, p.getStock());
            ps.setBigDecimal(5, p.getPrecio());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al actualizar producto: " + ex.toString());
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
        String sql = "DELETE FROM productos WHERE id=?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al eliminar producto: " + ex.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = """
            SELECT p.id, p.codigo, p.nombre, p.stock, p.precio, p.fecha,
                   pr.id AS id_proveedor, pr.nombre AS nombre_proveedor
            FROM productos p
            INNER JOIN proveedor pr ON p.id_proveedor = pr.id
            ORDER BY p.id DESC
        """;
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setStock(rs.getInt("stock"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setFecha(rs.getTimestamp("fecha"));

                Proveedor prov = new Proveedor();
                prov.setId(rs.getInt("id_proveedor"));
                prov.setNombre(rs.getString("nombre_proveedor"));
                p.setProveedor(prov);

                lista.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar productos: " + ex.toString());
        }
        return lista;
    }

    public Producto listarPorId(int id) {
        Producto p = null;
        String sql = """
            SELECT p.id, p.codigo, p.nombre, p.stock, p.precio, p.fecha,
                   pr.id AS id_proveedor, pr.nombre AS nombre_proveedor
            FROM productos p
            INNER JOIN proveedor pr ON p.id_proveedor = pr.id
            WHERE p.id = ?
        """;
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                p = new Producto();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setStock(rs.getInt("stock"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setFecha(rs.getTimestamp("fecha"));

                Proveedor prov = new Proveedor();
                prov.setId(rs.getInt("id_proveedor"));
                prov.setNombre(rs.getString("nombre_proveedor"));
                p.setProveedor(prov);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar producto por ID: " + ex.toString());
        }
        return p;
    }

    //Metodos Extras
    public String generarCodigoProducto() {
        String codigo;
        int intentos = 0;
        do {
            codigo = "PRO" + generarNumerosAleatorios(10);
            intentos++;
            if (intentos > 10) {
                throw new RuntimeException(
                        "No se pudo generar un código único después de 10 intentos.");
            }
        } while (existeCodigoEnBD(codigo));
        return codigo;
    }

    private boolean existeCodigoEnBD(String codigo) {
        String sql = "SELECT COUNT(*) FROM productos WHERE codigo = ?";
        boolean existe = false;

        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            rs = ps.executeQuery();

            if (rs.next()) {
                existe = rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            System.out.println(
                    "Error al verificar código existente: "
                    + ex.toString());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }

        return existe;
    }

    private String generarNumerosAleatorios(int longitud) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(10));
        }
        return sb.toString();
    }

    public Producto buscarPorNombre(String nombre) {
        Producto producto = null;
        String sql = "SELECT * FROM productos WHERE nombre = ?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            if (rs.next()) {
                producto = new Producto();
                producto.setCodigo(rs.getString("codigo"));
                producto.setNombre(rs.getString("nombre"));
                producto.setStock(rs.getInt("stock"));
                producto.setPrecio(rs.getBigDecimal("precio"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar producto por nombre: " + e.getMessage());
        }
        return producto;
    }

    public BigDecimal obtenerPrecioPorNombre(String nombreProducto) {
        BigDecimal precio = null;
        String sql = "SELECT precio FROM productos WHERE nombre = ?";
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreProducto);
            rs = ps.executeQuery();
            if (rs.next()) {
                precio = rs.getBigDecimal("precio");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener precio: " + e.getMessage());
        }
        return precio;
    }

}
