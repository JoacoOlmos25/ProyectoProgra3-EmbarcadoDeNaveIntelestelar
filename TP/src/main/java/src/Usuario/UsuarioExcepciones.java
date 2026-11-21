package src.Usuario;

/**
 * Excepción verificada que encapsula fallos ocurridos en la capa de acceso a datos
 * (errores de conexión JDBC, consultas SQL o reglas de integridad).
 */
public class UsuarioExcepciones extends Exception {

    /**
     * Crea una excepción de persistencia con un mensaje descriptivo.
     * <p><b>Precondiciones:</b> {@code message != null}.</p>
     * <p><b>Postcondiciones:</b> la excepción queda inicializada con el mensaje indicado.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param message descripción del error ocurrido
     */
    public UsuarioExcepciones(String message) {
        super(message);
    }

    /**
     * Crea una excepción de persistencia encadenando la causa original.
     * <p><b>Precondiciones:</b> {@code message != null} y {@code cause != null}.</p>
     * <p><b>Postcondiciones:</b> la excepción queda inicializada con mensaje y causa subyacente.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param message descripción del error ocurrido
     * @param cause excepción original que provocó el fallo
     */
    public UsuarioExcepciones(String message, Throwable cause) {
        super(message, cause);
    }
}

