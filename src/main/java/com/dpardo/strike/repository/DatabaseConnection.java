package com.dpardo.strike.repository;

import com.dpardo.strike.domain.SessionManager; // <-- Importante añadir esta línea
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement; // <-- Importante añadir esta línea

/**
 * Clase de utilidad para gestionar y proporcionar la conexión a la base de datos.
 * Contiene los parámetros de conexión y un método estático para obtener una nueva conexión.
 */
public final class DatabaseConnection {

    //--- Constantes de Conexión ---
    private static final String URL = "jdbc:postgresql://localhost:5432/strike?application_name=StrikeApp";
    private static final String USER = "dpardo";
    private static final String PASSWORD = "dpardo23";

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     */
    private DatabaseConnection() {
        // Esta clase no debe ser instanciada.
    }

    /**
     * Establece y devuelve una conexión a la base de datos.
     * MODIFICADO: Antes de devolver la conexión, la "marca" con el ID del usuario
     * de la sesión actual para que los triggers de auditoría funcionen.
     *
     * @return un objeto Connection configurado.
     * @throws SQLException si ocurre un error al conectar.
     */
    public static Connection getConnection() throws SQLException {
        // 1. Obtenemos una conexión estándar de la base de datos.
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

        // 2. Obtenemos el ID del usuario de la sesión actual desde el SessionManager.
        Integer currentUserId = SessionManager.getCurrentUserId();

        // 3. Si hay un usuario logueado (después del login), "marcamos" la conexión.
        if (currentUserId != null) {
            try (Statement stmt = conn.createStatement()) {
                // Este comando crea la variable temporal 'app.current_user_id'
                // que el trigger de auditoría leerá en la base de datos.
                stmt.execute("SET app.current_user_id = " + currentUserId);
            }
        }

        // 4. Devolvemos la conexión, ya sea anónima (si nadie se ha logueado) o "marcada".
        return conn;
    }
}