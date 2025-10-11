package com.dpardo.strike.repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones de la entidad Jugador en la base de datos.
 */
public class JugadorRepository {

    /**
     * Inserta un nuevo jugador en la base de datos llamando a un procedimiento almacenado.
     *
     * @param id El identificador del jugador.
     * @param nombre El nombre completo del jugador.
     * @param fechaNacimiento La fecha de nacimiento del jugador.
     * @param sexo El sexo del jugador ('M' o 'F').
     * @param paisCodFifa El código FIFA del país de nacionalidad.
     * @param posicion La posición de juego del jugador.
     * @param equipoId El ID del equipo al que pertenece el jugador.
     * @param estadisticas Un valor numérico para estadísticas (puede ser nulo).
     * @param altura La altura del jugador en centímetros.
     * @param peso El peso del jugador en kilogramos.
     * @param foto Un arreglo de bytes que representa la imagen del jugador (puede ser nulo).
     * @throws SQLException Si ocurre un error durante la inserción en la BD.
     */
    public void insertarJugador(int id, String nombre, LocalDate fechaNacimiento, char sexo,
                                String paisCodFifa, String posicion, int equipoId,
                                Integer estadisticas, int altura, BigDecimal peso, byte[] foto) throws SQLException {

        // Llamada al procedimiento almacenado 'insertar_jugador'
        String sql = "{CALL insertar_jugador(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            // Asignación de parámetros al CallableStatement
            cstmt.setInt(1, id);
            cstmt.setString(2, nombre);
            cstmt.setDate(3, Date.valueOf(fechaNacimiento));
            cstmt.setString(4, String.valueOf(sexo)); // Se envía como String de 1 carácter
            cstmt.setString(5, paisCodFifa);
            cstmt.setString(6, posicion);
            cstmt.setInt(7, equipoId);
            cstmt.setObject(8, estadisticas, Types.INTEGER); // Permite el envío de valores nulos
            cstmt.setInt(9, altura);
            cstmt.setBigDecimal(10, peso);
            cstmt.setBytes(11, foto); // Guarda los bytes de la imagen

            // Ejecución del procedimiento
            cstmt.execute();
        }
    }

    /**
     * Obtiene una lista de todos los nombres de los jugadores.
     * @return Lista de Strings con los nombres.
     */
    public List<String> obtenerTodosLosNombresDeJugadores() {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre FROM public.jugador ORDER BY nombre";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombres;
    }

    /**
     * Llama al procedimiento almacenado para eliminar un jugador por su nombre.
     * @param nombre El nombre del jugador a eliminar.
     * @throws SQLException Si ocurre un error de BD.
     */
    public void eliminarJugadorPorNombre(String nombre) throws SQLException {
        String sql = "SELECT public.eliminar_jugador_por_nombre(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.execute();
        }
    }
}