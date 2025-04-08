package com.busmanagement.service;

import com.busmanagement.model.Booking;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing booking operations.
 */
public interface BookingService {
    
    /**
     * Finds a booking by its ID.
     * 
     * @param id ID of the booking
     * @return The booking, or null if not found
     */
    Booking findBookingById(Long id);
    
    /**
     * Finds all bookings in the system.
     * 
     * @return List of all bookings
     */
    List<Booking> findAllBookings();
    
    /**
     * Finds all bookings for a specific user.
     * 
     * @param userId ID of the user
     * @return List of bookings
     */
    List<Booking> findAllBookingsByUserId(Long userId);
    
    /**
     * Finds all upcoming bookings for a specific user.
     * 
     * @param userId ID of the user
     * @return List of upcoming bookings
     */
    List<Booking> findUpcomingBookingsByUserId(Long userId);
    
    /**
     * Finds all bookings for a specific schedule.
     * 
     * @param scheduleId ID of the schedule
     * @return List of bookings
     */
    List<Booking> findBookingsByScheduleId(Long scheduleId);
    
    /**
     * Saves a new booking.
     * 
     * @param booking The booking to save
     * @return The saved booking
     */
    Booking saveBooking(Booking booking);
    
    /**
     * Updates an existing booking.
     * 
     * @param booking The booking to update
     * @return The updated booking
     */
    Booking updateBooking(Booking booking);
    
    /**
     * Updates the status of a booking.
     * 
     * @param bookingId ID of the booking
     * @param status New status
     * @return The updated booking
     */
    Booking updateBookingStatus(Long bookingId, String status);
    
    /**
     * Cancels a booking.
     * 
     * @param bookingId ID of the booking to cancel
     * @return The cancelled booking
     */
    Booking cancelBooking(Long bookingId);
    
    /**
     * Deletes a booking.
     * 
     * @param bookingId ID of the booking to delete
     */
    void deleteBooking(Long bookingId);
    
    /**
     * Validates a booking to ensure it meets all business rules.
     * 
     * @param booking The booking to validate
     * @return true if the booking is valid, false otherwise
     */
    boolean validateBooking(Booking booking);
    
    /**
     * Counts the total number of bookings.
     * 
     * @return Total number of bookings
     */
    long countTotalBookings();
    
    /**
     * Counts the number of bookings created within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Number of bookings
     */
    long countBookingsBetweenDates(LocalDate startDate, LocalDate endDate);
    
    /**
     * Gets the total revenue from bookings within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Total revenue
     */
    double getTotalRevenueBetweenDates(LocalDate startDate, LocalDate endDate);
}
