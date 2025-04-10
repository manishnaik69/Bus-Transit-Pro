package com.busmanagement.repository;

import com.busmanagement.model.City;
import com.busmanagement.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Route entity
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
    /**
     * Find routes by source city
     * @param source The source city
     * @return List of routes starting from the given source
     */
    List<Route> findBySource(City source);
    
    /**
     * Find routes by destination city
     * @param destination The destination city
     * @return List of routes ending at the given destination
     */
    List<Route> findByDestination(City destination);
    
    /**
     * Find routes by source and destination cities
     * @param source The source city
     * @param destination The destination city
     * @return List of routes between the given source and destination
     */
    List<Route> findBySourceAndDestination(City source, City destination);
    
    /**
     * Find routes by source city ID
     * @param sourceId The source city ID
     * @return List of routes starting from the city with the given ID
     */
    List<Route> findBySourceId(Long sourceId);
    
    /**
     * Find routes by destination city ID
     * @param destinationId The destination city ID
     * @return List of routes ending at the city with the given ID
     */
    List<Route> findByDestinationId(Long destinationId);
    
    /**
     * Find routes by source city ID and destination city ID
     * @param sourceId The source city ID
     * @param destinationId The destination city ID
     * @return List of routes between the cities with the given IDs
     */
    List<Route> findBySourceIdAndDestinationId(Long sourceId, Long destinationId);
    
    /**
     * Find routes with distance less than or equal to the given value
     * @param maxDistance The maximum distance
     * @return List of routes with distance <= the given value
     */
    List<Route> findByDistanceLessThanEqual(Double maxDistance);
    
    /**
     * Find routes with fare amount less than or equal to the given value
     * @param maxFare The maximum fare
     * @return List of routes with fare <= the given value
     */
    List<Route> findByFareAmountLessThanEqual(Double maxFare);
    
    /**
     * Find popular routes (routes with the most schedules)
     * @param limit The maximum number of routes to return
     * @return List of routes ordered by number of schedules (descending)
     */
    @Query("SELECT r FROM Route r LEFT JOIN r.schedules s GROUP BY r ORDER BY COUNT(s) DESC")
    List<Route> findPopularRoutes(int limit);
}