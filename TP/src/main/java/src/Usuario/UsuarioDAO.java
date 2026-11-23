package src.Usuario;

/**
 * Contrato del patrón DAO (Data Access Object) para el acceso persistente
 * a credenciales de usuarios. Solo encapsula operaciones CRUD sin reglas de negocio.
 */
public interface UsuarioDAO {

    /**
     * Persiste un nuevo usuario en la fuente de datos.
     * <p><b>Precondiciones:</b> {@code usuario != null}, con {@code username} y {@code password} no vacíos,
     * y el {@code username} no debe existir previamente.</p>
     * <p><b>Postcondiciones:</b> las credenciales quedan almacenadas en el repositorio persistente.</p>
     * <p><b>Excepciones declaradas:</b> {@link UsuarioExcepciones} si ocurre un fallo de acceso a datos.</p>
     *
     * @param usuario DTO con las credenciales a persistir
     * @throws UsuarioExcepciones si falla la operación SQL o la conexión
     */
    void registrar(UsuarioDTO usuario) throws UsuarioExcepciones;

    /**
     * Recupera un usuario desde la fuente de datos a partir de su nombre de usuario.
     * <p><b>Precondiciones:</b> {@code username != null && !username.isBlank()}.</p>
     * <p><b>Postcondiciones:</b> devuelve un {@link UsuarioDTO} con los datos recuperados si existe,
     * o {@code null} si no se encontró. No modifica los datos persistidos.</p>
     * <p><b>Excepciones declaradas:</b> {@link UsuarioExcepciones} si ocurre un error en la consulta.</p>
     *
     * @param username identificador del usuario buscado
     * @return DTO del usuario encontrado, o {@code null} si no existe
     * @throws UsuarioExcepciones si falla la consulta a la base de datos
     */
    UsuarioDTO buscarPorUsername(String username) throws UsuarioExcepciones;

    /**
     * Comprueba si ya existe un usuario registrado con el nombre indicado.
     * <p><b>Precondiciones:</b> {@code username != null && !username.isBlank()}.</p>
     * <p><b>Postcondiciones:</b> devuelve {@code true} si el nombre está en uso o {@code false} si está disponible.</p>
     * <p><b>Excepciones declaradas:</b> {@link UsuarioExcepciones} si ocurre un fallo en la consulta.</p>
     *
     * @param username nombre de usuario a verificar
     * @return {@code true} si existe; {@code false} en caso contrario
     * @throws UsuarioExcepciones si ocurre un fallo al consultar la base de datos
     */
    boolean existeUsuario(String username) throws UsuarioExcepciones;
}