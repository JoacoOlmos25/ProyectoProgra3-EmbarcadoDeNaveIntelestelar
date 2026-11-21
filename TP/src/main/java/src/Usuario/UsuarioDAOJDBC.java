package src.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementación JDBC sobre SQLite del patrón DAO para el acceso y registro
 * de credenciales de usuarios.
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

        if (existeUsuario(usuario.getUsername())) {
            throw new UsuarioExcepciones("El nombre de usuario '" + usuario.getUsername() + "' ya se encuentra registrado.");
        }

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
    public boolean autenticar(UsuarioDTO usuario) throws UsuarioExcepciones {
        assert usuario != null : "El DTO de usuario no puede ser nulo";

        String sql = "SELECT 1 FROM usuarios WHERE username = ? AND password = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new UsuarioExcepciones("Error al validar credenciales: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existeUsuario(String username) throws UsuarioExcepciones {
        assert username != null : "El nombre de usuario no puede ser nulo";

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
