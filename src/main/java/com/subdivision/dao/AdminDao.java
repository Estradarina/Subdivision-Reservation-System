
package com.subdivision.dao;

import com.subdivision.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {

    // Get all pending accounts
    public List<User> getPendingUsers() throws SQLException {
        List<User> users = new ArrayList<>();

        String sql = """
                SELECT id, username, password, roles, status
                FROM users
                WHERE status = 'PENDING'
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                users.add(new User(
                        result.getInt("id"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("roles"),
                        result.getString("status")
                ));
            }
        }

        return users;
    }

    // Approve a pending account
    public boolean approveUser(String username) throws SQLException {
        UserDao userDao = new UserDao();
        return userDao.approveUser(username);
    }
}
