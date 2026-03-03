package com.carrental;

public class User {
    private String userId;
    private String username;
    private String password;
    private String role; // "CUSTOMER" or "MANAGER"
    private String customerId; // Link to customer if role is CUSTOMER
    
    public User(String userId, String username, String password, String role, String customerId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.customerId = customerId;
    }
    
    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getCustomerId() { return customerId; }
    
    public boolean isManager() {
        return "MANAGER".equals(role);
    }
    
    public boolean isCustomer() {
        return "CUSTOMER".equals(role);
    }
}
