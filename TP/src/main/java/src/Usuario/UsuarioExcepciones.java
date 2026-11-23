package src.Usuario;

/**
 * Excepción verificada que encapsula fallos de reglas de acceso o de persistencia
 * (usuario duplicado, credenciales inválidas, errores JDBC o SQL).
 */
public class UsuarioExcepciones extends Exception {

    /**
     * Crea una excepción de usuario/persistencia con un mensaje descriptivo.
     * <p><b>Precondiciones:</b> {@code message != null && !message.isBlank()}.</p>
     * <p><b>Postcondiciones:</b> la excepción queda inicializada con el mensaje indicado.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param message descripción del error ocurrido
     */
    public UsuarioExcepciones(String message) {
        super(message);
        assert message != null && !message.isBlank() : "El mensaje de la excepción no puede ser nulo ni vacío";
    }

    /**
     * Crea una excepción encadenando la causa original.
     * <p><b>Precondiciones:</b> {@code message != null && !message.isBlank()} y {@code cause != null}.</p>
     * <p><b>Postcondiciones:</b> la excepción queda inicializada con mensaje y causa subyacente.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param message descripción del error ocurrido
     * @param cause excepción original que provocó el fallo
     */
    public UsuarioExcepciones(String message, Throwable cause) {
        super(message, cause);
        assert message != null && !message.isBlank() : "El mensaje de la excepción no puede ser nulo ni vacío";
        assert cause != null : "La causa original no puede ser nula";
    }
}