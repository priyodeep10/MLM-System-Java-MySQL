package com.mlmsystem.dao;

import com.mlmsystem.DatabaseConnection;
import com.mlmsystem.model.ProfitDistribution;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfitDAO {
    
    public List<ProfitDistribution> getProfitBySale(int saleId) {
        List<ProfitDistribution> distributions = new ArrayList<>();
        String sql = "SELECT * FROM profit_distribution WHERE sale_id = ? ORDER BY level";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, saleId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                distributions.add(extractProfitFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting profit by sale: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
        }
        
        return distributions;
    }
    
    public List<ProfitDistribution> getProfitByReceiver(int receiverId, String receiverType) {
        List<ProfitDistribution> distributions = new ArrayList<>();
        String sql = "SELECT * FROM profit_distribution WHERE receiver_id = ? AND receiver_type = ? ORDER BY distribution_date DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, receiverId);
            pstmt.setString(2, receiverType);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                distributions.add(extractProfitFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting profit by receiver: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
        }
        
        return distributions;
    }
    
    public List<ProfitDistribution> getAllProfitDistributions() {
        List<ProfitDistribution> distributions = new ArrayList<>();
        String sql = "SELECT * FROM profit_distribution ORDER BY distribution_date DESC";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                distributions.add(extractProfitFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all profit distributions: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
        }
        
        return distributions;
    }
    
    public double getTotalProfitDistributed() {
        String sql = "SELECT SUM(amount) FROM profit_distribution";
        double total = 0.0;
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                total = rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total profit distributed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
        }
        
        return total;
    }
    
    private ProfitDistribution extractProfitFromResultSet(ResultSet rs) throws SQLException {
        ProfitDistribution pd = new ProfitDistribution();
        pd.setDistributionId(rs.getInt("distribution_id"));
        pd.setSaleId(rs.getInt("sale_id"));
        pd.setReceiverId(rs.getInt("receiver_id"));
        pd.setReceiverType(rs.getString("receiver_type"));
        pd.setAmount(rs.getDouble("amount"));
        pd.setPercentage(rs.getDouble("percentage"));
        pd.setLevel(rs.getInt("level"));
        pd.setDistributionDate(rs.getTimestamp("distribution_date"));
        return pd;
    }
}