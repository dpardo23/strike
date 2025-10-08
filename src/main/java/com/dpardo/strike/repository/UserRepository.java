package com.dpardo.strike.repository;

import com.dpardo.strike.domain.SessionInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // <-- Importante añadir esta línea

/**
 * Repositorio para gestionar las operaciones de la entidad Usuario en la base de datos.
 */
public class UserRepository {

    /**
     * Autentica a un usuario y registra una nueva sesión en la base de datos.
     * Llama a la función de PostgreSQL 'autenticar_usuario_y_registrar_sesion'.
     *
     * @param username El nombre de usuario o correo electrónico.
     * @param password La contraseña del usuario.
     * @return un objeto SessionInfo con los datos de la sesión si la autenticación es exitosa, de lo contrario null.
     * @throws SQLException Si ocurre un error de acceso a la BD.
     */
    public SessionInfo authenticateAndRegisterSession(String username, String password) throws SQLException {
        // Llamada a la función de la base de datos
        String sql = "SELECT * FROM autenticar_usuario_y_registrar_sesion(?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignación de parámetros
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Procesa el resultado
                if (rs.next()) {
                    // Mapeo del resultado a un objeto SessionInfo
                    SessionInfo sessionInfo = new SessionInfo(
                            rs.getInt("id_usuario_out"),
                            rs.getInt("pid_out"),
                            rs.getString("client_address_out"),
                            rs.getInt("client_port_out"),
                            rs.getString("rol_nombre_out")
                    );

                    // Después de confirmar que el login es correcto y tenemos el ID del usuario,
                    // ejecutamos un comando SET en la misma conexión de la base de datos.
                    // Este comando crea una variable temporal llamada 'app.current_user_id' que solo
                    // existe para esta conexión y durante el tiempo que esté activa.
                    // Nuestro trigger en PostgreSQL leerá esta variable para saber QUIÉN hizo el cambio.
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute("SET app.current_user_id = " + sessionInfo.userId());
                    }

                    return sessionInfo;
                }
            }
        }
        // Devuelve null si la autenticación falla (ninguna fila devuelta)
        return null;
    }
}