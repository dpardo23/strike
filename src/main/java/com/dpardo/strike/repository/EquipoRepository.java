package com.dpardo.strike.repository;

import com.dpardo.strike.domain.EquipoComboItem;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones de la entidad Equipo en la base de datos.
 */
public class EquipoRepository {

    /**
     * Obtiene una lista con todos los códigos FIFA de los países desde la BD.
     * @return Una lista de Strings (ej: "BOL", "ARG", ...).
     * @throws SQLException Si ocurre un error de acceso a la BD.
     */
    public List<String> obtenerCodigosPaises() throws SQLException {
        List<String> codigosPaises = new ArrayList<>();
        String sql = "SELECT * FROM obtener_codigos_paises()"; // Llamada a la función de BD

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                codigosPaises.add(rs.getString("codigo_fifa"));
            }
        }
        return codigosPaises;
    }

    /**
     * Obtiene una lista de equipos (ID y nombre) para ser usada en ComboBoxes.
     * @return Una lista de objetos EquipoComboItem.
     * @throws SQLException Si ocurre un error de acceso a la BD.
     */
    public List<EquipoComboItem> obtenerEquiposParaCombo() throws SQLException {
        List<EquipoComboItem> equipos = new ArrayList<>();
        String sql = "SELECT * FROM obtener_equipos_para_combo()"; // Llamada a la función de BD

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                equipos.add(new EquipoComboItem(rs.getInt("id"), rs.getString("nombre_equipo")));
            }
        }
        return equipos;
    }

    /**
     * Inserta un nuevo equipo en la base de datos llamando a un procedimiento almacenado.
     * @param id El identificador del equipo.
     * @param nombre El nombre del equipo.
     * @param paisCodFifa El código FIFA del país asociado.
     * @param ciudad La ciudad del equipo.
     * @param fechaFundacion La fecha de fundación del equipo.
     * @param dt El nombre del director técnico.
     * @throws SQLException Si ocurre un error durante la inserción.
     */
    public void insertarEquipo(int id, String nombre, String paisCodFifa, String ciudad, LocalDate fechaFundacion, String dt) throws SQLException {
        String sql = "{CALL insertar_equipo(?, ?, ?, ?, ?, ?)}"; // Llamada al procedimiento almacenado

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            // Asignación de parámetros
            cstmt.setInt(1, id);
            cstmt.setString(2, nombre);
            cstmt.setString(3, paisCodFifa);
            cstmt.setString(4, ciudad);
            cstmt.setDate(5, Date.valueOf(fechaFundacion));
            cstmt.setString(6, dt);

            cstmt.execute();
        }
        // La excepción se relanza para que sea manejada por el controlador
    }
}