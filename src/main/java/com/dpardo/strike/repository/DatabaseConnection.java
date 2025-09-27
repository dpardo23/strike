package com.dpardo.strike.repository; // O donde manejes la conexión

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/strike";

    private static final String USER = "dpardo";
    private static final String PASSWORD = "dpardo23";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos 'strike'.");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("¡Conexión exitosa a la base de datos strike!");
            } else {
                System.out.println("Fallo la conexión.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}