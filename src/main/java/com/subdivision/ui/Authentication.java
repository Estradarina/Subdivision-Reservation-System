
package com.subdivision.ui;

import com.subdivision.dao.AdminDao;
import com.subdivision.dao.UserDao;
import com.subdivision.model.User;

import java.sql.SQLException;
import java.util.Scanner;

public class Authentication {

    private final UserDao userDao;
    private final AdminDao adminDao;
    private final Scanner scanner;

    public Authentication(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.adminDao = new AdminDao();
        this.scanner = scanner;
    }

    public void start() {

        boolean running = true;

        while (running) {

            System.out.println("\n===== SUBDIVISION RESERVATION SYSTEM =====");
            System.out.println("1. Register Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> register();
                case "2" -> login();

                case "3" -> {
                    running = false;
                    System.out.println("Goodbye!");
                }

                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void register() {

        try {
            System.out.println("\n===== REGISTER ACCOUNT =====");

            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.println("1. Staff");
            System.out.println("2. Resident");
            System.out.print("Choose role: ");

            String roleChoice = scanner.nextLine().trim();

            String role = switch (roleChoice) {
                case "1" -> "STAFF";
                case "2" -> "RESIDENT";
                default -> null;
            };

            if (username.isBlank()
                    || password.isBlank()
                    || role == null) {

                System.out.println("Invalid input. Please try again.");
                return;
            }

            User user = new User(
                    0, username, password, role, "PENDING"
            );

            if (userDao.register(user)) {
                System.out.println("Registration successful!");
                System.out.println("Username: " + username);
                System.out.println("Role: " + role);
                System.out.println("Status: PENDING");
                System.out.println(
                        "Please wait for administrator approval.");
            }

        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void login() {

        try {
            System.out.println("\n===== LOGIN =====");

            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (username.isBlank() || password.isBlank()) {
                System.out.println(
                        "Username and password are required.");
                return;
            }

            User user = userDao.login(username, password);

            if (user == null) {
                System.out.println(
                        "Login failed. Check your credentials "
                                + "or account approval status.");
                return;
            }

            System.out.println("Login successful!");
            System.out.println("Welcome, " + user.username() + "!");
            System.out.println("Role: " + user.roles());

            switch (user.roles().toUpperCase()) {
                case "ADMIN" -> adminMenu();
                case "STAFF", "RESIDENT" -> userMenu(user);
                default -> System.out.println("Unknown user role.");
            }

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    private void adminMenu() {
        AdminMenu menu = new AdminMenu(adminDao, scanner);
        menu.showMenu();
    }

    private void userMenu(User user) {

        boolean loggedIn = true;

        while (loggedIn) {

            System.out.println("\n===== " + user.roles() + " MENU =====");
            System.out.println("Logged in as: " + user.username());
            System.out.println("1. View Account Information");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.println("\n===== ACCOUNT INFORMATION =====");
                    System.out.println("User ID: " + user.id());
                    System.out.println("Username: " + user.username());
                    System.out.println("Role: " + user.roles());
                    System.out.println("Status: " + user.status());
                }

                case "2" -> {
                    loggedIn = false;
                    System.out.println("You have logged out successfully.");
                }

                default -> System.out.println("Invalid choice.");
            }
        }

        System.out.println("Returning to the main menu...");
    }
}
