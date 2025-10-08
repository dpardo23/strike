package com.dpardo.strike.repository;

import com.dpardo.strike.domain.Pais;
import com.dpardo.strike.domain.PaisComboItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones de la entidad País en la base de datos.
 */
public class PaisRepository {

    /**
     * Obtiene una lista completa de todos los países.
     * @return Una lista de objetos Pais.
     */
    public List<Pais> obtenerTodosLosPaises() {
        List<Pais> paises = new ArrayList<>();
        String sql = "SELECT cod_fifa, nombre_pais FROM obtener_todos_los_paises()";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String codigo = rs.getString("cod_fifa");
                String nombre = rs.getString("nombre_pais");
                paises.add(new Pais(nombre, codigo));
            }
        } catch (SQLException e) {
            // Manejo de errores local: imprime el error en la consola
            System.err.println("Error al obtener los países desde la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return paises;
    }

    /**
     * Obtiene una lista de países (código FIFA y nombre) para ser usada en ComboBoxes.
     * @return Una lista de objetos PaisComboItem.
     * @throws SQLException Si ocurre un error de acceso a la BD.
     */
    public List<PaisComboItem> obtenerPaisesParaCombo() throws SQLException {
        List<PaisComboItem> paises = new ArrayList<>();
        String sql = "SELECT * FROM obtener_paises_fifa_y_nombre()";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                paises.add(new PaisComboItem(rs.getString("codigo_fifa"), rs.getString("nombre_pais")));
            }
        }
        return paises;
    }

    /**
     * Inserta un nuevo país en la base de datos llamando a un procedimiento almacenado.
     * @param codFifa El código FIFA del país (ej: "BOL").
     * @param nombre El nombre del país.
     * @param continente El continente al que pertenece.
     * @throws SQLException Si ocurre un error durante la inserción (ej: clave duplicada).
     */
    public void insertarPais(String codFifa, String nombre, String continente) throws SQLException {
        String sql = "{CALL insertar_pais(?, ?, ?)}"; // Llamada al procedimiento almacenado

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            // Asignación de parámetros
            cstmt.setString(1, codFifa);
            cstmt.setString(2, nombre);
            cstmt.setString(3, continente);

            cstmt.execute();
            System.out.println("País '" + nombre + "' insertado correctamente.");

        } catch (SQLException e) {
            // Relanza la excepción para que el controlador la maneje y muestre al usuario.
            throw e;
        }
    }
}