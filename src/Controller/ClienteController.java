package Controller;

import DAO.ClienteDAO;
import Model.Cliente;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/* @author Jhonny */
public class ClienteController {

    ClienteDAO clidao = new ClienteDAO();

    public void registrarCliente(String dniText,
            String nombre,
            String telefono,
            String direccion,
            String razon) {
        try {
            if (dniText.isEmpty()
                    || nombre.isEmpty()
                    || telefono.isEmpty()
                    || direccion.isEmpty()
                    || razon.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Todos los campos son obligatorios.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!dniText.matches("\\d{8}")) {
                JOptionPane.showMessageDialog(null,
                        "Ingresa un DNI válido de 8 dígitos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int opcion = JOptionPane.showConfirmDialog(null,
                    "¿Es RUC? (Sí = RUC | No = DNI)",
                    "Tipo de documento",
                    JOptionPane.YES_NO_OPTION);

            String documentoFinal;

            if (opcion == JOptionPane.YES_OPTION) {
                int randomDigit = (int) (Math.random() * 10);
                documentoFinal = "10" + dniText + randomDigit;
            } else {
                documentoFinal = dniText;
            }

            Cliente c = new Cliente();
            c.setDni(documentoFinal.trim());
            c.setNombre(nombre.trim());
            c.setTelefono(telefono.trim());
            c.setDireccion(direccion.trim());
            c.setRazon(razon.trim());

            if (clidao.guardar(c)) {
                JOptionPane.showMessageDialog(null,
                        "Cliente registrado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error al registrar el cliente.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El DNI debe ser un número válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarCliente(int id,
            String dniText,
            String nombre,
            String telefono,
            String direccion,
            String razon) {
        try {
            // Validar vacíos
            if (dniText.isEmpty()
                    || nombre.isEmpty()
                    || telefono.isEmpty()
                    || direccion.isEmpty()
                    || razon.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Todos los campos son obligatorios.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!dniText.matches("\\d{8}")) {
                JOptionPane.showMessageDialog(null,
                        "Ingresa un DNI válido de 8 dígitos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int opcion = JOptionPane.showConfirmDialog(null,
                    "¿Es RUC? (Sí = RUC | No = DNI)",
                    "Tipo de documento",
                    JOptionPane.YES_NO_OPTION);

            String documentoFinal;

            if (opcion == JOptionPane.YES_OPTION) {
                int randomDigit = (int) (Math.random() * 10);
                documentoFinal = "10" + dniText + randomDigit;
            } else {
                documentoFinal = dniText;
            }

            Cliente c = new Cliente();
            c.setId(id);
            c.setDni(documentoFinal.trim());
            c.setNombre(nombre.trim());
            c.setTelefono(telefono.trim());
            c.setDireccion(direccion.trim());
            c.setRazon(razon.trim());

            if (clidao.actualizar(c)) {
                JOptionPane.showMessageDialog(null,
                        "Cliente actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error al actualizar el cliente.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El DNI debe ser un número válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarCliente(int id) {
        int confirmar = JOptionPane.showConfirmDialog(null,
                "¿Seguro que deseas eliminar este cliente?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            if (clidao.eliminar(id)) {
                JOptionPane.showMessageDialog(null,
                        "Cliente eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error: Cliente registrado en una venta.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public Integer obtenerIdClienteSeleccionado(JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return null;
        }
        return Integer.valueOf(tabla.getValueAt(fila, 0).toString());
    }

    public void listarClientes(JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        List<Cliente> lista = clidao.listar();
        for (Cliente c : lista) {
            model.addRow(new Object[]{
                c.getId(),
                c.getDni(),
                c.getNombre(),
                c.getTelefono(),
                c.getDireccion(),
                c.getRazon(),
                c.getFecha()
            });
        }
    }
}
