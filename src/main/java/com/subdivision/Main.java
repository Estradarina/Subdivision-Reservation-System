
package com.subdivision;

import com.subdivision.dao.DatabaseConnection;
import com.subdivision.ui.MainMenu;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            DatabaseConnection.initializeDatabase();
            System.out.println("Database initialized successfully!");

            MainMenu.showMenu();

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
