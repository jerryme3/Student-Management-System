package com.jerme.sis.db;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final Dotenv de = Dotenv.load();

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(de.get("URL"), de.get("USER"), de.get("PASSWORD"));
    }
}
