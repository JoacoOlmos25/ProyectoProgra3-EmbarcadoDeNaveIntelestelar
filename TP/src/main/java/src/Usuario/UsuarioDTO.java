package src.Usuario;

import java.io.Serializable;

/**
 * Objeto de Transferencia de Datos (DTO) que encapsula las credenciales de un operador
 * para transportarlas entre la capa de servicios y el DAO.
 *
 * <p><b>Invariante de clase:</b> {@code username != null && password != null}.</p>
 */
public class UsuarioDTO implements Serializable {
    private static final long serialVersionUID = 1L;

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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}