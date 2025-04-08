package com.busmanagement.repository;

import com.busmanagement.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Route entities.
 * Provides methods to interact with the routes table in the database.
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
    /**
     * Finds all routes with a specific source city.
     * 
     * @param sourceId ID of the source city
     * @return List of routes
     */
    List<Route> findBySourceId(Long sourceId);
    
    /**
     * Finds all routes with a specific destination city.
     * 
     * @param destinationId ID of the destination city
     * @return List of routes
     */
    List<Route> findByDestinationId(Long destinationId);
    
    /**
     * Finds a route between specific source and destination cities.
     * 
     * @param sourceId ID of the source city
     * @param destinationId ID of the destination city
     * @return List of routes
     */
    List<Route> findBySourceIdAndDestinationId(Long sourceId, Long destinationId);
    
    /**
     * Finds all routes with a distance less than or equal to the given value.
     * 
     * @param distance Maximum distance
     * @return List of routes
     */
    List<Route> findByDistanceLessThanEqual(Double distance);
    
    /**
     * Finds the most popular routes based on the number of bookings.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @param limit Maximum number of routes to return
     * @return List of routes with booking counts
     */
    @Query(value = "SELECT r.*, COUNT(b.id) as booking_count FROM routes r " +
           "JOIN schedules s ON r.id = s.route_id " +
           "JOIN bookings b ON s.id = b.schedule_id " +
           "WHERE b.booking_date BETWEEN :startDate AND :endDate " +
           "GROUP BY r.id ORDER BY booking_count DESC LIMIT :limit", 
           nativeQuery = true)
    List<Object[]> findMostPopularRoutes(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("limit") int limit);
    
    /**
     * Calculates the average fare amount across all routes.
     * 
     * @return Average fare amount
     */
    @Query("SELECT AVG(r.fareAmount) FROM Route r")
    Double getAverageFareAmount();
    
    /**
     * Finds routes with fare amount in a specific range.
     * 
     * @param minFare Minimum fare amount
     * @param maxFare Maximum fare amount
     * @return List of routes
     */
    @Query("SELECT r FROM Route r WHERE r.fareAmount BETWEEN :minFare AND :maxFare")
    List<Route> findByFareAmountBetween(@Param("minFare") Double minFare, @Param("maxFare") Double maxFare);
}
