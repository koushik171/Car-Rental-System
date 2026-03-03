package com.carrental;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

/**
 * CarService class handles all car rental operations
 */
public class CarService {
    private ArrayList<Car> carList;
    private ArrayList<Customer> customerList;
    private ArrayList<Rental> rentalHistory;
    private HashMap<String, ArrayList<Car>> brandIndex;
    private ArrayList<Review> reviews;
    private ArrayList<PromoCode> promoCodes;
    private int nextCustomerId = 1;
    private int nextRentalId = 1;
    private int nextReviewId = 1;

    // Constructor
    public CarService() {
        carList = new ArrayList<>();
        customerList = new ArrayList<>();
        rentalHistory = new ArrayList<>();
        brandIndex = new HashMap<>();
        reviews = new ArrayList<>();
        promoCodes = new ArrayList<>();
        MySQLDatabaseService.initializeDatabase();
        addSampleCars();
        initializePromoCodes();
        loadData();
    }

    // Add sample cars for testing
    private void addSampleCars() {
        addCarToSystem(new Car(1, "Toyota", "Camry", 2023, "White", "Petrol", 45.00));
        addCarToSystem(new Car(2, "Honda", "Civic", 2022, "Blue", "Petrol", 40.00));
        addCarToSystem(new Car(3, "BMW", "X3", 2024, "Black", "Diesel", 80.00));
        addCarToSystem(new Car(4, "Mercedes", "C-Class", 2023, "Silver", "Petrol", 90.00));
        addCarToSystem(new Car(5, "Ford", "Focus", 2021, "Red", "Petrol", 35.00));
    }
    
    // Initialize default promo codes
    private void initializePromoCodes() {
        // Try to load from database first
        ArrayList<PromoCode> dbPromoCodes = MySQLDatabaseService.loadPromoCodes();
        if (!dbPromoCodes.isEmpty()) {
            promoCodes = dbPromoCodes;
            return;
        }
        
        // If no promo codes in DB, create defaults and save them
        promoCodes.add(new PromoCode("WELCOME10", 10, java.time.LocalDate.now().plusMonths(3), 100));
        promoCodes.add(new PromoCode("SUMMER25", 25, java.time.LocalDate.now().plusMonths(2), 50));
        promoCodes.add(new PromoCode("FIRST20", 20, java.time.LocalDate.now().plusYears(1), 1000));
        
        // Save to database
        for (PromoCode promo : promoCodes) {
            MySQLDatabaseService.savePromoCode(promo);
        }
    }

    // Add a new car to the fleet
    public void addCar(int carId, String brand, String model, int year, String color, String fuelType, double pricePerDay) {
        for (Car car : carList) {
            if (car.getCarId() == carId) {
                System.out.println("Car with ID " + carId + " already exists!");
                return;
            }
        }
        
        Car newCar = new Car(carId, brand, model, year, color, fuelType, pricePerDay);
        addCarToSystem(newCar);
        System.out.println("Car added successfully: " + newCar);
        saveData();
    }

    // Helper method to add car and update index
    private void addCarToSystem(Car car) {
        carList.add(car);
        brandIndex.computeIfAbsent(car.getBrand().toLowerCase(), k -> new ArrayList<>()).add(car);
    }

    // View all cars in the fleet
    public void viewCars() {
        if (carList.isEmpty()) {
            System.out.println("No cars available in the fleet.");
            return;
        }

        System.out.println("\n=== CAR FLEET ===");
        for (Car car : carList) {
            System.out.println(car);
        }
        System.out.println("==================");
    }

    // Rent a car with customer info
    public void rentCar(int carId, int customerId, int days) {
        rentCarWithFeatures(carId, customerId, days, Insurance.Type.NONE, null, 0);
    }
    
    // Enhanced rental with all features
    public void rentCarWithFeatures(int carId, int customerId, int days, Insurance.Type insuranceType, 
                                    String promoCode, int pointsToRedeem) {
        Car carToRent = findCarById(carId);
        Customer customer = findCustomerById(customerId);
        
        if (carToRent == null) {
            System.out.println("Car with ID " + carId + " not found!");
            return;
        }
        if (customer == null) {
            System.out.println("Customer with ID " + customerId + " not found!");
            return;
        }
        if (!carToRent.isAvailable()) {
            System.out.println("Car is already rented!");
            return;
        }
        if (carToRent.getMaintenance().needsService()) {
            System.out.println("Car needs maintenance and cannot be rented!");
            return;
        }

        // Calculate dynamic price
        int availableCars = (int)carList.stream().filter(Car::isAvailable).count();
        double basePrice = carToRent.getPricePerDay();
        double dynamicPrice = DynamicPricing.calculatePrice(basePrice, java.time.LocalDate.now(), 
                                                           carList.size(), availableCars);
        
        // Apply early bird discount
        dynamicPrice = DynamicPricing.applyEarlyBirdDiscount(dynamicPrice, 
                                                             java.time.LocalDate.now(), 
                                                             java.time.LocalDate.now());
        
        double totalCost = days * dynamicPrice;
        
        // Apply loyalty tier discount
        double tierDiscount = customer.getLoyaltyPoints().getTierDiscount();
        totalCost *= (1 - tierDiscount);
        
        // Apply promo code
        if (promoCode != null && !promoCode.isEmpty()) {
            PromoCode promo = findPromoCode(promoCode);
            if (promo != null && promo.isValid()) {
                totalCost = promo.applyDiscount(totalCost);
                MySQLDatabaseService.savePromoCode(promo); // Save updated usage
                System.out.println("Promo code applied: " + promo.getDiscountPercent() + "% off!");
            }
        }
        
        // Redeem loyalty points
        if (pointsToRedeem > 0) {
            double pointsDiscount = customer.getLoyaltyPoints().redeemPoints(pointsToRedeem);
            totalCost -= pointsDiscount;
            System.out.println("Redeemed " + pointsToRedeem + " points for $" + pointsDiscount + " discount!");
        }
        
        // Add insurance cost
        Insurance insurance = new Insurance(nextRentalId, insuranceType);
        double insuranceCost = insurance.calculateCost(days);
        totalCost += insuranceCost;
        
        carToRent.setAvailable(false);
        
        Rental rental = new Rental(nextRentalId++, customerId, carId, days, totalCost);
        rental.setInsurance(insurance);
        rentalHistory.add(rental);
        
        // Award loyalty points
        customer.getLoyaltyPoints().addPoints(totalCost);
        MySQLDatabaseService.saveLoyaltyPoints(customerId, customer.getLoyaltyPoints());
        
        // Save to database
        MySQLDatabaseService.saveRental(rental, customer, carToRent);
        
        System.out.println("\n=== RENTAL CONFIRMATION ===");
        System.out.println("Customer: " + customer.getName() + " (" + customer.getLoyaltyPoints().getTier() + ")");
        System.out.println("Car: " + carToRent.getYear() + " " + carToRent.getBrand() + " " + carToRent.getModel());
        System.out.println("Rental Days: " + days);
        System.out.println("Base Price: $" + String.format("%.2f", basePrice * days));
        System.out.println("Dynamic Price: $" + String.format("%.2f", dynamicPrice * days));
        if (tierDiscount > 0) {
            System.out.println("Tier Discount: " + (tierDiscount * 100) + "%");
        }
        if (insuranceCost > 0) {
            System.out.println("Insurance (" + insuranceType + "): $" + String.format("%.2f", insuranceCost));
        }
        System.out.println("Total Cost: $" + String.format("%.2f", totalCost));
        System.out.println("Points Earned: +" + (int)(totalCost / 10));
        System.out.println("Rental ID: " + rental.getRentalId());
        System.out.println("===========================");
        saveData();
    }

    // Return a car by ID
    public void returnCar(int carId) {
        returnCarWithDetails(carId, 0, 0);
    }
    
    // Enhanced return with mileage and damage
    public void returnCarWithDetails(int carId, int actualMileage, double damageAmount) {
        Car carToReturn = findCarById(carId);
        
        if (carToReturn == null) {
            System.out.println("Car with ID " + carId + " not found!");
            return;
        }
        if (carToReturn.isAvailable()) {
            System.out.println("Car is not currently rented!");
            return;
        }

        // Find and close rental record
        Rental rental = null;
        for (Rental r : rentalHistory) {
            if (r.getCarId() == carId && r.isActive()) {
                rental = r;
                r.setActive(false);
                r.setActualReturnDate(java.time.LocalDate.now());
                break;
            }
        }
        
        if (rental == null) {
            System.out.println("No active rental found for this car!");
            return;
        }
        
        // Calculate late fee
        double lateFee = rental.calculateLateFee();
        
        // Update maintenance
        if (actualMileage > 0) {
            carToReturn.getMaintenance().addMileage(actualMileage);
        } else {
            carToReturn.getMaintenance().addMileage(rental.getEstimatedMileage());
        }
        
        // Calculate damage liability
        double customerLiability = 0;
        if (damageAmount > 0) {
            customerLiability = rental.getInsurance().calculateLiability(damageAmount);
        }
        
        // Update database
        MySQLDatabaseService.updateRentalStatus(carId, false);
        
        carToReturn.setAvailable(true);
        
        System.out.println("\n=== RETURN CONFIRMATION ===");
        System.out.println("Car returned: " + carToReturn.getBrand() + " " + carToReturn.getModel());
        System.out.println("Rental ID: " + rental.getRentalId());
        if (lateFee > 0) {
            System.out.println("⚠ Late Return Fee: $" + String.format("%.2f", lateFee));
        }
        if (customerLiability > 0) {
            System.out.println("⚠ Damage Liability: $" + String.format("%.2f", customerLiability));
        }
        System.out.println("Current Mileage: " + carToReturn.getMaintenance().getCurrentMileage() + " km");
        if (carToReturn.getMaintenance().needsService()) {
            System.out.println("⚠ Car needs maintenance!");
        } else {
            System.out.println("Next service in: " + carToReturn.getMaintenance().getMileageUntilService() + " km");
        }
        System.out.println("===========================");
        saveData();
    }

    // Helper method to find car by ID
    private Car findCarById(int carId) {
        for (Car car : carList) {
            if (car.getCarId() == carId) {
                return car;
            }
        }
        return null;
    }

    // Get available cars only
    public void viewAvailableCars() {
        System.out.println("\n=== AVAILABLE CARS ===");
        boolean hasAvailable = false;
        
        for (Car car : carList) {
            if (car.isAvailable()) {
                System.out.println(car);
                hasAvailable = true;
            }
        }
        
        if (!hasAvailable) {
            System.out.println("No cars available for rent.");
        }
        System.out.println("======================");
    }

    // Customer management
    public void addCustomer(String name, String phone, String email) {
        Customer customer = new Customer(nextCustomerId++, name, phone, email);
        customerList.add(customer);
        
        // Save to database
        MySQLDatabaseService.saveCustomer(customer);
        
        System.out.println("Customer added: " + customer);
        saveData();
    }
    
    // Add customer and return ID (for registration)
    public int addCustomerWithId(String name, String phone, String email) {
        Customer customer = new Customer(nextCustomerId++, name, phone, email);
        
        // Load loyalty points from database if exists
        LoyaltyPoints lp = MySQLDatabaseService.loadLoyaltyPoints(customer.getCustomerId());
        customer.setLoyaltyPoints(lp);
        
        customerList.add(customer);
        
        // Save to database
        MySQLDatabaseService.saveCustomer(customer);
        MySQLDatabaseService.saveLoyaltyPoints(customer.getCustomerId(), customer.getLoyaltyPoints());
        
        saveData();
        return customer.getCustomerId();
    }

    public void viewCustomers() {
        if (customerList.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }
        System.out.println("\n=== CUSTOMERS ===");
        for (Customer customer : customerList) {
            System.out.println(customer);
        }
        System.out.println("=================");
    }

    // Search functionality
    public void searchCarsByBrand(String brand) {
        ArrayList<Car> cars = brandIndex.get(brand.toLowerCase());
        if (cars == null || cars.isEmpty()) {
            System.out.println("No cars found for brand: " + brand);
            return;
        }
        System.out.println("\n=== CARS BY BRAND: " + brand.toUpperCase() + " ===");
        for (Car car : cars) {
            System.out.println(car);
        }
        System.out.println("================================");
    }

    public void searchCarsByPriceRange(double minPrice, double maxPrice) {
        System.out.println("\n=== CARS IN PRICE RANGE $" + minPrice + " - $" + maxPrice + " ===");
        boolean found = false;
        for (Car car : carList) {
            if (car.getPricePerDay() >= minPrice && car.getPricePerDay() <= maxPrice) {
                System.out.println(car);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No cars found in this price range.");
        }
        System.out.println("=======================================");
    }

    // Rental history from database
    public void viewRentalHistory() {
        ArrayList<String> history = MySQLDatabaseService.getRentalHistory();
        if (history.isEmpty()) {
            System.out.println("No rental history available.");
            return;
        }
        System.out.println("\n=== RENTAL HISTORY (FROM DATABASE) ===");
        for (String record : history) {
            System.out.println(record);
        }
        System.out.println("=====================================");
    }
    
    // View rentals for specific customer
    public void viewCustomerRentals(int customerId) {
        System.out.println("\n=== MY RENTALS ===");
        boolean found = false;
        
        for (Rental rental : rentalHistory) {
            if (rental.getCustomerId() == customerId) {
                Car car = findCarById(rental.getCarId());
                System.out.println("Rental ID: " + rental.getRentalId());
                System.out.println("Car: " + car.getBrand() + " " + car.getModel());
                System.out.println("Days: " + rental.getDays());
                System.out.println("Cost: $" + String.format("%.2f", rental.getTotalCost()));
                System.out.println("Status: " + (rental.isActive() ? "Active" : "Completed"));
                System.out.println("---");
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No rental history found.");
        }
        System.out.println("==================");
    }
    
    // Show rental statistics
    public void showStatistics() {
        MySQLDatabaseService.showRentalStatistics();
    }
    
    // Add review for a car
    public void addReview(int carId, int customerId, int rating, String comment) {
        Car car = findCarById(carId);
        Customer customer = findCustomerById(customerId);
        
        if (car == null || customer == null) {
            System.out.println("Invalid car or customer ID!");
            return;
        }
        
        Review review = new Review(nextReviewId++, carId, customerId, customer.getName(), rating, comment);
        reviews.add(review);
        car.addReview(rating);
        
        System.out.println("Review added successfully!");
    }
    
    // View reviews for a car
    public void viewCarReviews(int carId) {
        System.out.println("\n=== REVIEWS ===");
        boolean found = false;
        for (Review review : reviews) {
            if (review.getCarId() == carId) {
                System.out.println(review);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No reviews yet.");
        }
        System.out.println("===============");
    }
    
    // View all promo codes
    public void viewPromoCodes() {
        System.out.println("\n=== ACTIVE PROMO CODES ===");
        for (PromoCode promo : promoCodes) {
            if (promo.isValid()) {
                System.out.println(promo.getCode() + ": " + promo.getDiscountPercent() + "% off (Expires: " + 
                                 promo.getExpiryDate() + ")");
            }
        }
        System.out.println("==========================");
    }
    
    // Find promo code
    private PromoCode findPromoCode(String code) {
        for (PromoCode promo : promoCodes) {
            if (promo.getCode().equalsIgnoreCase(code)) {
                return promo;
            }
        }
        return null;
    }
    
    // View cars needing maintenance
    public void viewMaintenanceNeeded() {
        System.out.println("\n=== CARS NEEDING MAINTENANCE ===");
        boolean found = false;
        for (Car car : carList) {
            if (car.getMaintenance().needsService()) {
                System.out.println(car);
                System.out.println("  Mileage: " + car.getMaintenance().getCurrentMileage() + " km");
                System.out.println("  Last Service: " + car.getMaintenance().getLastServiceDate());
                found = true;
            }
        }
        if (!found) {
            System.out.println("All cars are in good condition!");
        }
        System.out.println("=================================");
    }
    
    // Perform maintenance on a car
    public void performMaintenance(int carId) {
        Car car = findCarById(carId);
        if (car == null) {
            System.out.println("Car not found!");
            return;
        }
        
        car.getMaintenance().performService();
        System.out.println("Maintenance completed for " + car.getBrand() + " " + car.getModel());
    }

    // Helper methods
    public Customer findCustomerById(int customerId) {
        for (Customer customer : customerList) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    // File operations
    private void saveData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("rental_data.txt"))) {
            writer.println("=== CARS ===");
            for (Car car : carList) {
                writer.println(car.getCarId() + "," + car.getBrand() + "," + car.getModel() + "," + 
                             car.getYear() + "," + car.getColor() + "," + car.getFuelType() + "," + 
                             car.getPricePerDay() + "," + car.isAvailable());
            }
            writer.println("=== CUSTOMERS ===");
            for (Customer customer : customerList) {
                writer.println(customer.getCustomerId() + "," + customer.getName() + "," + 
                             customer.getPhone() + "," + customer.getEmail());
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("rental_data.txt"))) {
            String line;
            String section = "";
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("===")) {
                    section = line;
                    continue;
                }
                if (section.equals("=== CUSTOMERS ===") && !line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        Customer customer = new Customer(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3]);
                        customerList.add(customer);
                        nextCustomerId = Math.max(nextCustomerId, customer.getCustomerId() + 1);
                    }
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet, which is fine
        }
    }
}