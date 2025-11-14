package com.example.Booking.dto;

import java.math.BigDecimal;

public class UserBookingStats {
    
    private Long totalBookings;
    private Long confirmedBookings;
    private Long cancelledBookings;
    private Long completedBookings;
    private BigDecimal totalSpent;
    private String mostVisitedDestination;
    
    // Default constructor
    public UserBookingStats() {}
    
    // All args constructor
    public UserBookingStats(Long totalBookings, Long confirmedBookings, Long cancelledBookings, 
                           Long completedBookings, BigDecimal totalSpent, String mostVisitedDestination) {
        this.totalBookings = totalBookings;
        this.confirmedBookings = confirmedBookings;
        this.cancelledBookings = cancelledBookings;
        this.completedBookings = completedBookings;
        this.totalSpent = totalSpent;
        this.mostVisitedDestination = mostVisitedDestination;
    }
    
    // Getters and Setters
    public Long getTotalBookings() {
        return totalBookings;
    }
    
    public void setTotalBookings(Long totalBookings) {
        this.totalBookings = totalBookings;
    }
    
    public Long getConfirmedBookings() {
        return confirmedBookings;
    }
    
    public void setConfirmedBookings(Long confirmedBookings) {
        this.confirmedBookings = confirmedBookings;
    }
    
    public Long getCancelledBookings() {
        return cancelledBookings;
    }
    
    public void setCancelledBookings(Long cancelledBookings) {
        this.cancelledBookings = cancelledBookings;
    }
    
    public Long getCompletedBookings() {
        return completedBookings;
    }
    
    public void setCompletedBookings(Long completedBookings) {
        this.completedBookings = completedBookings;
    }
    
    public BigDecimal getTotalSpent() {
        return totalSpent;
    }
    
    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }
    
    public String getMostVisitedDestination() {
        return mostVisitedDestination;
    }
    
    public void setMostVisitedDestination(String mostVisitedDestination) {
        this.mostVisitedDestination = mostVisitedDestination;
    }
}