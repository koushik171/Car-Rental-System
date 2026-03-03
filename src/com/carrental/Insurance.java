package com.carrental;

public class Insurance {
    public enum Type {
        NONE(0, 0),
        BASIC(10, 500),      // $10/day, $500 coverage
        PREMIUM(20, 2000),   // $20/day, $2000 coverage
        FULL(35, 10000);     // $35/day, full coverage
        
        private final double dailyCost;
        private final double coverage;
        
        Type(double dailyCost, double coverage) {
            this.dailyCost = dailyCost;
            this.coverage = coverage;
        }
        
        public double getDailyCost() { return dailyCost; }
        public double getCoverage() { return coverage; }
    }
    
    private Type type;
    private int rentalId;
    
    public Insurance(int rentalId, Type type) {
        this.rentalId = rentalId;
        this.type = type;
    }
    
    // Calculate insurance cost for rental period
    public double calculateCost(int days) {
        return type.getDailyCost() * days;
    }
    
    // Calculate customer liability for damage
    public double calculateLiability(double damageAmount) {
        if (type == Type.FULL) return 0;
        if (damageAmount <= type.getCoverage()) return 0;
        return damageAmount - type.getCoverage();
    }
    
    public Type getType() { return type; }
    public int getRentalId() { return rentalId; }
}
