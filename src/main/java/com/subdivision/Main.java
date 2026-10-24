
package com.subdivision;

import com.subdivision.dao.DatabaseInitializer;
import com.subdivision.dao.UserDao;
import com.subdivision.ui.Authentication;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            DatabaseInitializer initializer =
                    new DatabaseInitializer();

            initializer.initialize();

            UserDao userDao = new UserDao();

            try (Scanner scanner = new Scanner(System.in)) {

                Authentication authentication =
                        new Authentication(userDao, scanner);

                authentication.start();
            }

        } catch (SQLException e) {
            System.err.println(
                    "Database error: " + e.getMessage()
            );
        }
    }
}
