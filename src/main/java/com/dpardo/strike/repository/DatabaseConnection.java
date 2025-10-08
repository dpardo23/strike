package com.dpardo.strike.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de utilidad para gestionar y proporcionar la conexión a la base de datos.
 * Contiene los parámetros de conexión y un método estático para obtener una nueva conexión.
 */
public final class DatabaseConnection {

    //--- Constantes de Conexión ---
    // URL de conexión JDBC para la base de datos PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/strike?application_name=StrikeApp";
    // Usuario de la base de datos
    private static final String USER = "dpardo";
    // Contraseña del usuario
    private static final String PASSWORD = "dpardo23";

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     */
    private DatabaseConnection() {
        // Esta clase no debe ser instanciada.
    }

    /**
     * Establece y devuelve una nueva conexión con la base de datos.
     * @return un objeto Connection a la base de datos.
     * @throws SQLException si ocurre un error al intentar conectar.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}