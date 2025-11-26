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
    private int nextCustomerId = 1;
    private int nextRentalId = 1;

    // Constructor
    public CarService() {
        carList = new ArrayList<>();
        customerList = new ArrayList<>();
        rentalHistory = new ArrayList<>();
        brandIndex = new HashMap<>();
        MySQLDatabaseService.initializeDatabase();
        addSampleCars();
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

        double totalCost = days * carToRent.getPricePerDay();
        carToRent.setAvailable(false);
        
        Rental rental = new Rental(nextRentalId++, customerId, carId, days, totalCost);
        rentalHistory.add(rental);
        
        // Save to database
        MySQLDatabaseService.saveRental(rental, customer, carToRent);
        
        System.out.println("\n=== RENTAL CONFIRMATION ===");
        System.out.println("Customer: " + customer.getName());
        System.out.println("Car: " + carToRent.getYear() + " " + carToRent.getBrand() + " " + carToRent.getModel());
        System.out.println("Rental Days: " + days);
        System.out.println("Total Cost: $" + String.format("%.2f", totalCost));
        System.out.println("Rental ID: " + rental.getRentalId());
        System.out.println("===========================");
        saveData();
    }

    // Return a car by ID
    public void returnCar(int carId) {
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
        for (Rental rental : rentalHistory) {
            if (rental.getCarId() == carId && rental.isActive()) {
                rental.setActive(false);
                break;
            }
        }
        
        // Update database
        MySQLDatabaseService.updateRentalStatus(carId, false);
        
        carToReturn.setAvailable(true);
        System.out.println("Car returned successfully: " + carToReturn.getBrand() + " " + carToReturn.getModel());
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
    
    // Show rental statistics
    public void showStatistics() {
        MySQLDatabaseService.showRentalStatistics();
    }

    // Helper methods
    private Customer findCustomerById(int customerId) {
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