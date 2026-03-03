package com.carrental;

import java.util.Scanner;

/**
 * Main class - Entry point for Car Rental System
 */
public class Main {
    private static CarService carService = new CarService();
    private static AuthService authService = new AuthService();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("   WELCOME TO CAR RENTAL SYSTEM");
        System.out.println("=================================");

        // Login first
        if (!loginMenu()) {
            System.out.println("Login failed. Exiting...");
            return;
        }

        // Main menu loop
        while (true) {
            showMenu();
            int choice = getChoice();
            
            if (currentUser.isManager()) {
                handleManagerMenu(choice);
            } else {
                handleCustomerMenu(choice);
            }
            
            // Pause before showing menu again
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    // Handle manager menu choices
    private static void handleManagerMenu(int choice) {
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
                rentCarMenuAdvanced();
                break;
            case 6:
                returnCarMenuAdvanced();
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
                carService.viewPromoCodes();
                break;
            case 11:
                maintenanceMenu();
                break;
            case 12:
                viewAllReviewsMenu();
                break;
            case 13:
                System.out.println("Thank you for using Car Rental System!");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }

    // Handle customer menu choices
    private static void handleCustomerMenu(int choice) {
        switch (choice) {
            case 1:
                carService.viewAvailableCars();
                break;
            case 2:
                searchMenu();
                break;
            case 3:
                rentCarMenuCustomerAdvanced();
                break;
            case 4:
                carService.viewCustomerRentals(Integer.parseInt(currentUser.getCustomerId()));
                break;
            case 5:
                carService.viewPromoCodes();
                break;
            case 6:
                viewLoyaltyPoints();
                break;
            case 7:
                addReviewMenu();
                break;
            case 8:
                System.out.println("Thank you for using Car Rental System!");
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }

    // Rent car menu for customers (auto-fill customer ID)
    private static void rentCarMenuCustomer() {
        System.out.println("\n=== RENT A CAR ===");
        carService.viewAvailableCars();
        
        try {
            System.out.print("Enter Car ID to rent: ");
            int carId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter number of rental days: ");
            int days = Integer.parseInt(scanner.nextLine());
            
            if (days <= 0) {
                System.out.println("Number of days must be positive!");
                return;
            }
            
            int customerId = Integer.parseInt(currentUser.getCustomerId());
            carService.rentCar(carId, customerId, days);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }

    // Login menu
    private static boolean loginMenu() {
        System.out.println("\n=== LOGIN ===");
        System.out.println("1. Login");
        System.out.println("2. Register as Customer");
        System.out.print("Choose option (1-2): ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            
            if (choice == 1) {
                return performLogin();
            } else if (choice == 2) {
                return registerNewCustomer();
            } else {
                System.out.println("Invalid choice!");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            return false;
        }
    }
    
    // Perform login
    private static boolean performLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        currentUser = authService.login(username, password);
        
        if (currentUser != null) {
            System.out.println("\nWelcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")!");
            return true;
        } else {
            System.out.println("Invalid credentials!");
            return false;
        }
    }
    
    // Register new customer with login
    private static boolean registerNewCustomer() {
        System.out.println("\n=== CUSTOMER REGISTRATION ===");
        
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine().trim();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Choose username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Choose password: ");
        String password = scanner.nextLine().trim();
        
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            System.out.println("All fields are required!");
            return false;
        }
        
        // Add customer first
        int customerId = carService.addCustomerWithId(name, phone, email);
        
        // Register user account
        if (authService.registerCustomer(username, password, String.valueOf(customerId))) {
            System.out.println("\nRegistration successful! Logging you in...");
            currentUser = authService.login(username, password);
            return currentUser != null;
        } else {
            System.out.println("Registration failed!");
            return false;
        }
    }

    // Display main menu based on role
    private static void showMenu() {
        System.out.println("\n=== MAIN MENU ===");
        
        if (currentUser.isManager()) {
            // Manager menu - Full access
            System.out.println("1. Add Car");
            System.out.println("2. View All Cars");
            System.out.println("3. Add Customer");
            System.out.println("4. View Customers");
            System.out.println("5. Rent a Car");
            System.out.println("6. Return a Car");
            System.out.println("7. Search Cars");
            System.out.println("8. View Rental History");
            System.out.println("9. View Statistics");
            System.out.println("10. View Promo Codes");
            System.out.println("11. Maintenance Management");
            System.out.println("12. View All Reviews");
            System.out.println("13. Exit");
            System.out.print("Enter your choice (1-13): ");
        } else {
            // Customer menu - Limited access
            System.out.println("1. View Available Cars");
            System.out.println("2. Search Cars");
            System.out.println("3. Rent a Car");
            System.out.println("4. View My Rentals");
            System.out.println("5. View Promo Codes");
            System.out.println("6. My Loyalty Points");
            System.out.println("7. Add Review");
            System.out.println("8. Exit");
            System.out.print("Enter your choice (1-8): ");
        }
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
    
    // Advanced rent car menu with all features
    private static void rentCarMenuAdvanced() {
        System.out.println("\n=== RENT A CAR (ADVANCED) ===");
        carService.viewAvailableCars();
        
        try {
            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Car ID to rent: ");
            int carId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter number of rental days: ");
            int days = Integer.parseInt(scanner.nextLine());
            
            if (days <= 0) {
                System.out.println("Number of days must be positive!");
                return;
            }
            
            System.out.println("\nInsurance Options:");
            System.out.println("0. None");
            System.out.println("1. Basic ($10/day, $500 coverage)");
            System.out.println("2. Premium ($20/day, $2000 coverage)");
            System.out.println("3. Full ($35/day, full coverage)");
            System.out.print("Choose insurance (0-3): ");
            int insuranceChoice = Integer.parseInt(scanner.nextLine());
            
            Insurance.Type insuranceType = Insurance.Type.NONE;
            switch(insuranceChoice) {
                case 1: insuranceType = Insurance.Type.BASIC; break;
                case 2: insuranceType = Insurance.Type.PREMIUM; break;
                case 3: insuranceType = Insurance.Type.FULL; break;
            }
            
            System.out.print("Enter promo code (or press Enter to skip): ");
            String promoCode = scanner.nextLine().trim();
            
            System.out.print("Redeem loyalty points? (Enter points or 0): ");
            int pointsToRedeem = Integer.parseInt(scanner.nextLine());
            
            carService.rentCarWithFeatures(carId, customerId, days, insuranceType, promoCode, pointsToRedeem);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }
    
    // Advanced return car menu
    private static void returnCarMenuAdvanced() {
        System.out.println("\n=== RETURN A CAR ===");
        
        try {
            System.out.print("Enter Car ID to return: ");
            int carId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter actual mileage driven (km): ");
            int mileage = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Any damage? Enter damage cost (or 0): $");
            double damageAmount = Double.parseDouble(scanner.nextLine());
            
            carService.returnCarWithDetails(carId, mileage, damageAmount);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }
    
    // Customer advanced rental
    private static void rentCarMenuCustomerAdvanced() {
        System.out.println("\n=== RENT A CAR ===");
        carService.viewAvailableCars();
        
        try {
            System.out.print("Enter Car ID to rent: ");
            int carId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter number of rental days: ");
            int days = Integer.parseInt(scanner.nextLine());
            
            if (days <= 0) {
                System.out.println("Number of days must be positive!");
                return;
            }
            
            System.out.println("\nInsurance Options:");
            System.out.println("0. None");
            System.out.println("1. Basic ($10/day, $500 coverage)");
            System.out.println("2. Premium ($20/day, $2000 coverage)");
            System.out.println("3. Full ($35/day, full coverage)");
            System.out.print("Choose insurance (0-3): ");
            int insuranceChoice = Integer.parseInt(scanner.nextLine());
            
            Insurance.Type insuranceType = Insurance.Type.NONE;
            switch(insuranceChoice) {
                case 1: insuranceType = Insurance.Type.BASIC; break;
                case 2: insuranceType = Insurance.Type.PREMIUM; break;
                case 3: insuranceType = Insurance.Type.FULL; break;
            }
            
            System.out.print("Enter promo code (or press Enter to skip): ");
            String promoCode = scanner.nextLine().trim();
            
            System.out.print("Redeem loyalty points? (Enter points or 0): ");
            int pointsToRedeem = Integer.parseInt(scanner.nextLine());
            
            int customerId = Integer.parseInt(currentUser.getCustomerId());
            carService.rentCarWithFeatures(carId, customerId, days, insuranceType, promoCode, pointsToRedeem);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }
    
    // Maintenance menu
    private static void maintenanceMenu() {
        System.out.println("\n=== MAINTENANCE MANAGEMENT ===");
        System.out.println("1. View Cars Needing Maintenance");
        System.out.println("2. Perform Maintenance");
        System.out.print("Choose option (1-2): ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            
            if (choice == 1) {
                carService.viewMaintenanceNeeded();
            } else if (choice == 2) {
                System.out.print("Enter Car ID: ");
                int carId = Integer.parseInt(scanner.nextLine());
                carService.performMaintenance(carId);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }
    
    // View loyalty points
    private static void viewLoyaltyPoints() {
        try {
            int customerId = Integer.parseInt(currentUser.getCustomerId());
            Customer customer = carService.findCustomerById(customerId);
            
            if (customer != null) {
                LoyaltyPoints lp = customer.getLoyaltyPoints();
                System.out.println("\n=== YOUR LOYALTY STATUS ===");
                System.out.println("Tier: " + lp.getTier());
                System.out.println("Points: " + lp.getPoints());
                System.out.println("Tier Discount: " + (lp.getTierDiscount() * 100) + "%");
                System.out.println("Redeemable Value: $" + (lp.getPoints() / 10));
                System.out.println("Referrals: " + customer.getReferralCount());
                System.out.println("===========================");
            }
        } catch (Exception e) {
            System.out.println("Error viewing loyalty points.");
        }
    }
    
    // Add review menu
    private static void addReviewMenu() {
        System.out.println("\n=== ADD REVIEW ===");
        
        try {
            System.out.print("Enter Car ID to review: ");
            int carId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Rating (1-5 stars): ");
            int rating = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Your comment: ");
            String comment = scanner.nextLine();
            
            int customerId = Integer.parseInt(currentUser.getCustomerId());
            carService.addReview(carId, customerId, rating, comment);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }
    
    // View all reviews
    private static void viewAllReviewsMenu() {
        System.out.print("Enter Car ID to view reviews: ");
        try {
            int carId = Integer.parseInt(scanner.nextLine());
            carService.viewCarReviews(carId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }
}