
package com.subdivision.dao;

import com.subdivision.model.User;

import java.sql.SQLException;
import java.util.List;

public class AdminDao {

    private final UserDao userDao = new UserDao();

    public List<User> getAllUsers() throws SQLException {
        return userDao.getAllUsers();
    }

    public List<User> getPendingUsers() throws SQLException {
        return userDao.getPendingUsers();
    }

    public boolean approveUser(int userId) throws SQLException {
        return userDao.approveUser(userId);
    }

    public boolean updateUserRoleAndStatus(
            int userId,
            String newRole,
            String newStatus
    ) throws SQLException {

        return userDao.updateUserRoleAndStatus(
                userId,
                newRole.toUpperCase(),
                newStatus.toUpperCase()
        );
    }
}
