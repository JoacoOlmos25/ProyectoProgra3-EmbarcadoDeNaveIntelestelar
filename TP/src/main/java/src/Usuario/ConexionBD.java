package src.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Administra la conexión JDBC con la base de datos SQLite local e inicializa
 * el esquema requerido para el almacenamiento de credenciales.
 */
public final class ConexionBD {

    private static final String URL_SQLITE = "jdbc:sqlite:usuarios.db";

    private ConexionBD() {
    }

    /**
     * Abre una nueva conexión hacia la base de datos SQLite y asegura que exista
     * la tabla {@code usuarios}.
     * <p><b>Precondiciones:</b> el driver {@code org.xerial:sqlite-jdbc} debe estar disponible en el classpath.</p>
     * <p><b>Postcondiciones:</b> devuelve una {@link Connection} abierta y válida con la tabla
     * {@code usuarios(username, password)} creada si aún no existía.</p>
     * <p><b>Excepciones declaradas:</b> {@link UsuarioExcepciones} si ocurre un error de conexión
     * o al inicializar el esquema.</p>
     *
     * @return conexión activa a la base de datos SQLite
     * @throws UsuarioExcepciones si falla la apertura de la conexión o la creación de la tabla
     */
    public static Connection obtenerConexion() throws UsuarioExcepciones {
        try {
            Connection conexion = DriverManager.getConnection(URL_SQLITE);
            inicializarTabla(conexion);
            return conexion;
        } catch (SQLException e) {
            throw new UsuarioExcepciones("No se pudo conectar con la base de datos de usuarios: " + e.getMessage(), e);
        }
    }

    /**
     * Crea la tabla {@code usuarios} en caso de que no exista en la base de datos.
     * <p><b>Precondiciones:</b> {@code conexion != null} y debe estar abierta.</p>
     * <p><b>Postcondiciones:</b> la tabla {@code usuarios} queda disponible en la base de datos.</p>
     * <p><b>Excepciones declaradas:</b> {@link SQLException} si falla la ejecución de la sentencia DDL.</p>
     *
     * @param conexion conexión activa sobre la cual ejecutar la sentencia
     * @throws SQLException si ocurre un error SQL al crear la tabla
     */
    private static void inicializarTabla(Connection conexion) throws SQLException {
        assert conexion != null : "La conexión no puede ser nula";
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "username TEXT PRIMARY KEY, "
                + "password TEXT NOT NULL"
                + ")";
        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(sql);
        }
    }
}
