package com.mlmsystem.model;

import java.sql.Timestamp;

public class Sale {
    private int saleId;
    private int productId;
    private int sellerId;
    private String sellerType;
    private int quantity;
    private double totalAmount;
    private Timestamp saleDate;
    
    public Sale() {}
    
    public Sale(int productId, int sellerId, String sellerType, int quantity, double totalAmount) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.sellerType = sellerType;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }
    
    // Getters and Setters
    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
    
    public String getSellerType() { return sellerType; }
    public void setSellerType(String sellerType) { this.sellerType = sellerType; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public Timestamp getSaleDate() { return saleDate; }
    public void setSaleDate(Timestamp saleDate) { this.saleDate = saleDate; }
}