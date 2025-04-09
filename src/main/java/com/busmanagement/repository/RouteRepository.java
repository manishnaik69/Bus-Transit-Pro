package com.busmanagement.repository;

import com.busmanagement.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Route entity
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
    /**
     * Find a route by route code
     * 
     * @param routeCode Route code to search for
     * @return Optional containing the route if found
     */
    Optional<Route> findByRouteCode(String routeCode);
    
    /**
     * Find routes by source
     * 
     * @param source Source location to search for
     * @return List of routes with the specified source
     */
    List<Route> findBySource(String source);
    
    /**
     * Find routes by destination
     * 
     * @param destination Destination location to search for
     * @return List of routes with the specified destination
     */
    List<Route> findByDestination(String destination);
    
    /**
     * Find routes by source and destination
     * 
     * @param source Source location to search for
     * @param destination Destination location to search for
     * @return List of routes with the specified source and destination
     */
    List<Route> findBySourceAndDestination(String source, String destination);
    
    /**
     * Find active routes
     * 
     * @return List of active routes
     */
    List<Route> findByIsActiveTrue();
    
    /**
     * Find routes by source or destination containing the given keyword
     * 
     * @param keyword Keyword to search for in source or destination
     * @return List of routes with source or destination containing the keyword
     */
    @Query("SELECT r FROM Route r WHERE r.source LIKE %:keyword% OR r.destination LIKE %:keyword%")
    List<Route> findBySourceOrDestinationContaining(@Param("keyword") String keyword);
    
    /**
     * Find routes that pass through a specific location (as a stop)
     * 
     * @param location Location to search for in route stops
     * @return List of routes that pass through the specified location
     */
    @Query("SELECT r FROM Route r JOIN r.stops s WHERE s.name LIKE %:location% OR s.city LIKE %:location%")
    List<Route> findByStopLocation(@Param("location") String location);
}