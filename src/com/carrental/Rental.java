package com.carrental;

import java.time.LocalDate;

/**
 * Rental class representing a car rental transaction
 */
public class Rental {
    private int rentalId;
    private int customerId;
    private int carId;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    private int days;
    private double totalCost;
    private boolean isActive;
    private Insurance insurance;
    private double lateFee;
    private int estimatedMileage;

    public Rental(int rentalId, int customerId, int carId, int days, double totalCost) {
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.carId = carId;
        this.days = days;
        this.totalCost = totalCost;
        this.rentalDate = LocalDate.now();
        this.returnDate = rentalDate.plusDays(days);
        this.isActive = true;
        this.insurance = new Insurance(rentalId, Insurance.Type.NONE);
        this.lateFee = 0;
        this.estimatedMileage = days * 100; // Estimate 100km per day
    }

    // Getters and Setters
    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }
    
    public LocalDate getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDate rentalDate) { this.rentalDate = rentalDate; }
    
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    
    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }
    
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public Insurance getInsurance() { return insurance; }
    public void setInsurance(Insurance insurance) { this.insurance = insurance; }
    
    public double getLateFee() { return lateFee; }
    public void setLateFee(double lateFee) { this.lateFee = lateFee; }
    
    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDate date) { this.actualReturnDate = date; }
    
    public int getEstimatedMileage() { return estimatedMileage; }
    public void setEstimatedMileage(int mileage) { this.estimatedMileage = mileage; }
    
    // Calculate late fee: $50/day
    public double calculateLateFee() {
        if (actualReturnDate == null || !actualReturnDate.isAfter(returnDate)) {
            return 0;
        }
        long daysLate = java.time.temporal.ChronoUnit.DAYS.between(returnDate, actualReturnDate);
        lateFee = daysLate * 50;
        return lateFee;
    }

    @Override
    public String toString() {
        String lateInfo = lateFee > 0 ? String.format(" | Late Fee: $%.2f", lateFee) : "";
        return String.format("Rental ID: %d | Customer: %d | Car: %d | Days: %d | Cost: $%.2f | Date: %s%s", 
                           rentalId, customerId, carId, days, totalCost, rentalDate, lateInfo);
    }
}