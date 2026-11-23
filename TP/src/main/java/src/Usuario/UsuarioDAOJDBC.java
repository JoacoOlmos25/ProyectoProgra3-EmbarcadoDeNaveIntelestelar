package src.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementación JDBC sobre SQLite del patrón DAO para credenciales de usuarios.
 * Todas las conexiones y recursos JDBC se abren y cierran exclusivamente dentro de cada método.
 */
public class UsuarioDAOJDBC implements UsuarioDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public void registrar(UsuarioDTO usuario) throws UsuarioExcepciones {
        assert usuario != null : "El DTO de usuario no puede ser nulo";
        assert !usuario.getUsername().isBlank() && !usuario.getPassword().isBlank()
                : "El usuario y la contraseña no pueden estar vacíos";

        String sql = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new UsuarioExcepciones("Error al registrar el usuario en la base de datos: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UsuarioDTO buscarPorUsername(String username) throws UsuarioExcepciones {
        assert username != null && !username.isBlank() : "El nombre de usuario no puede ser nulo ni vacío";

        String sql = "SELECT username, password FROM usuarios WHERE username = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UsuarioDTO(rs.getString("username"), rs.getString("password"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new UsuarioExcepciones("Error al buscar el usuario en la base de datos: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existeUsuario(String username) throws UsuarioExcepciones {
        assert username != null && !username.isBlank() : "El nombre de usuario no puede ser nulo ni vacío";

        String sql = "SELECT 1 FROM usuarios WHERE username = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new UsuarioExcepciones("Error al verificar existencia del usuario: " + e.getMessage(), e);
        }
    }
}