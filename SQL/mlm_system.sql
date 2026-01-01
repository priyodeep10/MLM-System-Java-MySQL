-- =========================================
-- DATABASE
-- =========================================
CREATE DATABASE IF NOT EXISTS mlm_system;
USE mlm_system;

-- =========================================
-- ADMIN TABLE
-- =========================================
CREATE TABLE admin (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    total_profit DECIMAL(12,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================================
-- USERS TABLE (Hierarchy based MLM)
-- =========================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    level TINYINT NOT NULL,   -- Level 1,2,3 (validated in Java)
    parent_id INT NULL,
    total_profit DECIMAL(12,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_parent FOREIGN KEY (parent_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

-- =========================================
-- PRODUCTS TABLE
-- =========================================
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================================
-- SALES TABLE
-- =========================================
CREATE TABLE sales (
    sale_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    seller_id INT NOT NULL,
    seller_type ENUM('admin','user') NOT NULL,
    quantity INT NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sales_product FOREIGN KEY (product_id)
        REFERENCES products(product_id)
        ON DELETE RESTRICT
);

-- =========================================
-- PROFIT DISTRIBUTION TABLE
-- =========================================
CREATE TABLE profit_distribution (
    distribution_id INT PRIMARY KEY AUTO_INCREMENT,
    sale_id INT NOT NULL,
    receiver_id INT NOT NULL,
    receiver_type ENUM('admin','user') NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    percentage DECIMAL(6,2) NOT NULL,
    level TINYINT,
    distribution_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_profit_sale FOREIGN KEY (sale_id)
        REFERENCES sales(sale_id)
        ON DELETE CASCADE
);

-- =========================================
-- DEFAULT ADMIN
-- =========================================
INSERT INTO admin (username, password)
VALUES ('admin', '123');

-- =========================================
-- SAMPLE PRODUCTS (20 ITEMS)
-- =========================================
INSERT INTO products (name, description, price, stock_quantity) VALUES
('Smartphone', 'Android smartphone 128GB', 699.99, 50),
('Tablet', '10 inch tablet', 499.99, 30),
('Smart Watch', 'Fitness watch', 199.99, 80),
('Headphone', 'Wireless headphones', 149.99, 60),
('Speaker', 'Bluetooth speaker', 89.99, 100),
('Power Bank', '20000mAh power bank', 49.99, 150),
('Laptop Stand', 'Adjustable stand', 39.99, 120),
('Wireless Mouse', 'Ergonomic mouse', 29.99, 200),
('Keyboard', 'Mechanical keyboard', 79.99, 90),
('Monitor', '24 inch monitor', 199.99, 40),
('Webcam', 'HD webcam', 59.99, 70),
('SSD 1TB', 'External SSD', 129.99, 50),
('Mouse Pad', 'Gaming mouse pad', 34.99, 150),
('Desk Lamp', 'LED lamp', 24.99, 200),
('Phone Case', 'Protective case', 19.99, 300),
('Screen Guard', 'Tempered glass', 9.99, 400),
('USB Cable', 'Fast charging cable', 14.99, 250),
('Router', 'WiFi router', 59.99, 45),
('Smart Bulb', 'WiFi bulb', 29.99, 90),
('Portable Fan', 'Rechargeable fan', 19.99, 110);

-- =========================================
-- INDEXES
-- =========================================
CREATE INDEX idx_users_parent ON users(parent_id);
CREATE INDEX idx_users_level ON users(level);
CREATE INDEX idx_sales_seller ON sales(seller_id, seller_type);
CREATE INDEX idx_profit_sale ON profit_distribution(sale_id);

-- =========================================
-- STORED PROCEDURE: PROFIT DISTRIBUTION (4 PARAMETERS)
-- =========================================
DELIMITER ₹₹

DROP PROCEDURE IF EXISTS DistributeProfit₹₹

CREATE PROCEDURE DistributeProfit(
    IN p_sale_id INT,
    IN p_seller_id INT,
    IN p_seller_type VARCHAR(10),
    IN p_total_amount DECIMAL(10,2)
)
BEGIN
    DECLARE v_seller_level INT;
    DECLARE v_parent_id INT;
    DECLARE v_grandparent_id INT;
    
    IF p_seller_type = 'admin' THEN
        -- Admin gets 100%
        INSERT INTO profit_distribution (sale_id, receiver_id, receiver_type, amount, percentage, level)
        VALUES (p_sale_id, p_seller_id, 'admin', p_total_amount, 100.00, 0);
        
        -- Update admin profit
        UPDATE admin SET total_profit = total_profit + p_total_amount WHERE admin_id = 1;
        
    ELSEIF p_seller_type = 'user' THEN
        -- Get seller level and hierarchy
        SELECT level, parent_id INTO v_seller_level, v_parent_id 
        FROM users WHERE user_id = p_seller_id;
        
        IF v_seller_level = 1 THEN
            -- Level 1 sale: Admin 10%, Level1 90%
            -- Admin's share
            INSERT INTO profit_distribution (sale_id, receiver_id, receiver_type, amount, percentage, level)
            VALUES (p_sale_id, 1, 'admin', p_total_amount * 0.10, 10.00, 0);
            
            -- Level 1's share
            INSERT INTO profit_distribution (sale_id, receiver_id, receiver_type, amount, percentage, level)
            VALUES (p_sale_id, p_seller_id, 'user', p_total_amount * 0.90, 90.00, 1);
            
            -- Update profits
            UPDATE admin SET total_profit = total_profit + (p_total_amount * 0.10) WHERE admin_id = 1;
            UPDATE users SET total_profit = total_profit + (p_total_amount * 0.90) WHERE user_id = p_seller_id;
            
        ELSEIF v_seller_level = 2 THEN
            -- Level 2 sale: Admin 5%, Level1 5%, Level2 90%
            -- Admin's share
            INSERT INTO profit_distribution (sale_id, receiver_id, receiver_type, amount, percentage, level)
            VALUES (p_sale_id, 1, 'admin', p_total_amount * 0.05, 5.00, 0);
            
            -- Level 1's share (parent)
            INSERT INTO profit_distribution (sale_id, receiver_id, receiver_type, amount, percentage, level)
            VALUES (p_sale_id, v_parent_id, 'user', p_total_amount * 0.05, 5.00, 1);
            
            -- Level 2's share
            INSERT INTO profit_distribution (sale_id, receiver_id, receiver_type, amount, percentage, level)
            VALUES (p_sale_id, p_seller_id, 'user', p_total_amount * 0.90, 90.00, 2);
            
            -- Update profits
            UPDATE admin SET total_profit = total_profit + (p_total_amount * 0.05) WHERE admin_id = 1;
            UPDATE users SET total_profit = total_profit + (p_total_amount * 0.05) WHERE user_id = v_parent_id;
            UPDATE users SET total_profit = total_profit + (p_total_amount * 0.90) WHERE user_id = p_seller_id;
            
        ELSEIF v_seller_level = 3 THEN
            -- Level 3 sale: Admin 3.33%, Level1 3.33%, Level2 3.33%, Level3 90%
            -- Get Level 1 (grandparent)
            SELECT parent_id INTO v_grandparent_id FROM users WHERE user_id = v_parent_id;
            
            -- Admin's share
            INSERT INTO profit_distribution (sale_id, receiver_id, receiver_type, amount, percentage, level)
            VALUES (p_sale_id, 1, 'admin', p_total_amount * 0.0333, 3.33, 0);
            
            -- Level 1's share (grandparent)
            INSERT INTO profit_distribution (sale_id, receiver_id, receiver_type, amount, percentage, level)
            VALUES (p_sale_id, v_grandparent_id, 'user', p_total_amount * 0.0333, 3.33, 1);
            
            -- Level 2's share (parent)
            INSERT INTO profit_distribution (sale_id, receiver_id, receiver_type, amount, percentage, level)
            VALUES (p_sale_id, v_parent_id, 'user', p_total_amount * 0.0333, 3.33, 2);
            
            -- Level 3's share
            INSERT INTO profit_distribution (sale_id, receiver_id, receiver_type, amount, percentage, level)
            VALUES (p_sale_id, p_seller_id, 'user', p_total_amount * 0.90, 90.00, 3);
            
            -- Update profits (rounding to 2 decimal places)
            UPDATE admin SET total_profit = total_profit + ROUND(p_total_amount * 0.0333, 2) WHERE admin_id = 1;
            UPDATE users SET total_profit = total_profit + ROUND(p_total_amount * 0.0333, 2) WHERE user_id = v_grandparent_id;
            UPDATE users SET total_profit = total_profit + ROUND(p_total_amount * 0.0333, 2) WHERE user_id = v_parent_id;
            UPDATE users SET total_profit = total_profit + ROUND(p_total_amount * 0.90, 2) WHERE user_id = p_seller_id;
        END IF;
    END IF;
END₹₹

DELIMITER ;