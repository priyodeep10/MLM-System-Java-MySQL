package com.mlmsystem.model;

import java.sql.Timestamp;

public class ProfitDistribution {
    private int distributionId;
    private int saleId;
    private int receiverId;
    private String receiverType;
    private double amount;
    private double percentage;
    private Integer level;
    private Timestamp distributionDate;
    
    public ProfitDistribution() {}
    
    // Getters and Setters
    public int getDistributionId() { return distributionId; }
    public void setDistributionId(int distributionId) { this.distributionId = distributionId; }
    
    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }
    
    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }
    
    public String getReceiverType() { return receiverType; }
    public void setReceiverType(String receiverType) { this.receiverType = receiverType; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }
    
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    
    public Timestamp getDistributionDate() { return distributionDate; }
    public void setDistributionDate(Timestamp distributionDate) { this.distributionDate = distributionDate; }
}