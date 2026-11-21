package src.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import src.Usuario.UsuarioDAO;
import src.Usuario.UsuarioDTO;
import src.Usuario.UsuarioExcepciones;
import src.vista.IVistaLogin;

/**
 * Controlador MVC encargado de coordinar el inicio de sesión y el registro de usuarios
 * entre {@link IVistaLogin} y la capa de persistencia ({@link UsuarioDAO}).
 */
public class ControladorLogin implements ActionListener {

    private final IVistaLogin vista;
    private final UsuarioDAO usuarioDAO;
    private final Consumer<String> alAutenticarExitosamente;

    /**
     * Inicializa el controlador de acceso y lo vincula con la vista de login.
     * <p><b>Precondiciones:</b> {@code vista != null}, {@code usuarioDAO != null}
     * y {@code alAutenticarExitosamente != null}.</p>
     * <p><b>Postcondiciones:</b> el controlador queda registrado como oyente de acciones de {@code vista}.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param vista vista de inicio de sesión y registro
     * @param usuarioDAO implementación DAO para persistir y validar credenciales
     * @param alAutenticarExitosamente acción a ejecutar recibiendo el nombre del usuario autenticado
     */
    public ControladorLogin(IVistaLogin vista, UsuarioDAO usuarioDAO, Consumer<String> alAutenticarExitosamente) {
        assert vista != null : "La vista de login no puede ser nula";
        assert usuarioDAO != null : "El DAO de usuarios no puede ser nulo";
        assert alAutenticarExitosamente != null : "El callback de autenticación no puede ser nulo";

        this.vista = vista;
        this.usuarioDAO = usuarioDAO;
        this.alAutenticarExitosamente = alAutenticarExitosamente;
        this.vista.setActionListener(this);
    }

    /**
     * Procesa los eventos disparados por los botones de inicio de sesión o registro.
     * <p><b>Precondiciones:</b> {@code e != null}.</p>
     * <p><b>Postcondiciones:</b> ejecuta la operación solicitada y actualiza la vista según el resultado.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param e evento de acción capturado desde la vista
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        if (IVistaLogin.CMD_LOGIN.equals(comando)) {
            manejarLogin();
        } else if (IVistaLogin.CMD_REGISTRAR.equals(comando)) {
            manejarRegistro();
        }
    }

    /**
     * Valida los datos ingresados y verifica las credenciales contra el DAO.
     * Si son correctas, cierra la ventana de acceso e inicia la aplicación principal.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> si las credenciales son válidas, se cierra {@code vista} y se invoca
     * {@code alAutenticarExitosamente}; en caso contrario se muestra un aviso de error.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    private void manejarLogin() {
        String username = vista.getUsername();
        String password = vista.getPassword();

        if (username.isBlank() || password.isBlank()) {
            vista.mostrarError("Debe completar usuario y contraseña.");
            return;
        }

        try {
            UsuarioDTO credenciales = new UsuarioDTO(username, password);
            if (usuarioDAO.autenticar(credenciales)) {
                vista.cerrar();
                alAutenticarExitosamente.accept(username);
            } else {
                vista.limpiarPassword();
                vista.mostrarError("Usuario o contraseña incorrectos.");
            }
        } catch (UsuarioExcepciones ex) {
            vista.mostrarError("Error al validar acceso: " + ex.getMessage());
        }
    }

    /**
     * Valida los datos ingresados y persiste un nuevo usuario mediante el DAO.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> si el nombre está disponible, el usuario queda persistido en la base de datos
     * y se notifica al operador; si ya existe o hay error, se muestra un mensaje de advertencia.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    private void manejarRegistro() {
        String username = vista.getUsername();
        String password = vista.getPassword();

        if (username.isBlank() || password.isBlank()) {
            vista.mostrarError("Debe ingresar un nombre de usuario y una contraseña válidos.");
            return;
        }

        try {
            UsuarioDTO nuevoUsuario = new UsuarioDTO(username, password);
            usuarioDAO.registrar(nuevoUsuario);
            vista.limpiarPassword();
            vista.mostrarInfo("Operador '" + username + "' registrado correctamente. Ya puede iniciar sesión.");
        } catch (UsuarioExcepciones ex) {
            vista.mostrarError(ex.getMessage());
        }
    }
}
