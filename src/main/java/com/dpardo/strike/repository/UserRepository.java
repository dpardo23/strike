package com.dpardo.strike.repository;
import com.dpardo.strike.domain.SessionInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    public SessionInfo authenticateAndRegisterSession(String username, String password) throws SQLException {

        String sql = "SELECT exito, id_usuario_logueado, session_pid, client_ip, client_port_num " +
                "FROM iniciar_sesion_y_registrar(?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    boolean success = rs.getBoolean("exito");
                    if (success) {
                        // Si la autenticación fue exitosa, extraemos los datos
                        int userId = rs.getInt("id_usuario_logueado");
                        int pid = rs.getInt("session_pid");
                        String clientIp = rs.getString("client_ip");
                        int clientPort = rs.getInt("client_port_num");
                        return new SessionInfo(userId, pid, clientIp, clientPort);
                    }
                }
            }
        }
        return null;
    }
}