package com.carrental;

public class LoyaltyPoints {
    private int customerId;
    private int points;
    private String tier; // BRONZE, SILVER, GOLD, PLATINUM
    
    public LoyaltyPoints(int customerId) {
        this.customerId = customerId;
        this.points = 0;
        this.tier = "BRONZE";
    }
    
    // Add points: 1 point per $10 spent
    public void addPoints(double amountSpent) {
        int earnedPoints = (int)(amountSpent / 10);
        points += earnedPoints;
        updateTier();
    }
    
    // Redeem points: 100 points = $10 discount
    public double redeemPoints(int pointsToRedeem) {
        if (pointsToRedeem > points) {
            return 0;
        }
        points -= pointsToRedeem;
        updateTier();
        return pointsToRedeem / 10.0;
    }
    
    // Update tier based on points
    private void updateTier() {
        if (points >= 1000) tier = "PLATINUM";
        else if (points >= 500) tier = "GOLD";
        else if (points >= 200) tier = "SILVER";
        else tier = "BRONZE";
    }
    
    // Get tier discount
    public double getTierDiscount() {
        switch(tier) {
            case "PLATINUM": return 0.15; // 15%
            case "GOLD": return 0.10;     // 10%
            case "SILVER": return 0.05;   // 5%
            default: return 0.0;
        }
    }
    
    public int getPoints() { return points; }
    public String getTier() { return tier; }
    public int getCustomerId() { return customerId; }
    public void setPoints(int points) { 
        this.points = points;
        updateTier();
    }
}
