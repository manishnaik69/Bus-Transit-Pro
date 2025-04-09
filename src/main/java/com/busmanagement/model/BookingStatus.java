package com.busmanagement.model;

/**
 * Enum representing the status of a booking
 */
public enum BookingStatus {
    PENDING,        // Booking is created but payment is pending
    CONFIRMED,      // Booking is confirmed after payment
    CHECKED_IN,     // Passenger has checked in
    COMPLETED,      // Trip completed with this booking
    CANCELLED,      // Booking has been cancelled
    NO_SHOW         // Passenger did not show up for the trip
}