package com.mlmsystem;

import com.mlmsystem.model.Admin;
import com.mlmsystem.model.User;
import com.mlmsystem.service.AuthService;
import com.mlmsystem.service.AdminService;
import com.mlmsystem.service.UserService;
import com.mlmsystem.util.ConsoleUtil;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Main {
    public static void main(String[] args) {
        try {
            // Set console encoding to UTF-8
            System.setProperty("file.encoding", "UTF-8");
            try {
                System.setOut(new PrintStream(System.out, true, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                System.err.println("Warning: UTF-8 encoding not supported, some characters may not display correctly");
            }
            // Initialize database connection
            DatabaseConnection.getConnection();
            
            System.out.println("========================================");
            System.out.println("  MULTILEVEL MARKETING SYSTEM (4 LEVELS)");
            System.out.println("========================================");
            
            boolean exit = false;
            
            while (!exit) {
                ConsoleUtil.clearScreen();
                ConsoleUtil.printHeader("MAIN MENU");
                System.out.println("1. Admin Login");
                System.out.println("2. User Login");
                System.out.println("3. Exit");
                
                int choice = ConsoleUtil.readInt("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        AuthService authService = new AuthService();
                        Admin admin = authService.authenticateAdmin();
                        if (admin != null) {
                            AdminService adminService = new AdminService();
                            adminService.showAdminMenu();
                        }
                        break;
                    case 2:
                        AuthService authService2 = new AuthService();
                        User user = authService2.authenticateUser();
                        if (user != null) {
                            UserService userService = new UserService();
                            userService.showUserMenu(user);
                        }
                        break;
                    case 3:
                        exit = true;
                        ConsoleUtil.printInfo("Thank you for using MLM System!");
                        break;
                    default:
                        ConsoleUtil.printError("Invalid choice!");
                        break;
                }
            }
            
        } catch (Exception e) {
            System.err.println("System error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
            ConsoleUtil.closeScanner();
        }
    }
}