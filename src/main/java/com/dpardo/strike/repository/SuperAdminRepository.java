package com.dpardo.strike.repository;

import com.dpardo.strike.domain.SessionViewModel;
import com.dpardo.strike.domain.UiComboItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones específicas de la vista de Super Administrador.
 */
public class SuperAdminRepository {

    /**
     * Obtiene una lista detallada de todas las sesiones activas en la base de datos.
     * @return Una lista de objetos SessionViewModel.
     * @throws SQLException Si ocurre un error de acceso a la BD.
     */
    public List<SessionViewModel> obtenerSesionesActivas() throws SQLException {
        List<SessionViewModel> sesiones = new ArrayList<>();
        String sql = "SELECT * FROM obtener_sesiones_activas_detalladas()"; // Llamada a la función de BD

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Mapeo de cada fila del resultado a un objeto SessionViewModel
                sesiones.add(new SessionViewModel(
                        rs.getInt("pid"),
                        rs.getString("nombre_usuario"),
                        rs.getString("correo"),
                        rs.getTimestamp("fec_creacion_usuario"),
                        rs.getString("nombre_rol"),
                        rs.getString("cod_componente_ui"),
                        rs.getString("direccion_ip"),
                        rs.getInt("puerto"),
                        rs.getTimestamp("fecha_asignacion_rol"),
                        rs.getBoolean("rol_activo")
                ));
            }
        }
        return sesiones;
    }

    /**
     * Obtiene una lista de componentes de UI (ID, código y descripción) para ComboBoxes.
     * @return Una lista de objetos UiComboItem.
     * @throws SQLException Si ocurre un error de acceso a la BD.
     */
    public List<UiComboItem> obtenerUis() throws SQLException {
        List<UiComboItem> uis = new ArrayList<>();
        String sql = "SELECT * FROM obtener_uis_para_combo()"; // Llamada a la función de BD

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                uis.add(new UiComboItem(rs.getInt("id"), rs.getString("codigo_componente"), rs.getString("descripcion_ui")));
            }
        }
        return uis;
    }
}