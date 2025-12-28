
package Controller;

import DAO.UsuarioDAO;
import Model.Usuario;

/* @author Jhonny */
public class UsuarioController {
    UsuarioDAO dao = new UsuarioDAO();

    public String registrarUsuario(String nombre, String correo, String pass) {

    String regexNombre = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$";
    String regexCorreo = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
        return "Todos los campos son obligatorios.";
    }
    if (!nombre.matches(regexNombre)) {
        return "El nombre solo puede contener letras y espacios.";
    }
    if (!correo.matches(regexCorreo)) {
        return "El correo ingresado no es válido.";
    }

    Usuario u = new Usuario();
    u.setNombre(nombre);
    u.setCorreo(correo);
    u.setPass(pass);

    boolean ok = dao.guardar(u);
    if (ok) {
        return "OK";
    } else {
        return "Error al registrar usuario.";
    }
}

}
