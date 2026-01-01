package com.mlmsystem.service;

import com.mlmsystem.dao.ProductDAO;
import com.mlmsystem.model.Product;
import com.mlmsystem.util.ConsoleUtil;
import com.mlmsystem.util.InputValidator;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO;
    
    public ProductService() {
        this.productDAO = new ProductDAO();
    }
    
    public void manageProducts() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("PRODUCT MANAGEMENT");
            System.out.println("1. View All Products");
            System.out.println("2. Add New Product");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Update Stock");
            System.out.println("6. Back to Main Menu");
            
            int choice = ConsoleUtil.readInt("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    viewProducts();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    updateStock();
                    break;
                case 6:
                    return;
                default:
                    ConsoleUtil.printError("Invalid choice!");
                    break;
            }
        }
    }
    
    public void viewProducts() {
        List<Product> products = productDAO.getAllProducts();
        
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("PRODUCT LIST");
        
        if (products.isEmpty()) {
            ConsoleUtil.printInfo("No products found.");
        } else {
            ConsoleUtil.out.println("ID  | Name                    | Price      | Stock | Description");
            
            for (Product product : products) {
                String priceStr = ConsoleUtil.formatCurrency(product.getPrice());
                String description = product.getDescription().length() > 50 
                    ? product.getDescription().substring(0, 47) + "..." 
                    : product.getDescription();
                
                ConsoleUtil.out.println(String.format("%-4d| %-23s| %-10s | %-5d | %s",
                    product.getProductId(), 
                    product.getName(),
                    priceStr,
                    product.getStockQuantity(),
                    description));
            }
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    private void addProduct() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("ADD NEW PRODUCT");
        
        String name = ConsoleUtil.readString("Product Name: ");
        String description = ConsoleUtil.readString("Description: ");
        
        double price = 0;
        while (true) {
            price = ConsoleUtil.readDouble("Price: " + ConsoleUtil.RUPEE_SYMBOL);
            if (!InputValidator.isValidPrice(price)) {
                ConsoleUtil.printError("Price must be greater than 0.");
                continue;
            }
            break;
        }
        
        int stock = 0;
        while (true) {
            stock = ConsoleUtil.readInt("Initial Stock Quantity: ");
            if (!InputValidator.isValidQuantity(stock)) {
                ConsoleUtil.printError("Quantity must be greater than 0.");
                continue;
            }
            break;
        }
        
        Product product = new Product(name, description, price, stock);
        
        if (productDAO.addProduct(product)) {
            ConsoleUtil.printSuccess("Product added successfully!");
        } else {
            ConsoleUtil.printError("Failed to add product!");
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    private void updateProduct() {
        viewProducts();
        
        int productId = ConsoleUtil.readInt("\nEnter Product ID to update: ");
        Product product = productDAO.getProductById(productId);
        
        if (product == null) {
            ConsoleUtil.printError("Product not found!");
            ConsoleUtil.pressEnterToContinue();
            return;
        }
        
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("UPDATE PRODUCT");
        System.out.println("Current Details:");
        System.out.println("Name: " + product.getName());
        System.out.println("Description: " + product.getDescription());
        System.out.println("Price: " + ConsoleUtil.formatCurrency(product.getPrice()));
        System.out.println("Stock: " + product.getStockQuantity());
        System.out.println("\nEnter new details (press Enter to keep current value):");
        
        String newName = ConsoleUtil.readString("New Name [" + product.getName() + "]: ");
        if (!newName.isEmpty()) product.setName(newName);
        
        String newDesc = ConsoleUtil.readString("New Description [" + product.getDescription() + "]: ");
        if (!newDesc.isEmpty()) product.setDescription(newDesc);
        
        String priceStr = ConsoleUtil.readString("New Price [" + ConsoleUtil.formatCurrency(product.getPrice()) + "]: ");
        if (!priceStr.isEmpty()) {
            try {
                double newPrice = Double.parseDouble(priceStr);
                if (InputValidator.isValidPrice(newPrice)) {
                    product.setPrice(newPrice);
                }
            } catch (NumberFormatException e) {
                ConsoleUtil.printError("Invalid price format!");
            }
        }
        
        String stockStr = ConsoleUtil.readString("New Stock [" + product.getStockQuantity() + "]: ");
        if (!stockStr.isEmpty()) {
            try {
                int newStock = Integer.parseInt(stockStr);
                if (newStock >= 0) {
                    product.setStockQuantity(newStock);
                }
            } catch (NumberFormatException e) {
                ConsoleUtil.printError("Invalid stock format!");
            }
        }
        
        if (productDAO.updateProduct(product)) {
            ConsoleUtil.printSuccess("Product updated successfully!");
        } else {
            ConsoleUtil.printError("Failed to update product!");
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    private void deleteProduct() {
        viewProducts();
        
        int productId = ConsoleUtil.readInt("\nEnter Product ID to delete: ");
        
        String confirm = ConsoleUtil.readString("Are you sure you want to delete this product? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            if (productDAO.deleteProduct(productId)) {
                ConsoleUtil.printSuccess("Product deleted successfully!");
            } else {
                ConsoleUtil.printError("Failed to delete product!");
            }
        } else {
            ConsoleUtil.printInfo("Deletion cancelled.");
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    private void updateStock() {
        viewProducts();
        
        int productId = ConsoleUtil.readInt("\nEnter Product ID: ");
        Product product = productDAO.getProductById(productId);
        
        if (product == null) {
            ConsoleUtil.printError("Product not found!");
            ConsoleUtil.pressEnterToContinue();
            return;
        }
        
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("UPDATE STOCK");
        System.out.println("Product: " + product.getName());
        System.out.println("Current Stock: " + product.getStockQuantity());
        
        int change = ConsoleUtil.readInt("\nEnter quantity change (+ for add, - for remove): ");
        
        if (productDAO.updateStock(productId, change)) {
            ConsoleUtil.printSuccess("Stock updated successfully!");
            int newStock = productDAO.getStockQuantity(productId);
            System.out.println("New Stock: " + newStock);
        } else {
            ConsoleUtil.printError("Failed to update stock! Check if you have enough stock to remove.");
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }
    
    public boolean updateProductStock(int productId, int quantityChange) {
        return productDAO.updateStock(productId, quantityChange);
    }
}