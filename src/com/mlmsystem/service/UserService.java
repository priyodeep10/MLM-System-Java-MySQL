package com.mlmsystem.service;

import com.mlmsystem.dao.UserDAO;
import com.mlmsystem.model.User;
import com.mlmsystem.util.ConsoleUtil;

import java.util.List;

public class UserService {
    private UserDAO userDAO;
    private AuthService authService;
    private ProductService productService;
    private SaleService saleService;
    
    public UserService() {
        this.userDAO = new UserDAO();
        this.authService = new AuthService();
        this.productService = new ProductService();
        this.saleService = new SaleService();
    }
    
    public void showUserDashboard(User currentUser) {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("USER DASHBOARD - " + currentUser.getFullName());
        System.out.println("Level: " + currentUser.getLevel());
        System.out.printf("Total Profit Earned: \u20B9%.2f%n", currentUser.getTotalProfit());
        System.out.println("\n");
    }
    
    public void viewMyProfile(User user) {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("MY PROFILE");
        
        System.out.println("User ID: " + user.getUserId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Full Name: " + user.getFullName());
        System.out.println("Email: " + (user.getEmail() == null || user.getEmail().isEmpty() ? "N/A" : user.getEmail()));
        System.out.println("Phone: " + (user.getPhone() == null || user.getPhone().isEmpty() ? "N/A" : user.getPhone()));
        System.out.println("Level: " + user.getLevel());
        System.out.println("Parent ID: " + (user.getParentId() == 0 ? "Admin" : user.getParentId()));
        System.out.printf("Total Profit: \u20B9%.2f%n", user.getTotalProfit());
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    public void viewMyDownline(User user) {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("MY DOWNLINE");
        
        List<User> children = userDAO.getChildrenUsers(user.getUserId());
        
        if (children.isEmpty()) {
            ConsoleUtil.printInfo("You have no downline users yet.");
        } else {
            System.out.println("Your downline users:");
            System.out.println("ID  | Username       | Full Name           | Level | Profit");
            // Java 8 compatible line drawing
            for (int i = 0; i < 70; i++) {
                System.out.print("-");
            }
            System.out.println();
            
            for (User child : children) {
                System.out.printf("%-4d| %-14s| %-20s| %-6d| \u20B9%-8.2f%n",
                    child.getUserId(), child.getUsername(), child.getFullName(),
                    child.getLevel(), child.getTotalProfit());
            }
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    public void showUserMenu(User currentUser) {
        while (true) {
            showUserDashboard(currentUser);
            System.out.println("1. View My Profile");
            System.out.println("2. View Products");
            System.out.println("3. Sell Product");
            System.out.println("4. View My Sales");
            System.out.println("5. View My Downline");
            System.out.println("6. Add New User to My Downline");
            System.out.println("7. View My Profit Distribution");
            System.out.println("8. Logout");
            
            int choice = ConsoleUtil.readInt("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    viewMyProfile(currentUser);
                    break;
                case 2:
                    productService.viewProducts();
                    break;
                case 3:
                    saleService.sellProductAsUser(currentUser);
                    break;
                case 4:
                    saleService.viewMySales(currentUser);
                    break;
                case 5:
                    viewMyDownline(currentUser);
                    break;
                case 6:
                    User newUser = authService.registerUser(currentUser);
                    if (newUser != null) {
                        ConsoleUtil.pressEnterToContinue();
                    }
                    break;
                case 7:
                    saleService.viewMyProfitDistribution(currentUser);
                    break;
                case 8:
                    ConsoleUtil.printInfo("Logging out...");
                    return;
                default:
                    ConsoleUtil.printError("Invalid choice!");
                    break;
            }
        }
    }
}