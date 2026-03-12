package com.carrental.api;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import com.google.gson.*;

public class LoginHandler implements HttpHandler {
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
            Map<String, String> credentials = gson.fromJson(body, Map.class);
            
            String username = credentials.get("username");
            String password = credentials.get("password");
            
            // Authenticate user
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String sql = "SELECT u.*, c.name, c.email FROM users u " +
                           "LEFT JOIN customers c ON u.customer_id = c.customer_id " +
                           "WHERE u.username = ? AND u.password = ?";
                
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("userId", rs.getString("user_id"));
                    user.put("username", rs.getString("username"));
                    user.put("role", rs.getString("role"));
                    user.put("customerId", rs.getString("customer_id"));
                    user.put("name", rs.getString("name"));
                    user.put("email", rs.getString("email"));
                    user.put("token", generateToken(username));
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("user", user);
                    
                    CarRentalAPI.sendResponse(exchange, 200, gson.toJson(response));
                } else {
                    CarRentalAPI.sendResponse(exchange, 401, 
                        "{\"success\":false,\"error\":\"Invalid credentials\"}");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            CarRentalAPI.sendResponse(exchange, 500, 
                "{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    private String generateToken(String username) {
        return Base64.getEncoder().encodeToString(
            (username + ":" + System.currentTimeMillis()).getBytes()
        );
    }
}
