package Controller;

import DAO.*;
import Model.*;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/* @author Jhonny */
public class ProductoController {

    ProductoDAO proDao = new ProductoDAO();
    ProveedorDAO provDao = new ProveedorDAO();

    public void listarProductos(JTable tableProducto) {
        List<Producto> lista = proDao.listar();
        DefaultTableModel modelo = (DefaultTableModel) tableProducto.getModel();
        modelo.setRowCount(0);

        for (Producto p : lista) {
            Object[] row = {
                p.getId(),
                p.getCodigo(),
                p.getNombre(),
                p.getStock(),
                p.getPrecio(),
                p.getProveedor().getNombre()
            };
            modelo.addRow(row);
        }

        tableProducto.setModel(modelo);
    }

    public void registrarProducto(
            String nombre,
            String stockText,
            String precioText,
            String nombreProveedorSeleccionado) {
        try {
            if (nombre.isEmpty()
                    || stockText.isEmpty()
                    || precioText.isEmpty()
                    || nombreProveedorSeleccionado.equals(
                            "---- SELECCIONA AL PROVEEDOR -----")) {

                JOptionPane.showMessageDialog(null,
                        "Todos los campos son obligatorios, incluido el proveedor.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int stock = Integer.parseInt(stockText.trim());
            BigDecimal precio = new BigDecimal(precioText.trim());

            String codigoGenerado = proDao.generarCodigoProducto();

            Proveedor proveedor = 
                    provDao.buscarPorNombre(nombreProveedorSeleccionado);
            if (proveedor == null) {
                JOptionPane.showMessageDialog(null,
                        "El proveedor seleccionado no existe.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Producto p = new Producto();
            p.setCodigo(codigoGenerado);
            p.setNombre(nombre.trim());
            p.setStock(stock);
            p.setPrecio(precio);
            p.setProveedor(proveedor);

            if (proDao.guardar(p)) {
                JOptionPane.showMessageDialog(null,
                        "Producto registrado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error al registrar el producto.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Stock y precio deben ser valores numéricos válidos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarProducto(int id,
                               String codigo,
                               String nombre,
                               String stockText,
                               String precioText,
                               String nombreProveedorSeleccionado) {
    try {
        if (codigo.isEmpty()
                || nombre.isEmpty()
                || stockText.isEmpty()
                || precioText.isEmpty()
                || nombreProveedorSeleccionado.equals(
                        "---- SELECCIONA AL PROVEEDOR -----")) {

            JOptionPane.showMessageDialog(null,
                    "Todos los campos son obligatorios, incluido el proveedor.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int stock = Integer.parseInt(stockText.trim());
        BigDecimal precio = new BigDecimal(precioText.trim());

        Proveedor proveedor = provDao.buscarPorNombre(nombreProveedorSeleccionado);
        if (proveedor == null) {
            JOptionPane.showMessageDialog(null,
                    "El proveedor seleccionado no existe.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear el objeto producto con los nuevos datos
        Producto p = new Producto();
        p.setId(id);
        p.setCodigo(codigo.trim());
        p.setNombre(nombre.trim());
        p.setStock(stock);
        p.setPrecio(precio);
        p.setProveedor(proveedor);

        if (proDao.actualizar(p)) {
            JOptionPane.showMessageDialog(null,
                    "Producto actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null,
                    "Error al actualizar el producto.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null,
                "Stock y precio deben ser valores numéricos válidos.",
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    public void eliminarProducto(int id) {
        int confirmar = JOptionPane.showConfirmDialog(null,
                "¿Seguro que deseas eliminar este producto?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            if (proDao.eliminar(id)) {
                JOptionPane.showMessageDialog(null,
                        "Producto eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error: Producto registrado en una venta.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public Integer obtenerIdProductoSeleccionado(JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        return Integer.valueOf(tabla.getValueAt(fila, 0).toString());
    }
}
