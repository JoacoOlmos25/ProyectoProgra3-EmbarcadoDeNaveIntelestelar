package src.vista;

import java.awt.event.ActionListener;

/**
 * Contrato de la vista de control de acceso al sistema (Login y Registro),
 * desacoplado de los componentes concretos de Swing según el patrón MVC.
 */
public interface IVistaLogin {

    String CMD_LOGIN = "INICIAR_SESION";
    String CMD_REGISTRAR = "REGISTRAR_USUARIO";

    /**
     * Vincula el controlador que escuchará las acciones de inicio de sesión y registro.
     * <p><b>Precondiciones:</b> {@code controlador != null}.</p>
     * <p><b>Postcondiciones:</b> los controles interactivos de la vista notifican sus eventos al {@code controlador}.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param controlador oyente de eventos de acción
     */
    void setActionListener(ActionListener controlador);

    /**
     * Obtiene el nombre de usuario ingresado en el formulario.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> devuelve el texto sin espacios iniciales ni finales.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @return nombre de usuario ingresado
     */
    String getUsername();

    /**
     * Obtiene la contraseña ingresada en el formulario.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> devuelve la clave ingresada como cadena de texto.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @return contraseña ingresada
     */
    String getPassword();

    /**
     * Muestra un mensaje de error en pantalla.
     * <p><b>Precondiciones:</b> {@code mensaje != null}.</p>
     * <p><b>Postcondiciones:</b> se presenta una alerta visual con el mensaje indicado.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param mensaje descripción del error
     */
    void mostrarError(String mensaje);

    /**
     * Muestra un mensaje informativo en pantalla.
     * <p><b>Precondiciones:</b> {@code mensaje != null}.</p>
     * <p><b>Postcondiciones:</b> se presenta un aviso visual con el mensaje indicado.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param mensaje texto informativo
     */
    void mostrarInfo(String mensaje);

    /**
     * Limpia el campo de contraseña por seguridad tras un intento de acceso o registro.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> el campo de contraseña queda vacío.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    void limpiarPassword();

    /**
     * Hace visible la ventana de acceso al sistema.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> la ventana queda visible en pantalla.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    void mostrar();

    /**
     * Cierra y libera los recursos gráficos de la ventana de acceso.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> la ventana queda cerrada y dispuesta.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    void cerrar();
}
