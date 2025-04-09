package com.busmanagement.repository;

import com.busmanagement.model.Booking;
import com.busmanagement.model.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Booking entity
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Find all bookings for a specific user
     * @param userId ID of the user
     * @return List of bookings
     */
    List<Booking> findByUserId(Long userId);
    
    /**
     * Find all upcoming bookings for a specific user
     * @param userId ID of the user
     * @return List of bookings
     */
    @Query("SELECT b FROM Booking b JOIN b.schedule s WHERE b.user.id = :userId AND s.departureTime > CURRENT_TIME AND b.status != 'CANCELLED'")
    List<Booking> findUpcomingBookingsByUserId(@Param("userId") Long userId);
    
    /**
     * Find all bookings for a specific schedule
     * @param scheduleId ID of the schedule
     * @return List of bookings
     */
    List<Booking> findByScheduleId(Long scheduleId);
    
    /**
     * Find all bookings with a specific status
     * @param status Status of the bookings
     * @return List of bookings
     */
    List<Booking> findByStatus(BookingStatus status);
    
    /**
     * Count the number of bookings for a specific schedule
     * @param scheduleId ID of the schedule
     * @return Number of bookings
     */
    long countByScheduleId(Long scheduleId);
    
    /**
     * Find all bookings for a specific user with a specific status
     * @param userId ID of the user
     * @param status Status of the bookings
     * @return List of bookings
     */
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
}