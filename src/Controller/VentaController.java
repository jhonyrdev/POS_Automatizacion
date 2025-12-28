
package Controller;

import DAO.*;
import Model.Venta;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/* @author Jhonny */
public class VentaController {
    
    VentaDAO venDao = new VentaDAO();
    DetalleDAO detDao = new DetalleDAO();

public void listarVentas(JTable tableVentass) {
    List<Venta> lista = venDao.listar();
    DefaultTableModel modelo = (DefaultTableModel) tableVentass.getModel();
    modelo.setRowCount(0);

    for (Venta v : lista) {

        int cantidadTotal = detDao.obtenerCantidadTotalPorVenta(v.getId());

        Object[] row = {
            v.getId(),
            v.getCliente().getNombre(),
            v.getVendedor().getNombre(),
            cantidadTotal, 
            "S/. " + v.getTotal()
        };
        modelo.addRow(row);
    }

    tableVentass.setModel(modelo);
}

}
