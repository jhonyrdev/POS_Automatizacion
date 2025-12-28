
package utils;

import javax.swing.*;

public class limpiarCampos {

    public static void limpiarCliente(JTextField txtDni, 
                                        JTextField txtNombre, 
                                        JTextField txtTelefono, 
                                        JTextField txtDireccion,
                                        JTextField txtRazon) {
        txtDni.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtRazon.setText("");
    }

    public static void limpiarProveedor(JTextField txtRuc, 
                                        JTextField txtNombre,                                        
                                        JTextField txtTelefono, 
                                        JTextField txtDireccion, 
                                        JTextField txtRazon) {
        txtRuc.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtRazon.setText("");
    }

    public static void limpiarProducto(JTextField txtDescripcion, 
                                        JTextField txtCantidad, 
                                        JTextField txtPrecio,
                                        JComboBox<?> cbxProveedor) {
        txtDescripcion.setText("");
        txtCantidad.setText("");
        txtPrecio.setText("");
        cbxProveedor.setSelectedIndex(0);
    }
    
    public static void limpiarCamposProducto(JComboBox<?> cbxProducto,
                                        JTextField txtPrecio,
                                        JSpinner spnCantidad) {
        cbxProducto.setSelectedIndex(0);
        spnCantidad.setValue(0);
        txtPrecio.setText("");
    }

}