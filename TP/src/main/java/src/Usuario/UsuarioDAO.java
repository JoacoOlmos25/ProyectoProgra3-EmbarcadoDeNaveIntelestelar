package src.Usuario;
/**
 * Contrato del patrón DAO (Data Access Object) para la gestión persistente
 * de credenciales de usuarios del sistema.
 */
public interface UsuarioDAO {

    /**
     * Registra un nuevo usuario en el almacenamiento persistente.
     * <p><b>Precondiciones:</b> {@code usuario != null}, con {@code username} y {@code password} no vacíos.</p>
     * <p><b>Postcondiciones:</b> las credenciales del usuario quedan persistidas y disponibles
     * para futuras autenticaciones.</p>
     * <p><b>Excepciones declaradas:</b> {@link UsuarioExcepciones} si el nombre de usuario ya existe
     * o si ocurre un fallo en el acceso a datos.</p>
     *
     * @param usuario DTO con las credenciales a registrar
     * @throws UsuarioExcepciones si el usuario ya está registrado o falla la persistencia
     */
    void registrar(UsuarioDTO usuario) throws UsuarioExcepciones;

    /**
     * Verifica si las credenciales suministradas coinciden con un usuario registrado.
     * <p><b>Precondiciones:</b> {@code usuario != null}.</p>
     * <p><b>Postcondiciones:</b> devuelve {@code true} si existe un registro con igual {@code username}
     * y {@code password}, o {@code false} en caso contrario. No modifica los datos persistidos.</p>
     * <p><b>Excepciones declaradas:</b> {@link UsuarioExcepciones} si ocurre un fallo en el acceso a datos.</p>
     *
     * @param usuario DTO con las credenciales a validar
     * @return {@code true} si las credenciales son válidas; {@code false} en caso contrario
     * @throws UsuarioExcepciones si ocurre un error al consultar la base de datos
     */
    boolean autenticar(UsuarioDTO usuario) throws UsuarioExcepciones;

    /**
     * Comprueba si ya existe un usuario registrado con el nombre indicado.
     * <p><b>Precondiciones:</b> {@code username != null}.</p>
     * <p><b>Postcondiciones:</b> devuelve {@code true} si el nombre ya se encuentra en uso,
     * o {@code false} si está disponible. No modifica la base de datos.</p>
     * <p><b>Excepciones declaradas:</b> {@link UsuarioExcepciones} si ocurre un error en la consulta.</p>
     *
     * @param username nombre de usuario a verificar
     * @return {@code true} si el usuario existe; {@code false} en caso contrario
     * @throws UsuarioExcepciones si ocurre un fallo al consultar la base de datos
     */
    boolean existeUsuario(String username) throws UsuarioExcepciones;
}
