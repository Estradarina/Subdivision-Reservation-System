
package com.subdivision.dao;

import com.subdivision.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public boolean register(User user) throws SQLException {
        String sql = """
                INSERT INTO users (username, password, roles, status)
                VALUES (?, ?, ?, 'PENDING')
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.username());
            statement.setString(2, user.password());
            statement.setString(3, user.roles());

            return statement.executeUpdate() == 1;
        }
    }

    public User login(String username, String password)
            throws SQLException {

        String sql = """
                SELECT id, username, password, roles, status
                FROM users
                WHERE username = ?
                  AND password = ?
                  AND status = 'ACTIVE'
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new User(
                            result.getInt("id"),
                            result.getString("username"),
                            result.getString("password"),
                            result.getString("roles"),
                            result.getString("status")
                    );
                }
            }
        }

        return null;
    }

    // Approve a pending user account
    public boolean approveUser(String username) throws SQLException {
        String sql = """
                UPDATE users
                SET status = 'ACTIVE'
                WHERE username = ? AND status = 'PENDING'
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            return statement.executeUpdate() == 1;
        }
    }
}
