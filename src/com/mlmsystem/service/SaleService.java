package com.mlmsystem.service;

import com.mlmsystem.dao.*;
import com.mlmsystem.model.Product;
import com.mlmsystem.model.Sale;
import com.mlmsystem.model.User;
import com.mlmsystem.model.ProfitDistribution;
import com.mlmsystem.util.ConsoleUtil;
import com.mlmsystem.util.DateUtil;
import com.mlmsystem.util.InputValidator;

import java.util.List;

public class SaleService {
    private SaleDAO saleDAO;
    private ProductDAO productDAO;
    private ProfitDAO profitDAO;
    private ProductService productService;
    
    public SaleService() {
        this.saleDAO = new SaleDAO();
        this.productDAO = new ProductDAO();
        this.profitDAO = new ProfitDAO();
        this.productService = new ProductService();
    }
    
    public void sellProductAsAdmin() {
        productService.viewProducts();
        
        int productId = ConsoleUtil.readInt("\nEnter Product ID to sell: ");
        Product product = productDAO.getProductById(productId);
        
        if (product == null) {
            ConsoleUtil.printError("Product not found!");
            ConsoleUtil.pressEnterToContinue();
            return;
        }
        
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("SELL PRODUCT AS ADMIN");
        System.out.println("Product: " + product.getName());
        System.out.println("Price: \u20B9" + product.getPrice());
        System.out.println("Available Stock: " + product.getStockQuantity());
        
        int quantity = 0;
        while (true) {
            quantity = ConsoleUtil.readInt("\nEnter quantity to sell: ");
            if (!InputValidator.isValidQuantity(quantity)) {
                ConsoleUtil.printError("Quantity must be greater than 0.");
                continue;
            }
            if (quantity > product.getStockQuantity()) {
                ConsoleUtil.printError("Insufficient stock. Available: " + product.getStockQuantity());
                continue;
            }
            break;
        }
        
        double totalAmount = product.getPrice() * quantity;
        
        System.out.println("\nSale Summary:");
        System.out.println("Product: " + product.getName());
        System.out.println("Quantity: " + quantity);
        System.out.println("Unit Price: \u20B9" + product.getPrice());
        System.out.println("Total Amount: \u20B9" + totalAmount);
        System.out.println("Profit Distribution: Admin - 100%");
        
        String confirm = ConsoleUtil.readString("\nConfirm sale? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            // Update stock
            if (!productService.updateProductStock(productId, -quantity)) {
                ConsoleUtil.printError("Failed to update stock!");
                ConsoleUtil.pressEnterToContinue();
                return;
            }
            
            // Record sale
            Sale sale = new Sale(productId, 1, "admin", quantity, totalAmount);
            int saleId = saleDAO.recordSale(sale);
            
            if (saleId > 0) {
                // Distribute profit (Admin gets 100%)
                saleDAO.distributeProfit(saleId, 1, "admin", totalAmount);
                
                ConsoleUtil.printSuccess("Sale completed successfully!");
                System.out.println("Sale ID: " + saleId);
                System.out.println("Total Amount: \u20B9" + totalAmount);
                System.out.println("Admin profit: \u20B9" + totalAmount);
            } else {
                ConsoleUtil.printError("Failed to record sale!");
            }
        } else {
            ConsoleUtil.printInfo("Sale cancelled.");
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    public void sellProductAsUser(User user) {
        productService.viewProducts();
        
        int productId = ConsoleUtil.readInt("\nEnter Product ID to sell: ");
        Product product = productDAO.getProductById(productId);
        
        if (product == null) {
            ConsoleUtil.printError("Product not found!");
            ConsoleUtil.pressEnterToContinue();
            return;
        }
        
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("SELL PRODUCT AS USER");
        System.out.println("Seller: " + user.getFullName() + " (Level " + user.getLevel() + ")");
        System.out.println("Product: " + product.getName());
        System.out.println("Price: \u20B9" + product.getPrice());
        System.out.println("Available Stock: " + product.getStockQuantity());
        
        int quantity = 0;
        while (true) {
            quantity = ConsoleUtil.readInt("\nEnter quantity to sell: ");
            if (!InputValidator.isValidQuantity(quantity)) {
                ConsoleUtil.printError("Quantity must be greater than 0.");
                continue;
            }
            if (quantity > product.getStockQuantity()) {
                ConsoleUtil.printError("Insufficient stock. Available: " + product.getStockQuantity());
                continue;
            }
            break;
        }
        
        double totalAmount = product.getPrice() * quantity;
        
        System.out.println("\nSale Summary:");
        System.out.println("Product: " + product.getName());
        System.out.println("Quantity: " + quantity);
        System.out.println("Unit Price: \u20B9" + product.getPrice());
        System.out.println("Total Amount: \u20B9" + totalAmount);
        
        // Show profit distribution based on user level
        System.out.println("\nProfit Distribution:");
        switch (user.getLevel()) {
            case 1:
                System.out.println("Admin: 10% (\u20B9" + (totalAmount * 0.10) + ")");
                System.out.println("You (Level 1): 90% (\u20B9" + (totalAmount * 0.90) + ")");
                break;
            case 2:
                System.out.println("Admin: 5% (\u20B9" + (totalAmount * 0.05) + ")");
                System.out.println("Your Recruiter (Level 1): 5% (\u20B9" + (totalAmount * 0.05) + ")");
                System.out.println("You (Level 2): 90% (\u20B9" + (totalAmount * 0.90) + ")");
                break;
            case 3:
                System.out.println("Admin: 3.33% (\u20B9" + (totalAmount * 0.0333) + ")");
                System.out.println("Your Recruiter's Recruiter (Level 1): 3.33% (\u20B9" + (totalAmount * 0.0333) + ")");
                System.out.println("Your Recruiter (Level 2): 3.33% (\u20B9" + (totalAmount * 0.0333) + ")");
                System.out.println("You (Level 3): 90% (\u20B9" + (totalAmount * 0.90) + ")");
                break;
        }
        
        String confirm = ConsoleUtil.readString("\nConfirm sale? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            // Update stock
            if (!productService.updateProductStock(productId, -quantity)) {
                ConsoleUtil.printError("Failed to update stock!");
                ConsoleUtil.pressEnterToContinue();
                return;
            }
            
            // Record sale
            Sale sale = new Sale(productId, user.getUserId(), "user", quantity, totalAmount);
            int saleId = saleDAO.recordSale(sale);
            
            if (saleId > 0) {
                // Distribute profit based on user level
                saleDAO.distributeProfit(saleId, user.getUserId(), "user", totalAmount);
                
                ConsoleUtil.printSuccess("Sale completed successfully!");
                System.out.println("Sale ID: " + saleId);
                System.out.println("Total Amount: \u20B9" + totalAmount);
                System.out.println("Your profit: \u20B9" + 
                    (user.getLevel() == 1 ? totalAmount * 0.90 :
                     user.getLevel() == 2 ? totalAmount * 0.90 :
                     totalAmount * 0.90));
            } else {
                ConsoleUtil.printError("Failed to record sale!");
            }
        } else {
            ConsoleUtil.printInfo("Sale cancelled.");
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    public void viewAllSales() {
        List<Sale> sales = saleDAO.getAllSales();
        
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("ALL SALES");
        
        if (sales.isEmpty()) {
            ConsoleUtil.printInfo("No sales found.");
        } else {
            System.out.println("Sale ID | Product ID | Seller Type | Seller ID | Quantity | Total Amount | Date");
            // Fix for Java 8: Replace .repeat() with loop
            for (int i = 0; i < 100; i++) {
                System.out.print("-");
            }
            System.out.println();
            
            for (Sale sale : sales) {
                System.out.printf("%-8d| %-11d| %-12s| %-10d| %-9d| %-12.2f| %s%n",
                    sale.getSaleId(), sale.getProductId(), sale.getSellerType(),
                    sale.getSellerId(), sale.getQuantity(), sale.getTotalAmount(),
                    DateUtil.formatDate(sale.getSaleDate()));
            }
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    public void viewMySales(User user) {
        List<Sale> sales = saleDAO.getSalesBySeller(user.getUserId(), "user");
        
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("MY SALES");
        
        if (sales.isEmpty()) {
            ConsoleUtil.printInfo("You have no sales yet.");
        } else {
            System.out.println("Sale ID | Product ID | Quantity | Total Amount | Date");
            // Fix for Java 8: Replace .repeat() with loop
            for (int i = 0; i < 70; i++) {
                System.out.print("-");
            }
            System.out.println();
            
            for (Sale sale : sales) {
                System.out.printf("%-8d| %-11d| %-9d| \u20B9%-12.2f| %s%n",
                    sale.getSaleId(), sale.getProductId(),
                    sale.getQuantity(), sale.getTotalAmount(),
                    DateUtil.formatDate(sale.getSaleDate()));
            }
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    public void viewProfitDistribution() {
        List<ProfitDistribution> distributions = profitDAO.getAllProfitDistributions();
        
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("ALL PROFIT DISTRIBUTIONS");
        
        if (distributions.isEmpty()) {
            ConsoleUtil.printInfo("No profit distributions found.");
        } else {
            System.out.println("Distribution ID | Sale ID | Receiver Type | Receiver ID | Amount   | %     | Level | Date");
            // Fix for Java 8: Replace .repeat() with loop
            for (int i = 0; i < 100; i++) {
                System.out.print("-");
            }
            System.out.println();
            
            for (ProfitDistribution pd : distributions) {
                System.out.printf("%-16d| %-8d| %-13s| %-12d| \u20B9%-8.2f| %-6.2f| %-6s| %s%n",
                    pd.getDistributionId(), pd.getSaleId(), pd.getReceiverType(),
                    pd.getReceiverId(), pd.getAmount(), pd.getPercentage(),
                    pd.getLevel() == null ? "N/A" : pd.getLevel(),
                    DateUtil.formatDate(pd.getDistributionDate()));
            }
            
            double total = profitDAO.getTotalProfitDistributed();
            System.out.printf("%nTotal Profit Distributed: \u20B9%.2f%n", total);
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    public void viewMyProfitDistribution(User user) {
        List<ProfitDistribution> distributions = profitDAO.getProfitByReceiver(user.getUserId(), "user");
        
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("MY PROFIT DISTRIBUTIONS");
        
        if (distributions.isEmpty()) {
            ConsoleUtil.printInfo("You have no profit distributions yet.");
        } else {
            double totalProfit = 0;
            System.out.println("Distribution ID | Sale ID | Amount   | %     | Level | Date");
            // Fix for Java 8: Replace .repeat() with loop
            for (int i = 0; i < 80; i++) {
                System.out.print("-");
            }
            System.out.println();
            
            for (ProfitDistribution pd : distributions) {
                System.out.printf("%-16d| %-8d| \u20B9%-8.2f| %-6.2f| %-6s| %s%n",
                    pd.getDistributionId(), pd.getSaleId(), pd.getAmount(),
                    pd.getPercentage(), pd.getLevel(), DateUtil.formatDate(pd.getDistributionDate()));
                totalProfit += pd.getAmount();
            }
            
            System.out.printf("%nTotal Profit Earned: \u20B9%.2f%n", totalProfit);
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
}