import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/car_rental";
        String user = "root";
        String password = "Koushik@004";
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✓ MySQL Connection Successful!");
            System.out.println("✓ Database: car_rental");
            
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("✓ MySQL Version: " + meta.getDatabaseProductVersion());
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            System.out.println("\nTables in database:");
            while (rs.next()) {
                System.out.println("  - " + rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("✗ Connection Failed: " + e.getMessage());
        }
    }
}
