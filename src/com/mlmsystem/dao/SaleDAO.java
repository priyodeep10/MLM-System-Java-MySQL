package com.mlmsystem.dao;

import com.mlmsystem.DatabaseConnection;
import com.mlmsystem.model.Sale;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {
    
    public int recordSale(Sale sale) {
        String sql = "INSERT INTO sales (product_id, seller_id, seller_type, quantity, total_amount) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, sale.getProductId());
            pstmt.setInt(2, sale.getSellerId());
            pstmt.setString(3, sale.getSellerType());
            pstmt.setInt(4, sale.getQuantity());
            pstmt.setDouble(5, sale.getTotalAmount());
            
            int rows = pstmt.executeUpdate();
            
            if (rows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error recording sale: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
        }
        
        return generatedId;
    }
    
    public void distributeProfit(int saleId, int sellerId, String sellerType, double totalAmount) {
        String sql = "{call DistributeProfit(?, ?, ?, ?)}";
        
        Connection conn = null;
        CallableStatement cstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            cstmt = conn.prepareCall(sql);
            
            cstmt.setInt(1, saleId);
            cstmt.setInt(2, sellerId);
            cstmt.setString(3, sellerType);
            cstmt.setDouble(4, totalAmount);
            
            cstmt.execute();
            
        } catch (SQLException e) {
            System.err.println("Error distributing profit: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (cstmt != null) cstmt.close(); } catch (SQLException e) {}
        }
    }
    
    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales ORDER BY sale_date DESC";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                sales.add(extractSaleFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all sales: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
        }
        
        return sales;
    }
    
    public List<Sale> getSalesBySeller(int sellerId, String sellerType) {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales WHERE seller_id = ? AND seller_type = ? ORDER BY sale_date DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sellerId);
            pstmt.setString(2, sellerType);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                sales.add(extractSaleFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting sales by seller: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
        }
        
        return sales;
    }
    
    private Sale extractSaleFromResultSet(ResultSet rs) throws SQLException {
        Sale sale = new Sale();
        sale.setSaleId(rs.getInt("sale_id"));
        sale.setProductId(rs.getInt("product_id"));
        sale.setSellerId(rs.getInt("seller_id"));
        sale.setSellerType(rs.getString("seller_type"));
        sale.setQuantity(rs.getInt("quantity"));
        sale.setTotalAmount(rs.getDouble("total_amount"));
        sale.setSaleDate(rs.getTimestamp("sale_date"));
        return sale;
    }
}