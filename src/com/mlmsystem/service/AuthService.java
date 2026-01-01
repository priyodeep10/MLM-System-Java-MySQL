package com.mlmsystem.service;

import com.mlmsystem.dao.AdminDAO;
import com.mlmsystem.dao.UserDAO;
import com.mlmsystem.model.Admin;
import com.mlmsystem.model.User;
import com.mlmsystem.util.ConsoleUtil;
import com.mlmsystem.util.InputValidator;

public class AuthService {
    private AdminDAO adminDAO;
    private UserDAO userDAO;
    
    public AuthService() {
        this.adminDAO = new AdminDAO();
        this.userDAO = new UserDAO();
    }
    
    public Admin authenticateAdmin() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("ADMIN LOGIN");
        
        String username = ConsoleUtil.readString("Username: ");
        String password = ConsoleUtil.readString("Password: ");
        
        Admin admin = adminDAO.authenticate(username, password);
        if (admin != null) {
            ConsoleUtil.printSuccess("Login successful! Welcome Admin.");
            return admin;
        } else {
            ConsoleUtil.printError("Invalid admin credentials!");
            return null;
        }
    }
    
    public User authenticateUser() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("USER LOGIN");
        
        String username = ConsoleUtil.readString("Username: ");
        String password = ConsoleUtil.readString("Password: ");
        
        User user = userDAO.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            ConsoleUtil.printSuccess("Login successful! Welcome " + user.getFullName());
            return user;
        } else {
            ConsoleUtil.printError("Invalid username or password!");
            return null;
        }
    }
    
    public User registerUser(User parentUser) {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("REGISTER NEW USER");
        
        String username = "";
        while (true) {
            username = ConsoleUtil.readString("Enter username: ");
            if (!InputValidator.isValidUsername(username)) {
                ConsoleUtil.printError("Username must be 3-50 characters and contain only letters, numbers, and underscores.");
                continue;
            }
            if (userDAO.usernameExists(username)) {
                ConsoleUtil.printError("Username already exists. Please choose another.");
                continue;
            }
            break;
        }
        
        String password = "";
        while (true) {
            password = ConsoleUtil.readString("Enter password: ");
            if (!InputValidator.isValidPassword(password)) {
                ConsoleUtil.printError("Password must be at least 3 characters long.");
                continue;
            }
            break;
        }
        
        String fullName = "";
        while (true) {
            fullName = ConsoleUtil.readString("Enter full name: ");
            if (!InputValidator.isValidName(fullName)) {
                ConsoleUtil.printError("Full name must be 2-100 characters.");
                continue;
            }
            break;
        }
        
        String email = "";
        while (true) {
            email = ConsoleUtil.readString("Enter email (optional): ");
            if (!email.isEmpty() && !InputValidator.isValidEmail(email)) {
                ConsoleUtil.printError("Invalid email format.");
                continue;
            }
            break;
        }
        
        String phone = "";
        while (true) {
            phone = ConsoleUtil.readString("Enter phone (optional): ");
            if (!phone.isEmpty() && !InputValidator.isValidPhone(phone)) {
                ConsoleUtil.printError("Phone must be 10-15 digits.");
                continue;
            }
            break;
        }
        
        int newUserLevel = parentUser.getLevel() + 1;
        if (newUserLevel > 3) {
            ConsoleUtil.printError("Cannot add more than 4 levels (Admin → L1 → L2 → L3).");
            return null;
        }
        
        User newUser = new User(username, password, fullName, email, phone, newUserLevel, parentUser.getUserId());
        
        if (userDAO.addUser(newUser)) {
            ConsoleUtil.printSuccess("User registered successfully! Assigned to Level " + newUserLevel);
            return newUser;
        } else {
            ConsoleUtil.printError("Failed to register user.");
            return null;
        }
    }
}