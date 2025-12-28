
package Controller;

import DAO.ProveedorDAO;
import Model.Proveedor;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProveedorController {
    private final ProveedorDAO prDao = new ProveedorDAO();

    public void listarProveedores(JTable tableProveedor) {
        List<Proveedor> lista = prDao.listar();
        DefaultTableModel modelo = (DefaultTableModel) tableProveedor.getModel();
        modelo.setRowCount(0);

        for (Proveedor p : lista) {
            Object[] row = {
                p.getId(),
                p.getRuc(),
                p.getNombre(),
                p.getTelefono(),
                p.getDireccion(),
                p.getRazon()
            };
            modelo.addRow(row);
        }

        tableProveedor.setModel(modelo);
    }
    
    public void registrarProveedor(String rucText, 
                                   String nombre, 
                                   String telefono, 
                                   String direccion, 
                                   String razon) {
        try {
            if (rucText.isEmpty() || nombre.isEmpty() || telefono.isEmpty() 
                || direccion.isEmpty() || razon.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                        "Todos los campos son obligatorios.", 
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Proveedor p = new Proveedor();
            p.setRuc(rucText.trim());
            p.setNombre(nombre.trim());
            p.setTelefono(telefono.trim());
            p.setDireccion(direccion.trim());
            p.setRazon(razon.trim());

            if (prDao.guardar(p)) {
                JOptionPane.showMessageDialog(null, 
                        "Proveedor registrado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, 
                        "Error al registrar el proveedor.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                    "El RUC debe ser un número válido.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarProveedor(int id, 
                                    String rucText, 
                                    String nombre, 
                                    String telefono, 
                                    String direccion, 
                                    String razon) {
        try {
            if (rucText.isEmpty() || nombre.isEmpty() || telefono.isEmpty() 
                || direccion.isEmpty() || razon.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                        "Todos los campos son obligatorios.", 
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Proveedor p = new Proveedor();
            p.setId(id);
            p.setRuc(rucText.trim());
            p.setNombre(nombre.trim());
            p.setTelefono(telefono.trim());
            p.setDireccion(direccion.trim());
            p.setRazon(razon.trim());

            if (prDao.actualizar(p)) {
                JOptionPane.showMessageDialog(null, 
                        "Proveedor actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, 
                        "Error al actualizar el proveedor.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                    "El RUC debe ser un número válido.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarProveedor(int id) {
        int confirmar = JOptionPane.showConfirmDialog(null,
                "¿Seguro que deseas eliminar este proveedor?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            if (prDao.eliminar(id)) {
                JOptionPane.showMessageDialog(null, 
                        "Proveedor eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, 
                        "Error: Proveedor relacionado a un registro.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Obtener ID del proveedor seleccionado en la tabla
    public Integer obtenerIdProveedorSeleccionado(JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        return Integer.valueOf(tabla.getValueAt(fila, 0).toString());
    }
}
