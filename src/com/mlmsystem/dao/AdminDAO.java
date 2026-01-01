package com.mlmsystem.dao;

import com.mlmsystem.DatabaseConnection;
import com.mlmsystem.model.Admin;
import java.sql.*;

public class AdminDAO {
    
    public Admin authenticate(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        Admin admin = null;
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setTotalProfit(rs.getDouble("total_profit"));
                admin.setCreatedAt(rs.getTimestamp("created_at"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating admin: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
        }
        
        return admin;
    }
    
    public boolean updateAdminProfit(double profit) {
        String sql = "UPDATE admin SET total_profit = total_profit + ? WHERE admin_id = 1";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, profit);
            int rows = pstmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating admin profit: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Close resources
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
        }
    }
    
    public double getAdminTotalProfit() {
        String sql = "SELECT total_profit FROM admin WHERE admin_id = 1";
        double profit = 0.0;
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                profit = rs.getDouble("total_profit");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting admin profit: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
        }
        
        return profit;
    }
}