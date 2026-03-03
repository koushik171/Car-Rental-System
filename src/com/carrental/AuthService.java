package com.carrental;

import java.sql.*;
import java.util.ArrayList;

public class AuthService {
    private ArrayList<User> users = new ArrayList<>();
    
    public AuthService() {
        initializeDefaultUsers();
        loadUsersFromDB();
    }
    
    // Create default manager account
    private void initializeDefaultUsers() {
        users.add(new User("U001", "admin", "admin123", "MANAGER", null));
    }
    
    // Load users from database
    private void loadUsersFromDB() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/car_rental", "root", "Koushik@004")) {
            
            createUsersTable(conn);
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            
            // Don't clear default users, just add from DB
            while (rs.next()) {
                users.add(new User(
                    rs.getString("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("customer_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Note: Using default users only");
        }
    }
    
    // Create users table if not exists
    private void createUsersTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id VARCHAR(20) PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "role ENUM('CUSTOMER', 'MANAGER') NOT NULL, " +
                    "customer_id VARCHAR(20))";
        conn.createStatement().execute(sql);
        
        // Create loyalty_points table
        String loyaltySql = "CREATE TABLE IF NOT EXISTS loyalty_points (" +
                           "customer_id INT PRIMARY KEY, " +
                           "points INT DEFAULT 0, " +
                           "tier VARCHAR(20) DEFAULT 'BRONZE', " +
                           "referral_count INT DEFAULT 0)";
        conn.createStatement().execute(loyaltySql);
        
        // Create promo_codes table
        String promoSql = "CREATE TABLE IF NOT EXISTS promo_codes (" +
                         "code VARCHAR(50) PRIMARY KEY, " +
                         "discount_percent DOUBLE, " +
                         "expiry_date DATE, " +
                         "usage_limit INT, " +
                         "times_used INT DEFAULT 0, " +
                         "is_active BOOLEAN DEFAULT TRUE)";
        conn.createStatement().execute(promoSql);
    }
    
    // Login authentication
    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    
    // Register new customer user
    public boolean registerCustomer(String username, String password, String customerId) {
        String userId = "U" + String.format("%03d", users.size() + 1);
        User newUser = new User(userId, username, password, "CUSTOMER", customerId);
        users.add(newUser);
        
        // Save to database
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/car_rental", "root", "Koushik@004")) {
            
            String sql = "INSERT INTO users VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, "CUSTOMER");
            pstmt.setString(5, customerId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }
}
