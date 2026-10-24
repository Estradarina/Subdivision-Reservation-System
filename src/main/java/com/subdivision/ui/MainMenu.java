
package com.subdivision.ui;

import com.subdivision.dao.UserDao;
import com.subdivision.dao.AdminDao;
import com.subdivision.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDao userDao = new UserDao();
    private static final AdminDao adminDao = new AdminDao();

    public static void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== SUBDIVISION RESERVATION SYSTEM ===");
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

    private static void register() {
        try {
            System.out.println("\n--- REGISTER ACCOUNT ---");

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

            if (username.isBlank() || password.isBlank()
                    || role == null) {
                System.out.println("Invalid input. Please try again.");
                return;
            }

            User user = new User(
                    0, username, password, role, "PENDING"
            );

            if (userDao.register(user)) {
                System.out.println("Registration successful!");
                System.out.println("Account status: PENDING");
                System.out.println("Wait for administrator approval.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Registration failed. The username may already exist."
            );
        }
    }

    private static void login() {
        try {
            System.out.println("\n--- LOGIN ---");

            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            User user = userDao.login(username, password);

            if (user == null) {
                System.out.println(
                        "Login failed. Check your credentials "
                                + "or account status."
                );
                return;
            }

            System.out.println("Login successful!");
            System.out.println("Welcome, " + user.username() + "!");
            System.out.println("Role: " + user.roles());

            // Redirect based on the role stored in the database
            if ("ADMIN".equalsIgnoreCase(user.roles())) {
                adminMenu();
            } else {
                userMenu(user);
            }

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    private static void adminMenu() {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. View Pending Accounts");
            System.out.println("2. Approve Account");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewPendingAccounts();
                case "2" -> approveAccount();
                case "3" -> {
                    loggedIn = false;
                    System.out.println("Admin logged out successfully.");
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewPendingAccounts() {
        try {
            List<User> users = adminDao.getPendingUsers();

            System.out.println("\n--- PENDING ACCOUNTS ---");

            if (users.isEmpty()) {
                System.out.println("No pending accounts found.");
                return;
            }

            for (User user : users) {
                System.out.println(
                        "ID: " + user.id()
                                + " | Username: " + user.username()
                                + " | Role: " + user.roles()
                                + " | Status: " + user.status()
                );
            }

        } catch (SQLException e) {
            System.out.println(
                    "Unable to retrieve pending accounts: "
                            + e.getMessage()
            );
        }
    }

    private static void approveAccount() {
        try {
            System.out.println("\n--- APPROVE ACCOUNT ---");

            List<User> users = adminDao.getPendingUsers();

            if (users.isEmpty()) {
                System.out.println("No pending accounts to approve.");
                return;
            }

            for (User user : users) {
                System.out.println(
                        "Username: " + user.username()
                                + " | Role: " + user.roles()
                );
            }

            System.out.print("Enter username to approve: ");
            String username = scanner.nextLine().trim();

            if (username.isBlank()) {
                System.out.println("Username cannot be empty.");
                return;
            }

            boolean approved = adminDao.approveUser(username);

            if (approved) {
                System.out.println(
                        "Account approved successfully! Status: ACTIVE"
                );
            } else {
                System.out.println(
                        "Approval failed. Check the username and status."
                );
            }

        } catch (SQLException e) {
            System.out.println(
                    "Unable to approve account: " + e.getMessage()
            );
        }
    }

    private static void userMenu(User user) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("Logged in as: " + user.username());
            System.out.println("Role: " + user.roles());
            System.out.println("1. View Account Information");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.println("\n--- ACCOUNT INFORMATION ---");
                    System.out.println("Username: " + user.username());
                    System.out.println("Role: " + user.roles());
                    System.out.println("Status: " + user.status());
                }
                case "2" -> {
                    loggedIn = false;
                    System.out.println(
                            "You have logged out successfully."
                    );
                }
                default -> System.out.println("Invalid choice.");
            }
        }

        System.out.println("Returning to the main menu...");
    }
}
