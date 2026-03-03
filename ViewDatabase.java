import java.sql.*;

public class ViewDatabase {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/car_rental";
        String user = "root";
        String password = "Koushik@004";
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("=== CAR RENTAL DATABASE ===\n");
            
            // Show Customers
            System.out.println("--- CUSTOMERS ---");
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery("SELECT * FROM customers");
            while (rs1.next()) {
                System.out.printf("ID: %s | Name: %s | Phone: %s | Email: %s\n",
                    rs1.getString("customer_id"), rs1.getString("name"),
                    rs1.getString("phone"), rs1.getString("email"));
            }
            
            // Show Rentals
            System.out.println("\n--- RENTALS ---");
            Statement stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT * FROM rentals");
            while (rs2.next()) {
                System.out.printf("ID: %s | Customer: %s | Car: %s %s | Days: %d | Cost: $%.2f | Active: %s\n",
                    rs2.getString("rental_id"), rs2.getString("customer_name"),
                    rs2.getString("car_brand"), rs2.getString("car_model"),
                    rs2.getInt("days"), rs2.getDouble("total_cost"),
                    rs2.getBoolean("is_active") ? "Yes" : "No");
            }
            
            // Show counts
            System.out.println("\n--- STATISTICS ---");
            ResultSet rs3 = stmt1.executeQuery("SELECT COUNT(*) as count FROM customers");
            if (rs3.next()) System.out.println("Total Customers: " + rs3.getInt("count"));
            
            ResultSet rs4 = stmt2.executeQuery("SELECT COUNT(*) as count FROM rentals");
            if (rs4.next()) System.out.println("Total Rentals: " + rs4.getInt("count"));
            
            ResultSet rs5 = stmt2.executeQuery("SELECT COUNT(*) as count FROM rentals WHERE is_active = true");
            if (rs5.next()) System.out.println("Active Rentals: " + rs5.getInt("count"));
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
