package com.subdivision;

import com.subdivision.dao.DatabaseInitializer;
import com.subdivision.dao.UserDao;
import com.subdivision.ui.Authentication;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            // The database must be initialized before any menu is shown.
            new DatabaseInitializer().initialize();

            UserDao userDao = new UserDao();
            try (Scanner scanner = new Scanner(System.in)) {
                new Authentication(userDao, scanner).start();
            }
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
}
