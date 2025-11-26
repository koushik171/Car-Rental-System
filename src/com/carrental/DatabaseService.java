package com.carrental;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * DatabaseService handles SQL database operations for rental history
 */
public class DatabaseService {
    private static final String DB_URL = "jdbc:sqlite:car_rental.db";
    
    // Initialize database and create tables
    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            createTables(conn);
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }
    
    // Create necessary tables
    private static void createTables(Connection conn) throws SQLException {
        String createRentalsTable = """
            CREATE TABLE IF NOT EXISTS rentals (
                rental_id INTEGER PRIMARY KEY,
                customer_id INTEGER NOT NULL,
                car_id INTEGER NOT NULL,
                customer_name TEXT NOT NULL,
                car_brand TEXT NOT NULL,
                car_model TEXT NOT NULL,
                rental_date DATE NOT NULL,
                return_date DATE,
                days INTEGER NOT NULL,
                total_cost REAL NOT NULL,
                is_active BOOLEAN NOT NULL DEFAULT 1
            )
        """;
        
        String createCustomersTable = """
            CREATE TABLE IF NOT EXISTS customers (
                customer_id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                phone TEXT NOT NULL,
                email TEXT NOT NULL
            )
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createRentalsTable);
            stmt.execute(createCustomersTable);
        }
    }
    
    // Save rental to database
    public static void saveRental(Rental rental, Customer customer, Car car) {
        String sql = """
            INSERT INTO rentals (rental_id, customer_id, car_id, customer_name, 
                               car_brand, car_model, rental_date, return_date, 
                               days, total_cost, is_active) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
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
    
    // Update rental status when car is returned
    public static void updateRentalStatus(int carId, boolean isActive) {
        String sql = "UPDATE rentals SET is_active = ? WHERE car_id = ? AND is_active = 1";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, isActive);
            pstmt.setInt(2, carId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error updating rental status: " + e.getMessage());
        }
    }
    
    // Get all rental history from database
    public static ArrayList<String> getRentalHistory() {
        ArrayList<String> history = new ArrayList<>();
        String sql = """
            SELECT rental_id, customer_name, car_brand, car_model, 
                   rental_date, days, total_cost, is_active 
            FROM rentals ORDER BY rental_date DESC
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
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
            System.out.println("Error retrieving rental history: " + e.getMessage());
        }
        
        return history;
    }
    
    // Save customer to database
    public static void saveCustomer(Customer customer) {
        String sql = "INSERT OR REPLACE INTO customers (customer_id, name, phone, email) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customer.getCustomerId());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getEmail());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }
    
    // Get rental statistics
    public static void showRentalStatistics() {
        String sql = """
            SELECT 
                COUNT(*) as total_rentals,
                COUNT(CASE WHEN is_active = 1 THEN 1 END) as active_rentals,
                SUM(total_cost) as total_revenue,
                AVG(total_cost) as avg_rental_cost
            FROM rentals
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                System.out.println("\n=== RENTAL STATISTICS ===");
                System.out.println("Total Rentals: " + rs.getInt("total_rentals"));
                System.out.println("Active Rentals: " + rs.getInt("active_rentals"));
                System.out.println("Total Revenue: $" + String.format("%.2f", rs.getDouble("total_revenue")));
                System.out.println("Average Rental Cost: $" + String.format("%.2f", rs.getDouble("avg_rental_cost")));
                System.out.println("========================");
            }
            
        } catch (SQLException e) {
            System.out.println("Error retrieving statistics: " + e.getMessage());
        }
    }
}