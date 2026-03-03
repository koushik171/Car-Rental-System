package com.carrental;

/**
 * Car class representing a rental car with basic information
 */
public class Car {
    private int carId;
    private String brand;
    private String model;
    private int year;
    private String color;
    private String fuelType;
    private double pricePerDay;
    private boolean available;
    private Maintenance maintenance;
    private double averageRating;
    private int totalReviews;

    // Constructor
    public Car(int carId, String brand, String model, int year, String color, String fuelType, double pricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.fuelType = fuelType;
        this.pricePerDay = pricePerDay;
        this.available = true;
        this.maintenance = new Maintenance(carId);
        this.averageRating = 0.0;
        this.totalReviews = 0;
    }

    // Getters
    public int getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public String getFuelType() {
        return fuelType;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    // Setters
    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public Maintenance getMaintenance() { return maintenance; }
    public void setMaintenance(Maintenance maintenance) { this.maintenance = maintenance; }
    
    public double getAverageRating() { return averageRating; }
    public int getTotalReviews() { return totalReviews; }
    
    public void addReview(int rating) {
        averageRating = (averageRating * totalReviews + rating) / (totalReviews + 1);
        totalReviews++;
    }

    // toString method for easy display
    @Override
    public String toString() {
        String status = available ? "Available" : "Rented";
        if (maintenance.needsService()) status = "Maintenance";
        String rating = totalReviews > 0 ? String.format(" | ⭐%.1f (%d)", averageRating, totalReviews) : "";
        return String.format("ID: %d | %d %s %s | %s | %s | $%.2f/day | %s%s", 
                           carId, year, brand, model, color, fuelType, pricePerDay, status, rating);
    }
}