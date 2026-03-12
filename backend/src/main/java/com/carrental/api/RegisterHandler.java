package com.carrental.api;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import com.google.gson.*;

public class RegisterHandler implements HttpHandler {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Koushik@004";
    private static Gson gson = new Gson();
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            CarRentalAPI.sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }
        
        try {
            String body = CarRentalAPI.getRequestBody(exchange);
            Map<String, String> data = gson.fromJson(body, Map.class);
            
            String name = data.get("name");
            String phone = data.get("phone");
            String email = data.get("email");
            String username = data.get("username");
            String password = data.get("password");
            
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                // Check if username exists
                String checkSql = "SELECT username FROM users WHERE username = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, username);
                if (checkStmt.executeQuery().next()) {
                    CarRentalAPI.sendResponse(exchange, 400, 
                        "{\"success\":false,\"error\":\"Username already exists\"}");
                    return;
                }
                
                // Get next customer ID
                String maxIdSql = "SELECT COALESCE(MAX(customer_id), 0) + 1 as next_id FROM customers";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(maxIdSql);
                rs.next();
                int customerId = rs.getInt("next_id");
                
                // Insert customer
                String customerSql = "INSERT INTO customers (customer_id, name, phone, email) VALUES (?, ?, ?, ?)";
                PreparedStatement customerStmt = conn.prepareStatement(customerSql);
                customerStmt.setInt(1, customerId);
                customerStmt.setString(2, name);
                customerStmt.setString(3, phone);
                customerStmt.setString(4, email);
                customerStmt.executeUpdate();
                
                // Insert user
                String userSql = "INSERT INTO users (user_id, username, password, role, customer_id) VALUES (?, ?, ?, 'CUSTOMER', ?)";
                PreparedStatement userStmt = conn.prepareStatement(userSql);
                userStmt.setString(1, "U" + String.format("%03d", customerId));
                userStmt.setString(2, username);
                userStmt.setString(3, password);
                userStmt.setString(4, String.valueOf(customerId));
                userStmt.executeUpdate();
                
                // Initialize loyalty points
                String loyaltySql = "INSERT INTO loyalty_points (customer_id, points, tier, referral_count) VALUES (?, 0, 'BRONZE', 0)";
                PreparedStatement loyaltyStmt = conn.prepareStatement(loyaltySql);
                loyaltyStmt.setInt(1, customerId);
                loyaltyStmt.executeUpdate();
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Registration successful");
                response.put("customerId", customerId);
                
                CarRentalAPI.sendResponse(exchange, 201, gson.toJson(response));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            CarRentalAPI.sendResponse(exchange, 500, 
                "{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
