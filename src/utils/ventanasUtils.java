
package utils;

import java.awt.event.*;
import javax.swing.JFrame;

public class ventanasUtils {
    public static void mostrarPrincipalAlCerrar(JFrame principal, JFrame secundario) {
        secundario.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (principal != null) {
                    principal.setVisible(true);
                }
            }
        });
    }
}
