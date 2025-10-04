package com.dpardo.strike.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/strike?application_name=StrikeApp";
    private static final String USER = "dpardo";
    private static final String PASSWORD = "dpardo23";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}