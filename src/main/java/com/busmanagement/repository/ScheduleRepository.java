package com.busmanagement.repository;

import com.busmanagement.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Repository interface for Schedule entities.
 * Provides methods to interact with the schedules table in the database.
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    /**
     * Finds all schedules for a specific route.
     * 
     * @param routeId ID of the route
     * @return List of schedules
     */
    List<Schedule> findByRouteId(Long routeId);
    
    /**
     * Finds all schedules for a specific bus.
     * 
     * @param busId ID of the bus
     * @return List of schedules
     */
    List<Schedule> findByBusId(Long busId);
    
    /**
     * Finds all schedules for a specific driver.
     * 
     * @param driverId ID of the driver
     * @return List of schedules
     */
    List<Schedule> findByDriverId(Long driverId);
    
    /**
     * Finds all schedules with a specific status.
     * 
     * @param status Status of the schedules
     * @return List of schedules
     */
    List<Schedule> findByStatus(String status);
    
    /**
     * Finds all schedules with departure time after a specific date/time.
     * 
     * @param departureTime Minimum departure time
     * @return List of schedules
     */
    List<Schedule> findByDepartureTimeAfter(LocalDateTime departureTime);
    
    /**
     * Finds all schedules for a specific route and date.
     * 
     * @param routeId ID of the route
     * @param date Date of departure
     * @return List of schedules
     */
    @Query("SELECT s FROM Schedule s WHERE s.route.id = :routeId AND FUNCTION('DATE', s.departureTime) = :date")
    List<Schedule> findByRouteIdAndDepartureDate(@Param("routeId") Long routeId, @Param("date") LocalDate date);
    
    /**
     * Finds all schedules between specific source and destination cities on a specific date.
     * 
     * @param sourceId ID of the source city
     * @param destinationId ID of the destination city
     * @param date Date of departure
     * @return List of schedules
     */
    @Query("SELECT s FROM Schedule s JOIN s.route r WHERE r.source.id = :sourceId AND r.destination.id = :destinationId AND FUNCTION('DATE', s.departureTime) = :date AND s.status != 'Cancelled'")
    List<Schedule> findBySourceAndDestinationAndDate(@Param("sourceId") Long sourceId, @Param("destinationId") Long destinationId, @Param("date") LocalDate date);
    
    /**
     * Finds the current schedule for a specific driver.
     * 
     * @param driverId ID of the driver
     * @return List of schedules
     */
    @Query("SELECT s FROM Schedule s WHERE s.driver.id = :driverId AND s.departureTime <= CURRENT_TIMESTAMP AND s.arrivalTime >= CURRENT_TIMESTAMP")
    List<Schedule> findCurrentScheduleForDriver(@Param("driverId") Long driverId);
    
    /**
     * Finds all schedules with available seats.
     * 
     * @param minSeats Minimum number of available seats
     * @return List of schedules
     */
    @Query("SELECT s FROM Schedule s WHERE s.availableSeats >= :minSeats AND s.departureTime > CURRENT_TIMESTAMP")
    List<Schedule> findWithAvailableSeats(@Param("minSeats") int minSeats);
    
    /**
     * Calculates the occupancy rate for each schedule within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return List of schedules with occupancy rates
     */
    @Query("SELECT s, (1 - (s.availableSeats * 1.0 / s.bus.capacity)) as occupancyRate FROM Schedule s WHERE s.departureTime BETWEEN :startDate AND :endDate ORDER BY occupancyRate DESC")
    List<Object[]> getScheduleOccupancyRates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
