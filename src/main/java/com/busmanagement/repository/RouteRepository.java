package com.busmanagement.repository;

import com.busmanagement.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Route entity
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    /**
     * Find routes by source and destination IDs
     * @param sourceId Source city ID
     * @param destinationId Destination city ID
     * @return List of routes
     */
    List<Route> findBySourceIdAndDestinationId(Long sourceId, Long destinationId);
    
    /**
     * Find the average fare amount across all routes
     * @return Average fare amount
     */
    @Query("SELECT AVG(r.fareAmount) FROM Route r")
    Double getAverageFareAmount();
    
    /**
     * Find most popular routes based on booking count
     * @param startDate Start date for booking range
     * @param endDate End date for booking range
     * @param limit Number of routes to return
     * @return List of route objects and booking counts
     */
    @Query("SELECT r, COUNT(b) as bookingCount FROM Route r " +
           "JOIN Schedule s ON s.route.id = r.id " +
           "JOIN Booking b ON b.schedule.id = s.id " +
           "WHERE b.bookingTime BETWEEN :startDate AND :endDate " +
           "GROUP BY r.id ORDER BY bookingCount DESC")
    List<Object[]> findMostPopularRoutes(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate, 
        @Param("limit") int limit
    );
}