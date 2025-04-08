package com.busmanagement.repository;

import com.busmanagement.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Booking entities.
 * Provides methods to interact with the bookings table in the database.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    /**
     * Finds all bookings for a specific user.
     * 
     * @param userId ID of the user
     * @return List of bookings
     */
    List<Booking> findByUserId(Long userId);
    
    /**
     * Finds all active (non-cancelled) bookings for a specific user.
     * 
     * @param userId ID of the user
     * @return List of active bookings
     */
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status != 'CANCELLED'")
    List<Booking> findActiveBookingsByUserId(@Param("userId") Long userId);
    
    /**
     * Finds all upcoming bookings for a specific user.
     * 
     * @param userId ID of the user
     * @param currentDate Current date
     * @return List of upcoming bookings
     */
    @Query("SELECT b FROM Booking b JOIN b.schedule s WHERE b.user.id = :userId AND s.departureTime >= CURRENT_TIMESTAMP AND b.status != 'CANCELLED' ORDER BY s.departureTime ASC")
    List<Booking> findUpcomingBookingsByUserId(@Param("userId") Long userId);
    
    /**
     * Finds all bookings for a specific schedule.
     * 
     * @param scheduleId ID of the schedule
     * @return List of bookings
     */
    List<Booking> findByScheduleId(Long scheduleId);
    
    /**
     * Finds all bookings with a specific status.
     * 
     * @param status Status of the bookings
     * @return List of bookings
     */
    List<Booking> findByStatus(String status);
    
    /**
     * Counts the number of bookings for a specific schedule.
     * 
     * @param scheduleId ID of the schedule
     * @return Number of bookings
     */
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.schedule.id = :scheduleId AND b.status != 'CANCELLED'")
    Long countActiveBookingsByScheduleId(@Param("scheduleId") Long scheduleId);
    
    /**
     * Counts the number of bookings created within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Number of bookings
     */
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingDate BETWEEN :startDate AND :endDate")
    Long countBookingsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * Gets the total revenue from bookings within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Total revenue
     */
    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status = 'PAID' AND b.bookingDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * Finds the most recent bookings.
     * 
     * @param limit Maximum number of bookings to return
     * @return List of recent bookings
     */
    @Query("SELECT b FROM Booking b ORDER BY b.createdAt DESC")
    List<Booking> findRecentBookings(int limit);
}
