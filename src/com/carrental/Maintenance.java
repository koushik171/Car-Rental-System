package com.carrental;

import java.time.LocalDate;

public class Maintenance {
    private int carId;
    private int currentMileage;
    private int lastServiceMileage;
    private LocalDate lastServiceDate;
    private boolean needsService;
    private static final int SERVICE_INTERVAL = 5000; // km
    
    public Maintenance(int carId) {
        this.carId = carId;
        this.currentMileage = 0;
        this.lastServiceMileage = 0;
        this.lastServiceDate = LocalDate.now();
        this.needsService = false;
    }
    
    // Add mileage after rental
    public void addMileage(int miles) {
        currentMileage += miles;
        checkServiceDue();
    }
    
    // Check if service is due
    private void checkServiceDue() {
        if (currentMileage - lastServiceMileage >= SERVICE_INTERVAL) {
            needsService = true;
        }
    }
    
    // Perform service
    public void performService() {
        lastServiceMileage = currentMileage;
        lastServiceDate = LocalDate.now();
        needsService = false;
    }
    
    // Get days since last service
    public long getDaysSinceService() {
        return java.time.temporal.ChronoUnit.DAYS.between(lastServiceDate, LocalDate.now());
    }
    
    // Get mileage until next service
    public int getMileageUntilService() {
        return SERVICE_INTERVAL - (currentMileage - lastServiceMileage);
    }
    
    public int getCarId() { return carId; }
    public int getCurrentMileage() { return currentMileage; }
    public boolean needsService() { return needsService; }
    public LocalDate getLastServiceDate() { return lastServiceDate; }
    
    public void setCurrentMileage(int mileage) { 
        this.currentMileage = mileage;
        checkServiceDue();
    }
    public void setLastServiceMileage(int mileage) { this.lastServiceMileage = mileage; }
    public void setLastServiceDate(LocalDate date) { this.lastServiceDate = date; }
}
