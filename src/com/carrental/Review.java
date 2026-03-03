package com.carrental;

import java.time.LocalDate;

public class Review {
    private int reviewId;
    private int carId;
    private int customerId;
    private String customerName;
    private int rating; // 1-5 stars
    private String comment;
    private LocalDate reviewDate;
    
    public Review(int reviewId, int carId, int customerId, String customerName, int rating, String comment) {
        this.reviewId = reviewId;
        this.carId = carId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.rating = Math.max(1, Math.min(5, rating)); // Ensure 1-5
        this.comment = comment;
        this.reviewDate = LocalDate.now();
    }
    
    @Override
    public String toString() {
        return String.format("⭐ %d/5 - %s (%s)\n   \"%s\"", 
            rating, customerName, reviewDate, comment);
    }
    
    public int getReviewId() { return reviewId; }
    public int getCarId() { return carId; }
    public int getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDate getReviewDate() { return reviewDate; }
}
