package com.dpardo.strike.repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Repositorio para gestionar las operaciones de la entidad Partido en la base de datos.
 */
public class PartidoRepository {

    /**
     * Inserta un nuevo partido en la base de datos llamando a un procedimiento almacenado.
     *
     * @param equipoLocalId El ID del equipo local.
     * @param equipoVisitanteId El ID del equipo visitante.
     * @param fecha La fecha en que se juega el partido.
     * @param hora La hora de inicio del partido.
     * @param ligaId El ID de la liga a la que pertenece el partido.
     * @param historial Un número único que identifica el historial del partido.
     * @throws SQLException Si ocurre un error durante la inserción (ej: clave duplicada).
     */
    public void insertarPartido(int equipoLocalId, int equipoVisitanteId, LocalDate fecha, LocalTime hora, int ligaId, int historial) throws SQLException {
        // Llamada al procedimiento almacenado 'insertar_partido'
        String sql = "{CALL insertar_partido(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            // Asignación de parámetros al CallableStatement
            cstmt.setInt(1, equipoLocalId);
            cstmt.setInt(2, equipoVisitanteId);
            cstmt.setDate(3, Date.valueOf(fecha));
            cstmt.setTime(4, Time.valueOf(hora));
            cstmt.setInt(5, ligaId);
            cstmt.setInt(6, historial);

            // Ejecución del procedimiento
            cstmt.execute();
        }
    }
}