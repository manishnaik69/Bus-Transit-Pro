package com.busmanagement.service;

import com.busmanagement.dto.RouteDTO;
import com.busmanagement.model.City;
import com.busmanagement.model.Route;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing route operations.
 */
public interface RouteService {
    
    /**
     * Finds a route by its ID.
     * 
     * @param id ID of the route
     * @return The route, or null if not found
     */
    Route findRouteById(Long id);
    
    /**
     * Finds all routes in the system.
     * 
     * @return List of all routes
     */
    List<Route> findAllRoutes();
    
    /**
     * Finds all routes from a specific source city.
     * 
     * @param sourceId ID of the source city
     * @return List of routes
     */
    List<Route> findRoutesBySource(Long sourceId);
    
    /**
     * Finds all routes to a specific destination city.
     * 
     * @param destinationId ID of the destination city
     * @return List of routes
     */
    List<Route> findRoutesByDestination(Long destinationId);
    
    /**
     * Finds routes between specific source and destination cities.
     * 
     * @param sourceId ID of the source city
     * @param destinationId ID of the destination city
     * @return List of routes
     */
    List<Route> findRoutesBySourceAndDestination(Long sourceId, Long destinationId);
    
    /**
     * Saves a new route from a DTO.
     * 
     * @param routeDTO The route data to save
     * @return The saved route
     */
    Route saveRoute(RouteDTO routeDTO);
    
    /**
     * Updates an existing route from a DTO.
     * 
     * @param id ID of the route to update
     * @param routeDTO The updated route data
     * @return The updated route
     */
    Route updateRoute(Long id, RouteDTO routeDTO);
    
    /**
     * Deletes a route.
     * 
     * @param routeId ID of the route to delete
     */
    void deleteRoute(Long routeId);
    
    /**
     * Validates a route to ensure it meets all business rules.
     * 
     * @param route The route to validate
     * @return true if the route is valid, false otherwise
     */
    boolean validateRoute(Route route);
    
    /**
     * Finds all cities in the system.
     * 
     * @return List of all cities
     */
    List<City> findAllCities();
    
    /**
     * Finds a city by its ID.
     * 
     * @param id ID of the city
     * @return The city, or null if not found
     */
    City findCityById(Long id);
    
    /**
     * Gets the most popular routes based on the number of bookings.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @param limit Maximum number of routes to return
     * @return Map of routes to booking counts
     */
    Map<Route, Long> getMostPopularRoutes(LocalDate startDate, LocalDate endDate, int limit);
    
    /**
     * Calculates the average fare amount across all routes.
     * 
     * @return Average fare amount
     */
    double getAverageFareAmount();
    
    /**
     * Counts the total number of routes.
     * 
     * @return Total number of routes
     */
    long countAllRoutes();
}
