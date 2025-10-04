package com.dpardo.strike.repository;

import com.dpardo.strike.domain.Pais;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaisRepository {

    public List<Pais> obtenerTodosLosPaises() {
        List<Pais> paises = new ArrayList<>();

        // --- CORRECCIÓN: Especificamos las columnas que queremos ---
        // Esto es más seguro y una mejor práctica que usar SELECT *.
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
            System.err.println("Error al obtener los países desde la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return paises;
    }
}