package com.mlmsystem.service;

import com.mlmsystem.dao.AdminDAO;
import com.mlmsystem.dao.UserDAO;
import com.mlmsystem.model.User;
import com.mlmsystem.util.ConsoleUtil;
import com.mlmsystem.util.DateUtil;

import java.util.List;

public class AdminService {
    private AdminDAO adminDAO;
    private UserDAO userDAO;
    
    public AdminService() {
        this.adminDAO = new AdminDAO();
        this.userDAO = new UserDAO();
    }
    
    public void showDashboard() {
        double totalProfit = adminDAO.getAdminTotalProfit();
        
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("ADMIN DASHBOARD");
        System.out.printf("Total Admin Profit: \u20B9%.2f%n", totalProfit);
        System.out.println("Current Date/Time: " + DateUtil.getCurrentDateTime());
        System.out.println("\n");
    }
    
    public void viewAllUsers() {
        List<User> users = userDAO.getAllUsers();
        
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("ALL USERS");
        
        if (users.isEmpty()) {
            ConsoleUtil.printInfo("No users found.");
        } else {
            System.out.println("ID  | Username       | Full Name           | Level | Parent ID | Profit");
            for (int i = 0; i < 80; i++) System.out.print("-");
System.out.println();
            for (User user : users) {
                System.out.printf("%-4d| %-14s| %-20s| %-6d| %-10d| \u20B9%-8.2f%n",
                    user.getUserId(), user.getUsername(), user.getFullName(),
                    user.getLevel(), user.getParentId(), user.getTotalProfit());
            }
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    public void addLevel1User() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("ADD LEVEL 1 USER");
        
        String username = ConsoleUtil.readString("Enter username: ");
        if (userDAO.usernameExists(username)) {
            ConsoleUtil.printError("Username already exists!");
            ConsoleUtil.pressEnterToContinue();
            return;
        }
        
        String password = ConsoleUtil.readString("Enter password: ");
        String fullName = ConsoleUtil.readString("Enter full name: ");
        String email = ConsoleUtil.readString("Enter email (optional): ");
        String phone = ConsoleUtil.readString("Enter phone (optional): ");
        
        // Level 1 users have parent_id = 0 (no parent)
        User newUser = new User(username, password, fullName, email, phone, 1, 0);
        
        if (userDAO.addUser(newUser)) {
            ConsoleUtil.printSuccess("Level 1 user added successfully!");
        } else {
            ConsoleUtil.printError("Failed to add user!");
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    public void viewUserHierarchy() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("USER HIERARCHY");
        
        List<User> level1Users = userDAO.getUsersByLevel(1);
        
        if (level1Users.isEmpty()) {
            ConsoleUtil.printInfo("No users found.");
        } else {
            for (User level1User : level1Users) {
                System.out.println("\nLevel 1: " + level1User.getFullName() + " (" + level1User.getUsername() + ")");
                
                List<User> level2Users = userDAO.getChildrenUsers(level1User.getUserId());
                for (User level2User : level2Users) {
                    System.out.println("  ├── Level 2: " + level2User.getFullName() + " (" + level2User.getUsername() + ")");
                    
                    List<User> level3Users = userDAO.getChildrenUsers(level2User.getUserId());
                    for (User level3User : level3Users) {
                        System.out.println("  │     └── Level 3: " + level3User.getFullName() + " (" + level3User.getUsername() + ")");
                    }
                }
            }
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    public void showAdminMenu() {
        ProductService productService = new ProductService();
        SaleService saleService = new SaleService();
        
        while (true) {
            showDashboard();
            System.out.println("1. View All Users");
            System.out.println("2. Add Level 1 User");
            System.out.println("3. View User Hierarchy");
            System.out.println("4. Manage Products");
            System.out.println("5. Sell Product (as Admin)");
            System.out.println("6. View All Sales");
            System.out.println("7. View Profit Distribution");
            System.out.println("8. View Admin Profit");
            System.out.println("9. Logout");
            
            int choice = ConsoleUtil.readInt("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    addLevel1User();
                    break;
                case 3:
                    viewUserHierarchy();
                    break;
                case 4:
                    productService.manageProducts();
                    break;
                case 5:
                    saleService.sellProductAsAdmin();
                    break;
                case 6:
                    saleService.viewAllSales();
                    break;
                case 7:
                    saleService.viewProfitDistribution();
                    break;
                case 8:
                    viewAdminProfitDetails();
                    break;
                case 9:
                    ConsoleUtil.printInfo("Logging out...");
                    return;
                default:
                    ConsoleUtil.printError("Invalid choice!");
                    break;
            }
        }
    }
    
    private void viewAdminProfitDetails() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("ADMIN PROFIT DETAILS");
        
        double totalProfit = adminDAO.getAdminTotalProfit();
        System.out.printf("Total Profit Earned: \u20B9%.2f%n%n", totalProfit);
        
        ConsoleUtil.pressEnterToContinue();
    }
}