package com.mlmsystem.util;

public class InputValidator {
    
    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 3 && username.length() <= 50 
               && username.matches("^[a-zA-Z0-9_]+ \u20B9");
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 3;
    }
    
    public static boolean isValidEmail(String email) {
        return email == null || email.isEmpty() || email.matches("^[A-Za-z0-9+_.-]+@(.+)\u20B9");
    }
    
    public static boolean isValidPhone(String phone) {
        return phone == null || phone.isEmpty() || phone.matches("^[0-9]{10,15}\u20B9");
    }
    
    public static boolean isValidName(String name) {
        return name != null && name.length() >= 2 && name.length() <= 100;
    }
    
    public static boolean isValidPrice(double price) {
        return price > 0;
    }
    
    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }
    
    public static boolean isValidLevel(int level) {
        return level >= 1 && level <= 3;
    }
}