package com.carrental;

import java.sql.*;
import java.util.ArrayList;

/**
 * MySQL Database Service for Car Rental System
 */
public class MySQLDatabaseService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; // Change this to your MySQL password
    
    // Initialize MySQL database
    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            createTables(conn);
        } catch (SQLException e) {
            System.out.println("MySQL connection error: " + e.getMessage());
            System.out.println("Make sure MySQL is running and database 'car_rental' exists");
        }
    }
    
    // Create tables for MySQL
    private static void createTables(Connection conn) throws SQLException {
        String createRentalsTable = """
            CREATE TABLE IF NOT EXISTS rentals (
                rental_id INT PRIMARY KEY,
                customer_id INT NOT NULL,
                car_id INT NOT NULL,
                customer_name VARCHAR(100) NOT NULL,
                car_brand VARCHAR(50) NOT NULL,
                car_model VARCHAR(50) NOT NULL,
                rental_date DATE NOT NULL,
                return_date DATE,
                days INT NOT NULL,
                total_cost DECIMAL(10,2) NOT NULL,
                is_active BOOLEAN NOT NULL DEFAULT TRUE
            )
        """;
        
        String createCustomersTable = """
            CREATE TABLE IF NOT EXISTS customers (
                customer_id INT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                phone VARCHAR(20) NOT NULL,
                email VARCHAR(100) NOT NULL
            )
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createRentalsTable);
            stmt.execute(createCustomersTable);
        }
    }
    
    // Save rental to MySQL
    public static void saveRental(Rental rental, Customer customer, Car car) {
        String sql = """
            INSERT INTO rentals (rental_id, customer_id, car_id, customer_name, 
                               car_brand, car_model, rental_date, return_date, 
                               days, total_cost, is_active) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rental.getRentalId());
            pstmt.setInt(2, rental.getCustomerId());
            pstmt.setInt(3, rental.getCarId());
            pstmt.setString(4, customer.getName());
            pstmt.setString(5, car.getBrand());
            pstmt.setString(6, car.getModel());
            pstmt.setDate(7, Date.valueOf(rental.getRentalDate()));
            pstmt.setDate(8, Date.valueOf(rental.getReturnDate()));
            pstmt.setInt(9, rental.getDays());
            pstmt.setDouble(10, rental.getTotalCost());
            pstmt.setBoolean(11, rental.isActive());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving rental: " + e.getMessage());
        }
    }
    
    // Update rental status
    public static void updateRentalStatus(int carId, boolean isActive) {
        String sql = "UPDATE rentals SET is_active = ? WHERE car_id = ? AND is_active = TRUE";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, isActive);
            pstmt.setInt(2, carId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error updating rental: " + e.getMessage());
        }
    }
    
    // Get rental history
    public static ArrayList<String> getRentalHistory() {
        ArrayList<String> history = new ArrayList<>();
        String sql = """
            SELECT rental_id, customer_name, car_brand, car_model, 
                   rental_date, days, total_cost, is_active 
            FROM rentals ORDER BY rental_date DESC
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String status = rs.getBoolean("is_active") ? "Active" : "Completed";
                String record = String.format(
                    "ID: %d | Customer: %s | Car: %s %s | Date: %s | Days: %d | Cost: $%.2f | Status: %s",
                    rs.getInt("rental_id"),
                    rs.getString("customer_name"),
                    rs.getString("car_brand"),
                    rs.getString("car_model"),
                    rs.getDate("rental_date"),
                    rs.getInt("days"),
                    rs.getDouble("total_cost"),
                    status
                );
                history.add(record);
            }
            
        } catch (SQLException e) {
            System.out.println("Error retrieving history: " + e.getMessage());
        }
        
        return history;
    }
    
    // Save customer
    public static void saveCustomer(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, name, phone, email) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE name=?, phone=?, email=?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customer.getCustomerId());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getName());
            pstmt.setString(6, customer.getPhone());
            pstmt.setString(7, customer.getEmail());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }
    
    // Show statistics
    public static void showRentalStatistics() {
        String sql = """
            SELECT 
                COUNT(*) as total_rentals,
                COUNT(CASE WHEN is_active = TRUE THEN 1 END) as active_rentals,
                SUM(total_cost) as total_revenue,
                AVG(total_cost) as avg_rental_cost
            FROM rentals
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                System.out.println("\n=== RENTAL STATISTICS (MySQL) ===");
                System.out.println("Total Rentals: " + rs.getInt("total_rentals"));
                System.out.println("Active Rentals: " + rs.getInt("active_rentals"));
                System.out.println("Total Revenue: $" + String.format("%.2f", rs.getDouble("total_revenue")));
                System.out.println("Average Rental Cost: $" + String.format("%.2f", rs.getDouble("avg_rental_cost")));
                System.out.println("================================");
            }
            
        } catch (SQLException e) {
            System.out.println("Error retrieving statistics: " + e.getMessage());
        }
    }
    
    // Test connection method
    public static void main(String[] args) {
        System.out.println("Testing MySQL connection...");
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            System.out.println("✅ MySQL connection successful!");
            System.out.println("Database: " + conn.getCatalog());
        } catch (SQLException e) {
            System.out.println("❌ MySQL connection failed: " + e.getMessage());
            if (e.getMessage().contains("Unknown database")) {
                System.out.println("Please create database: CREATE DATABASE car_rental;");
            }
        }
    }
}