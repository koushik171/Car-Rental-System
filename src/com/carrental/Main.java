package com.carrental;

import java.util.Scanner;

/**
 * Main class - Entry point for Car Rental System
 */
public class Main {
    private static CarService carService = new CarService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("   WELCOME TO CAR RENTAL SYSTEM");
        System.out.println("=================================");

        // Main menu loop
        while (true) {
            showMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    addCarMenu();
                    break;
                case 2:
                    carService.viewCars();
                    break;
                case 3:
                    addCustomerMenu();
                    break;
                case 4:
                    carService.viewCustomers();
                    break;
                case 5:
                    rentCarMenu();
                    break;
                case 6:
                    returnCarMenu();
                    break;
                case 7:
                    searchMenu();
                    break;
                case 8:
                    carService.viewRentalHistory();
                    break;
                case 9:
                    carService.showStatistics();
                    break;
                case 10:
                    System.out.println("Thank you for using Car Rental System!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            
            // Pause before showing menu again
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    // Display main menu
    private static void showMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Add Car");
        System.out.println("2. View All Cars");
        System.out.println("3. Add Customer");
        System.out.println("4. View Customers");
        System.out.println("5. Rent a Car");
        System.out.println("6. Return a Car");
        System.out.println("7. Search Cars");
        System.out.println("8. View Rental History");
        System.out.println("9. View Statistics");
        System.out.println("10. Exit");
        System.out.print("Enter your choice (1-10): ");
    }

    // Get user choice with input validation
    private static int getChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return choice;
        } catch (NumberFormatException e) {
            return -1; // Invalid choice
        }
    }

    // Add car menu
    private static void addCarMenu() {
        System.out.println("\n=== ADD NEW CAR ===");
        
        try {
            System.out.print("Enter Car ID: ");
            int carId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Brand: ");
            String brand = scanner.nextLine().trim();
            
            System.out.print("Enter Model: ");
            String model = scanner.nextLine().trim();
            
            System.out.print("Enter Year: ");
            int year = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Color: ");
            String color = scanner.nextLine().trim();
            
            System.out.print("Enter Fuel Type: ");
            String fuelType = scanner.nextLine().trim();
            
            System.out.print("Enter Price per Day: $");
            double pricePerDay = Double.parseDouble(scanner.nextLine());
            
            // Validate inputs
            if (brand.isEmpty() || model.isEmpty() || color.isEmpty() || fuelType.isEmpty()) {
                System.out.println("All fields must be filled!");
                return;
            }
            
            if (pricePerDay <= 0 || year < 1900) {
                System.out.println("Invalid price or year!");
                return;
            }
            
            carService.addCar(carId, brand, model, year, color, fuelType, pricePerDay);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }

    // Rent car menu
    private static void rentCarMenu() {
        System.out.println("\n=== RENT A CAR ===");
        
        // Show available cars first
        carService.viewAvailableCars();
        
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Car ID to rent: ");
            int carId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter number of rental days: ");
            int days = Integer.parseInt(scanner.nextLine());
            
            // Validate days
            if (days <= 0) {
                System.out.println("Number of days must be positive!");
                return;
            }
            
            carService.rentCar(carId, customerId, days);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }

    // Return car menu
    private static void returnCarMenu() {
        System.out.println("\n=== RETURN A CAR ===");
        
        try {
            System.out.print("Enter Car ID to return: ");
            int carId = Integer.parseInt(scanner.nextLine());
            
            carService.returnCar(carId);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid Car ID.");
        }
    }

    // Add customer menu
    private static void addCustomerMenu() {
        System.out.println("\n=== ADD CUSTOMER ===");
        
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            System.out.println("All fields are required!");
            return;
        }
        
        carService.addCustomer(name, phone, email);
    }

    // Search menu
    private static void searchMenu() {
        System.out.println("\n=== SEARCH CARS ===");
        System.out.println("1. Search by Brand");
        System.out.println("2. Search by Price Range");
        System.out.print("Enter choice (1-2): ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            
            switch (choice) {
                case 1:
                    System.out.print("Enter brand name: ");
                    String brand = scanner.nextLine().trim();
                    carService.searchCarsByBrand(brand);
                    break;
                case 2:
                    System.out.print("Enter minimum price: $");
                    double minPrice = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter maximum price: $");
                    double maxPrice = Double.parseDouble(scanner.nextLine());
                    carService.searchCarsByPriceRange(minPrice, maxPrice);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }
}