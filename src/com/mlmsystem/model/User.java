package com.mlmsystem.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private int level;
    private int parentId;
    private double totalProfit;
    private Timestamp createdAt;
    
    public User() {}
    
    public User(String username, String password, String fullName, String email, 
                String phone, int level, int parentId) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.level = level;
        this.parentId = parentId;
        this.totalProfit = 0.0;
    }
    
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public int getParentId() { return parentId; }
    public void setParentId(int parentId) { this.parentId = parentId; }
    
    public double getTotalProfit() { return totalProfit; }
    public void setTotalProfit(double totalProfit) { this.totalProfit = totalProfit; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return String.format("User ID: %d, Username: %s, Level: %d, Parent ID: %d, Total Profit: \u20B9%.2f",
            userId, username, level, parentId, totalProfit);
    }
}