package com.carrental;

import java.time.*;
import java.util.*;

public class DynamicPricing {
    
    // Calculate dynamic price based on multiple factors
    public static double calculatePrice(double basePrice, LocalDate rentalDate, int totalCars, int availableCars) {
        double finalPrice = basePrice;
        
        // Weekend pricing (+20%)
        if (isWeekend(rentalDate)) {
            finalPrice *= 1.20;
        }
        
        // Holiday season (+30%)
        if (isHolidaySeason(rentalDate)) {
            finalPrice *= 1.30;
        }
        
        // Demand-based pricing
        double occupancyRate = (double)(totalCars - availableCars) / totalCars;
        if (occupancyRate > 0.8) {
            finalPrice *= 1.25; // High demand +25%
        } else if (occupancyRate < 0.3) {
            finalPrice *= 0.85; // Low demand -15%
        }
        
        // Peak season (summer: June-August)
        int month = rentalDate.getMonthValue();
        if (month >= 6 && month <= 8) {
            finalPrice *= 1.15; // +15%
        }
        
        return Math.round(finalPrice * 100.0) / 100.0;
    }
    
    // Early bird discount (7+ days advance booking)
    public static double applyEarlyBirdDiscount(double price, LocalDate bookingDate, LocalDate rentalDate) {
        long daysInAdvance = java.time.temporal.ChronoUnit.DAYS.between(bookingDate, rentalDate);
        if (daysInAdvance >= 7) {
            return price * 0.90; // 10% discount
        }
        return price;
    }
    
    // Check if weekend
    private static boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
    
    // Check if holiday season (Dec 15 - Jan 5, major holidays)
    private static boolean isHolidaySeason(LocalDate date) {
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        
        // Christmas/New Year
        if ((month == 12 && day >= 15) || (month == 1 && day <= 5)) {
            return true;
        }
        
        // Add more holidays as needed
        return false;
    }
}
