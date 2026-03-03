package com.carrental;

import java.time.LocalDate;

public class PromoCode {
    private String code;
    private double discountPercent;
    private LocalDate expiryDate;
    private boolean isActive;
    private int usageLimit;
    private int timesUsed;
    
    public PromoCode(String code, double discountPercent, LocalDate expiryDate, int usageLimit) {
        this.code = code.toUpperCase();
        this.discountPercent = discountPercent;
        this.expiryDate = expiryDate;
        this.usageLimit = usageLimit;
        this.timesUsed = 0;
        this.isActive = true;
    }
    
    // Validate and apply promo code
    public boolean isValid() {
        if (!isActive) return false;
        if (LocalDate.now().isAfter(expiryDate)) return false;
        if (timesUsed >= usageLimit) return false;
        return true;
    }
    
    // Use promo code
    public double applyDiscount(double amount) {
        if (!isValid()) return amount;
        timesUsed++;
        return amount * (1 - discountPercent / 100);
    }
    
    public String getCode() { return code; }
    public double getDiscountPercent() { return discountPercent; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public int getTimesUsed() { return timesUsed; }
    public void setTimesUsed(int timesUsed) { this.timesUsed = timesUsed; }
    public int getUsageLimit() { return usageLimit; }
}
