package com.busmanagement.repository;

import com.busmanagement.model.Booking;
import com.busmanagement.model.BookingStatus;
import com.busmanagement.model.Passenger;
import com.busmanagement.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Booking entity
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    /**
     * Find a booking by booking number
     * 
     * @param bookingNumber Booking number to search for
     * @return Optional containing the booking if found
     */
    Optional<Booking> findByBookingNumber(String bookingNumber);
    
    /**
     * Find bookings by passenger
     * 
     * @param passenger Passenger to search for
     * @return List of bookings for the specified passenger
     */
    List<Booking> findByPassenger(Passenger passenger);
    
    /**
     * Find bookings by trip
     * 
     * @param trip Trip to search for
     * @return List of bookings for the specified trip
     */
    List<Booking> findByTrip(Trip trip);
    
    /**
     * Find bookings by status
     * 
     * @param status Booking status to search for
     * @return List of bookings with the specified status
     */
    List<Booking> findByStatus(BookingStatus status);
    
    /**
     * Find bookings by booking date
     * 
     * @param startDate Start of booking date range
     * @param endDate End of booking date range
     * @return List of bookings with booking date within the specified range
     */
    List<Booking> findByBookingTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find bookings for passenger with status
     * 
     * @param passenger Passenger to search for
     * @param status Booking status to search for
     * @return List of bookings for the specified passenger with the specified status
     */
    List<Booking> findByPassengerAndStatus(Passenger passenger, BookingStatus status);
    
    /**
     * Find active bookings for passenger (upcoming trips)
     * 
     * @param passenger Passenger to search for
     * @param currentTime Current time
     * @return List of active bookings for the specified passenger
     */
    @Query("SELECT b FROM Booking b JOIN b.trip t WHERE b.passenger = :passenger AND " +
           "b.status IN ('CONFIRMED', 'CHECKED_IN') AND t.scheduledDepartureTime > :currentTime")
    List<Booking> findActiveBookingsForPassenger(@Param("passenger") Passenger passenger, 
                                                @Param("currentTime") LocalDateTime currentTime);
    
    /**
     * Find past bookings for passenger (completed trips)
     * 
     * @param passenger Passenger to search for
     * @param currentTime Current time
     * @return List of past bookings for the specified passenger
     */
    @Query("SELECT b FROM Booking b JOIN b.trip t WHERE b.passenger = :passenger AND " +
           "(t.scheduledArrivalTime < :currentTime OR b.status = 'COMPLETED')")
    List<Booking> findPastBookingsForPassenger(@Param("passenger") Passenger passenger, 
                                              @Param("currentTime") LocalDateTime currentTime);
}