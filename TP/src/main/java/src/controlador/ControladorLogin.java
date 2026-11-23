package src.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import src.Usuario.UsuarioDTO;
import src.Usuario.UsuarioExcepciones;
import src.Usuario.UsuarioService;
import src.vista.IVistaLogin;

/**
 * Controlador MVC encargado de coordinar el inicio de sesión y el registro de usuarios
 * entre {@link IVistaLogin} y el servicio de negocio ({@link UsuarioService}).
 */
public class ControladorLogin implements ActionListener {

    private final IVistaLogin vista;
    private final UsuarioService usuarioService;
    private final Consumer<String> alAutenticarExitosamente;

    /**
     * Inicializa el controlador de acceso y lo vincula con la vista de login.
     * <p><b>Precondiciones:</b> {@code vista != null}, {@code usuarioService != null}
     * y {@code alAutenticarExitosamente != null}.</p>
     * <p><b>Postcondiciones:</b> el controlador queda registrado como oyente de acciones de {@code vista}.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param vista vista de inicio de sesión y registro
     * @param usuarioService servicio de negocio para registrar y autenticar credenciales
     * @param alAutenticarExitosamente acción a ejecutar recibiendo el nombre del usuario autenticado
     */
    public ControladorLogin(IVistaLogin vista, UsuarioService usuarioService, Consumer<String> alAutenticarExitosamente) {
        assert vista != null : "La vista de login no puede ser nula";
        assert usuarioService != null : "El servicio de usuarios no puede ser nulo";
        assert alAutenticarExitosamente != null : "El callback de autenticación no puede ser nulo";

        this.vista = vista;
        this.usuarioService = usuarioService;
        this.alAutenticarExitosamente = alAutenticarExitosamente;
        this.vista.setActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        assert e != null : "El evento de acción no puede ser nulo";
        String comando = e.getActionCommand();
        if (IVistaLogin.CMD_LOGIN.equals(comando)) {
            manejarLogin();
        } else if (IVistaLogin.CMD_REGISTRAR.equals(comando)) {
            manejarRegistro();
        }
    }

    private void manejarLogin() {
        String username = vista.getUsername();
        String password = vista.getPassword();

        try {
            UsuarioDTO autenticado = usuarioService.iniciarSesion(username, password);
            vista.cerrar();
            alAutenticarExitosamente.accept(autenticado.getUsername());
        } catch (UsuarioExcepciones ex) {
            vista.limpiarPassword();
            vista.mostrarError(ex.getMessage());
        }
    }

    private void manejarRegistro() {
        String username = vista.getUsername();
        String password = vista.getPassword();

        try {
            usuarioService.registrarUsuario(username, password);
            vista.limpiarPassword();
            vista.mostrarInfo("Operador '" + username + "' registrado correctamente. Ya puede iniciar sesión.");
        } catch (UsuarioExcepciones ex) {
            vista.mostrarError(ex.getMessage());
        }
    }
}