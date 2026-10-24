
package com.subdivision.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:sqlite:subdivision.db";

    public static Connection getConnection()
            throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase()
            throws SQLException {

        String createTableSql = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    roles TEXT NOT NULL
                        CHECK (roles IN ('ADMIN', 'STAFF', 'RESIDENT')),
                    status TEXT NOT NULL DEFAULT 'PENDING'
                        CHECK (status IN ('PENDING', 'ACTIVE', 'INACTIVE'))
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute(createTableSql);
        }

        createInitialAdmin();
        System.out.println("Database initialized successfully!");
    }

    private static void createInitialAdmin()
            throws SQLException {

        String updateSql = """
                UPDATE users
                SET password = ?,
                    roles = 'ADMIN',
                    status = 'ACTIVE'
                WHERE username = ? COLLATE NOCASE
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(updateSql)) {

            statement.setString(1, "Admin123!");
            statement.setString(2, "admin");

            int updated = statement.executeUpdate();

            if (updated == 0) {
                String insertSql = """
                        INSERT INTO users
                            (username, password, roles, status)
                        VALUES (?, ?, 'ADMIN', 'ACTIVE')
                        """;

                try (PreparedStatement insert =
                             connection.prepareStatement(insertSql)) {

                    insert.setString(1, "admin");
                    insert.setString(2, "Admin123!");
                    insert.executeUpdate();
                }

                System.out.println("Initial admin account created.");
            } else {
                System.out.println("Admin account updated successfully.");
            }
        }
    }
}
