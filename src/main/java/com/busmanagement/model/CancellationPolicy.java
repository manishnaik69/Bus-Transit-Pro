package com.busmanagement.model;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Class representing cancellation policies for bookings
 * Implemented using the Singleton Pattern
 */
public class CancellationPolicy {
    private static CancellationPolicy instance;
    
    private double fullRefundThresholdHours = 24.0; // Hours before departure for full refund
    private double partialRefundThresholdHours = 6.0; // Hours before departure for partial refund
    private double partialRefundPercentage = 50.0; // Percentage of fare refunded for partial refunds
    private double cancellationFee = 100.0; // Fixed cancellation fee amount

    /**
     * Private constructor to prevent instantiation
     */
    private CancellationPolicy() {
    }

    /**
     * Get the singleton instance
     * 
     * @return The CancellationPolicy instance
     */
    public static CancellationPolicy getInstance() {
        if (instance == null) {
            instance = new CancellationPolicy();
        }
        return instance;
    }

    /**
     * Calculate refund amount based on the time difference between cancellation and departure
     * 
     * @param bookingFare Original booking fare
     * @param departureTime Scheduled departure time
     * @param cancellationTime Cancellation time
     * @return Refund amount
     */
    public double calculateRefundAmount(double bookingFare, LocalDateTime departureTime, LocalDateTime cancellationTime) {
        Duration timeToTrip = Duration.between(cancellationTime, departureTime);
        double hoursToTrip = timeToTrip.toHours();
        
        // Check if cancellation is after departure
        if (hoursToTrip <= 0) {
            return 0.0;
        }
        
        // Full refund (minus cancellation fee)
        if (hoursToTrip >= fullRefundThresholdHours) {
            return bookingFare - cancellationFee;
        }
        
        // Partial refund (minus cancellation fee)
        if (hoursToTrip >= partialRefundThresholdHours) {
            double refundAmount = (bookingFare * partialRefundPercentage / 100.0) - cancellationFee;
            return Math.max(0.0, refundAmount);
        }
        
        // No refund
        return 0.0;
    }
    
    // Getters and setters
    public double getFullRefundThresholdHours() {
        return fullRefundThresholdHours;
    }

    public void setFullRefundThresholdHours(double fullRefundThresholdHours) {
        this.fullRefundThresholdHours = fullRefundThresholdHours;
    }

    public double getPartialRefundThresholdHours() {
        return partialRefundThresholdHours;
    }

    public void setPartialRefundThresholdHours(double partialRefundThresholdHours) {
        this.partialRefundThresholdHours = partialRefundThresholdHours;
    }

    public double getPartialRefundPercentage() {
        return partialRefundPercentage;
    }

    public void setPartialRefundPercentage(double partialRefundPercentage) {
        this.partialRefundPercentage = partialRefundPercentage;
    }

    public double getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(double cancellationFee) {
        this.cancellationFee = cancellationFee;
    }
}