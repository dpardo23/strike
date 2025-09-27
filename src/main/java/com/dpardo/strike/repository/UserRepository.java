package com.dpardo.strike.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public boolean authenticateUser(String username, String password) throws SQLException {

        Connection conn = DatabaseConnection.getConnection();

        if (conn == null) {
            throw new SQLException("No se pudo establecer la conexión con la base de datos.");
        }

        String sql = "SELECT COUNT(*) FROM \"user\" WHERE nombre_usuario = ? AND contrasena = ?";

        try (conn;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }
}