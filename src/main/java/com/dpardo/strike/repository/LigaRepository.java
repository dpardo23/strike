package com.dpardo.strike.repository;

import com.dpardo.strike.domain.LigaComboItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones de la entidad Liga en la base de datos.
 */
public class LigaRepository {

    /**
     * Obtiene una lista de ligas (ID y nombre) para ser usada en ComboBoxes.
     * @return Una lista de objetos LigaComboItem.
     * @throws SQLException Si ocurre un error de acceso a la BD.
     */
    public List<LigaComboItem> obtenerLigasParaCombo() throws SQLException {
        List<LigaComboItem> ligas = new ArrayList<>();
        String sql = "SELECT * FROM obtener_ligas_para_combo()"; // Llamada a la función de BD

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ligas.add(new LigaComboItem(rs.getInt("id"), rs.getString("nombre_liga")));
            }
        }
        return ligas;
    }

    /**
     * Inserta una nueva liga en la base de datos llamando a un procedimiento almacenado.
     * @param id El identificador de la liga.
     * @param nombre El nombre de la liga.
     * @param paisCodFifa El código FIFA del país asociado.
     * @param tipo El tipo de liga (ej: "Profesional").
     * @throws SQLException Si ocurre un error durante la inserción.
     */
    public void insertarLiga(int id, String nombre, String paisCodFifa, String tipo) throws SQLException {
        String sql = "{CALL insertar_liga(?, ?, ?, ?)}"; // Llamada al procedimiento almacenado

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            // Asignación de parámetros
            cstmt.setInt(1, id);
            cstmt.setString(2, nombre);
            cstmt.setString(3, paisCodFifa);
            cstmt.setString(4, tipo);

            cstmt.execute();
        }
        // La excepción se relanza para que sea manejada por el controlador.
    }
}