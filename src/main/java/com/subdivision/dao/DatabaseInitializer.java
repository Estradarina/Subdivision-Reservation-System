package com.subdivision.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public void initialize() throws SQLException {

        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    roles TEXT NOT NULL
                        CHECK (roles IN ('ADMIN', 'STAFF', 'RESIDENT')),
                    status TEXT NOT NULL DEFAULT 'PENDING'
                        CHECK (status IN ('PENDING', 'APPROVED', 'INACTIVE'))
                )
                """;

        String createActivityLogsTable = """
                CREATE TABLE IF NOT EXISTS activity_logs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    action_type TEXT NOT NULL,
                    description TEXT NOT NULL,
                    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createUsersTable);
            statement.execute(createActivityLogsTable);
        }

        createInitialAdmin();

        System.out.println("Database initialized successfully!");
    }

    private void createInitialAdmin() throws SQLException {

        String sql = """
                INSERT OR IGNORE INTO users
                    (username, password, roles, status)
                VALUES (?, ?, 'ADMIN', 'APPROVED')
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "admin");
            statement.setString(2, "Admin123!");

            int inserted = statement.executeUpdate();

            if (inserted == 1) {
                System.out.println("Initial admin account created.");
            }
        }
    }
}