package src.Usuario;

/**
 * Objeto de Transferencia de Datos (DTO) que encapsula las credenciales de un operador
 * para transportarlas entre la capa de presentación, el controlador y el DAO.
 */
public class UsuarioDTO {

    private final String username;
    private final String password;

    /**
     * Construye un objeto de transferencia de usuario con sus credenciales.
     * <p><b>Precondiciones:</b> {@code username != null} y {@code password != null}.</p>
     * <p><b>Postcondiciones:</b> el DTO queda inicializado de forma inmutable con los valores recibidos.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param username nombre identificador del usuario
     * @param password clave de acceso del usuario
     */
    public UsuarioDTO(String username, String password) {
        assert username != null : "El nombre de usuario no puede ser nulo";
        assert password != null : "La contraseña no puede ser nula";
        this.username = username;
        this.password = password;
    }

    /**
     * Devuelve el nombre de usuario almacenado en el DTO.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> devuelve el identificador del usuario sin modificar el estado del objeto.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @return nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Devuelve la contraseña almacenada en el DTO.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> devuelve la clave de acceso sin modificar el estado del objeto.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @return contraseña del usuario
     */
    public String getPassword() {
        return password;
    }
}
