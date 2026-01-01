package com.mlmsystem.model;

import java.sql.Timestamp;

public class Admin {
    private int adminId;
    private String username;
    private String password;
    private double totalProfit;
    private Timestamp createdAt;
    
    public Admin() {}
    
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
        this.totalProfit = 0.0;
    }
    
    // Getters and Setters
    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public double getTotalProfit() { return totalProfit; }
    public void setTotalProfit(double totalProfit) { this.totalProfit = totalProfit; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}