package src.Usuario;

/**
 * Capa de servicio (lógica de negocio) para el registro y autenticación de operadores.
 * Recibe {@link UsuarioDAO} por constructor y encapsula las reglas de dominio
 * (unicidad de usuario y verificación de credenciales), desacoplado de SQL y Swing.
 *
 * <p><b>Invariante de clase:</b> {@code usuarioDAO != null}.</p>
 */
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    /**
     * Crea el servicio inyectando su dependencia DAO obligatoria.
     * <p><b>Precondiciones:</b> {@code usuarioDAO != null}.</p>
     * <p><b>Postcondiciones:</b> el servicio queda listo para registrar y autenticar operadores.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param usuarioDAO implementación concreta del DAO de usuarios
     */
    public UsuarioService(UsuarioDAO usuarioDAO) {
        assert usuarioDAO != null : "El DAO de usuarios no puede ser nulo";
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Registra un nuevo operador verificando que su nombre de usuario no esté duplicado.
     * <p><b>Precondiciones:</b> {@code username != null && !username.isBlank()} y
     * {@code password != null && !password.isBlank()}.</p>
     * <p><b>Postcondiciones:</b> el usuario queda persistido en el DAO mediante un {@link UsuarioDTO}.</p>
     * <p><b>Excepciones declaradas:</b> {@link UsuarioExcepciones} si el usuario ya existe o falla la persistencia.</p>
     *
     * @param username nombre identificador del nuevo operador
     * @param password contraseña del nuevo operador
     * @throws UsuarioExcepciones si el nombre ya está registrado o ocurre un fallo de acceso a datos
     */
    public void registrarUsuario(String username, String password) throws UsuarioExcepciones {
        assert username != null && !username.isBlank() : "El nombre de usuario no puede ser nulo ni vacío";
        assert password != null && !password.isBlank() : "La contraseña no puede ser nula ni vacía";

        if (usuarioDAO.existeUsuario(username)) {
            throw new UsuarioExcepciones("El nombre de usuario '" + username + "' ya se encuentra registrado.");
        }

        UsuarioDTO nuevoUsuario = new UsuarioDTO(username, password);
        usuarioDAO.registrar(nuevoUsuario);
    }

    /**
     * Autentica un operador verificando sus credenciales contra los datos recuperados del DAO.
     * <p><b>Precondiciones:</b> {@code username != null && !username.isBlank()} y
     * {@code password != null && !password.isBlank()}.</p>
     * <p><b>Postcondiciones:</b> devuelve el {@link UsuarioDTO} del operador autenticado sin modificar la base de datos.</p>
     * <p><b>Excepciones declaradas:</b> {@link UsuarioExcepciones} si las credenciales son inválidas o falla la consulta.</p>
     *
     * @param username nombre de usuario ingresado
     * @param password contraseña ingresada
     * @return DTO del usuario autenticado
     * @throws UsuarioExcepciones si el usuario no existe, la contraseña no coincide o falla el acceso a datos
     */
    public UsuarioDTO iniciarSesion(String username, String password) throws UsuarioExcepciones {
        assert username != null && !username.isBlank() : "El nombre de usuario no puede ser nulo ni vacío";
        assert password != null && !password.isBlank() : "La contraseña no puede ser nula ni vacía";

        UsuarioDTO registrado = usuarioDAO.buscarPorUsername(username);
        if (registrado == null || !registrado.getPassword().equals(password)) {
            throw new UsuarioExcepciones("Usuario o contraseña incorrectos.");
        }
        return registrado;
    }
}