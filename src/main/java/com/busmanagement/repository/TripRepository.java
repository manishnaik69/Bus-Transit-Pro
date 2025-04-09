package com.busmanagement.repository;

import com.busmanagement.model.Bus;
import com.busmanagement.model.Driver;
import com.busmanagement.model.Route;
import com.busmanagement.model.Trip;
import com.busmanagement.model.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Trip entity
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    
    /**
     * Find trips by status
     * 
     * @param status Trip status to search for
     * @return List of trips with the specified status
     */
    List<Trip> findByStatus(TripStatus status);
    
    /**
     * Find trips by bus
     * 
     * @param bus Bus to search for
     * @return List of trips with the specified bus
     */
    List<Trip> findByBus(Bus bus);
    
    /**
     * Find trips by driver
     * 
     * @param driver Driver to search for
     * @return List of trips with the specified driver
     */
    List<Trip> findByDriver(Driver driver);
    
    /**
     * Find trips by route
     * 
     * @param route Route to search for
     * @return List of trips with the specified route
     */
    List<Trip> findByRoute(Route route);
    
    /**
     * Find trips by source and destination
     * 
     * @param source Source location
     * @param destination Destination location
     * @return List of trips with the specified source and destination
     */
    @Query("SELECT t FROM Trip t JOIN t.route r WHERE r.source = :source AND r.destination = :destination")
    List<Trip> findBySourceAndDestination(@Param("source") String source, @Param("destination") String destination);
    
    /**
     * Find trips by departure date
     * 
     * @param departureStart Start of departure date range
     * @param departureEnd End of departure date range
     * @return List of trips with departure date within the specified range
     */
    List<Trip> findByScheduledDepartureTimeBetween(LocalDateTime departureStart, LocalDateTime departureEnd);
    
    /**
     * Find trips with available seats
     * 
     * @param minSeats Minimum number of available seats
     * @return List of trips with at least the specified number of available seats
     */
    List<Trip> findByAvailableSeatsGreaterThanEqual(Integer minSeats);
    
    /**
     * Find active trips (scheduled, boarding, departed, in transit)
     * 
     * @return List of active trips
     */
    @Query("SELECT t FROM Trip t WHERE t.status IN ('SCHEDULED', 'BOARDING', 'DEPARTED', 'IN_TRANSIT')")
    List<Trip> findActiveTrips();
    
    /**
     * Find trips with current location updates
     * 
     * @return List of trips with current location updates
     */
    @Query("SELECT t FROM Trip t WHERE t.currentLatitude IS NOT NULL AND t.currentLongitude IS NOT NULL")
    List<Trip> findTripsWithLocationUpdates();
}