package com.busmanagement.service.impl;

import com.busmanagement.dto.RouteDTO;
import com.busmanagement.model.City;
import com.busmanagement.model.Route;
import com.busmanagement.repository.CityRepository;
import com.busmanagement.repository.RouteRepository;
import com.busmanagement.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the RouteService interface.
 * Provides business logic for route operations.
 */
@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteRepository routeRepository;
    
    @Autowired
    private CityRepository cityRepository;

    @Override
    public Route findRouteById(Long id) {
        Optional<Route> route = routeRepository.findById(id);
        return route.orElse(null);
    }

    @Override
    public List<Route> findAllRoutes() {
        return routeRepository.findAll();
    }

    @Override
    public List<Route> findRoutesBySource(Long sourceId) {
        return routeRepository.findBySourceId(sourceId);
    }

    @Override
    public List<Route> findRoutesByDestination(Long destinationId) {
        return routeRepository.findByDestinationId(destinationId);
    }

    @Override
    public List<Route> findRoutesBySourceAndDestination(Long sourceId, Long destinationId) {
        return routeRepository.findBySourceIdAndDestinationId(sourceId, destinationId);
    }

    @Override
    @Transactional
    public Route saveRoute(RouteDTO routeDTO) {
        // Validate input
        if (routeDTO == null || routeDTO.getSourceId() == null || routeDTO.getDestinationId() == null ||
            routeDTO.getDistance() == null || routeDTO.getDuration() == null || routeDTO.getFareAmount() == null) {
            throw new IllegalArgumentException("Invalid route details");
        }
        
        // Check if source and destination cities exist
        Optional<City> sourceOpt = cityRepository.findById(routeDTO.getSourceId());
        Optional<City> destinationOpt = cityRepository.findById(routeDTO.getDestinationId());
        
        if (!sourceOpt.isPresent()) {
            throw new IllegalArgumentException("Source city not found with id: " + routeDTO.getSourceId());
        }
        
        if (!destinationOpt.isPresent()) {
            throw new IllegalArgumentException("Destination city not found with id: " + routeDTO.getDestinationId());
        }
        
        City source = sourceOpt.get();
        City destination = destinationOpt.get();
        
        // Check if source and destination are different
        if (source.getId().equals(destination.getId())) {
            throw new IllegalArgumentException("Source and destination cities must be different");
        }
        
        // Check if route already exists
        List<Route> existingRoutes = routeRepository.findBySourceIdAndDestinationId(source.getId(), destination.getId());
        if (!existingRoutes.isEmpty()) {
            throw new IllegalArgumentException("A route from " + source.getName() + " to " + destination.getName() + " already exists");
        }
        
        // Create and save the route
        Route route = new Route();
        route.setSource(source);
        route.setDestination(destination);
        route.setDistance(routeDTO.getDistance());
        route.setEstimatedDuration(routeDTO.getDuration().intValue());
        route.setFareAmount(routeDTO.getFareAmount());
        
        if (!validateRoute(route)) {
            throw new IllegalArgumentException("Invalid route details");
        }
        
        return routeRepository.save(route);
    }

    @Override
    @Transactional
    public Route updateRoute(Long id, RouteDTO routeDTO) {
        // Check if route exists
        Route existingRoute = findRouteById(id);
        if (existingRoute == null) {
            throw new IllegalArgumentException("Route not found with id: " + id);
        }
        
        // Validate input
        if (routeDTO == null || routeDTO.getSourceId() == null || routeDTO.getDestinationId() == null ||
            routeDTO.getDistance() == null || routeDTO.getDuration() == null || routeDTO.getFareAmount() == null) {
            throw new IllegalArgumentException("Invalid route details");
        }
        
        // Check if source and destination cities exist
        Optional<City> sourceOpt = cityRepository.findById(routeDTO.getSourceId());
        Optional<City> destinationOpt = cityRepository.findById(routeDTO.getDestinationId());
        
        if (!sourceOpt.isPresent()) {
            throw new IllegalArgumentException("Source city not found with id: " + routeDTO.getSourceId());
        }
        
        if (!destinationOpt.isPresent()) {
            throw new IllegalArgumentException("Destination city not found with id: " + routeDTO.getDestinationId());
        }
        
        City source = sourceOpt.get();
        City destination = destinationOpt.get();
        
        // Check if source and destination are different
        if (source.getId().equals(destination.getId())) {
            throw new IllegalArgumentException("Source and destination cities must be different");
        }
        
        // Check if the updated route conflicts with an existing route
        if (!existingRoute.getSource().getId().equals(source.getId()) || 
            !existingRoute.getDestination().getId().equals(destination.getId())) {
            
            List<Route> existingRoutes = routeRepository.findBySourceIdAndDestinationId(source.getId(), destination.getId());
            if (!existingRoutes.isEmpty() && !existingRoutes.get(0).getId().equals(id)) {
                throw new IllegalArgumentException("A route from " + source.getName() + " to " + destination.getName() + " already exists");
            }
        }
        
        // Update the route
        existingRoute.setSource(source);
        existingRoute.setDestination(destination);
        existingRoute.setDistance(routeDTO.getDistance());
        existingRoute.setEstimatedDuration(routeDTO.getDuration().intValue());
        existingRoute.setFareAmount(routeDTO.getFareAmount());
        
        if (!validateRoute(existingRoute)) {
            throw new IllegalArgumentException("Invalid route details");
        }
        
        return routeRepository.save(existingRoute);
    }

    @Override
    @Transactional
    public void deleteRoute(Long routeId) {
        // Check if route exists
        if (!routeRepository.existsById(routeId)) {
            throw new IllegalArgumentException("Route not found with id: " + routeId);
        }
        
        // Check if the route has any schedules before deleting
        Route route = findRouteById(routeId);
        if (route.getSchedules() != null && !route.getSchedules().isEmpty()) {
            throw new IllegalStateException("Cannot delete route as it has associated schedules");
        }
        
        routeRepository.deleteById(routeId);
    }

    @Override
    public boolean validateRoute(Route route) {
        // Check if source and destination are valid
        if (route.getSource() == null || route.getDestination() == null) {
            return false;
        }
        
        // Check if source and destination are different
        if (route.getSource().getId().equals(route.getDestination().getId())) {
            return false;
        }
        
        // Check if distance is valid
        if (route.getDistance() == null || route.getDistance() <= 0) {
            return false;
        }
        
        // Check if duration is valid
        if (route.getEstimatedDuration() == null || route.getEstimatedDuration() <= 0) {
            return false;
        }
        
        // Check if fare amount is valid
        if (route.getFareAmount() == null || route.getFareAmount() <= 0) {
            return false;
        }
        
        return true;
    }

    @Override
    public List<City> findAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public City findCityById(Long id) {
        Optional<City> city = cityRepository.findById(id);
        return city.orElse(null);
    }

    @Override
    public Map<Route, Long> getMostPopularRoutes(LocalDate startDate, LocalDate endDate, int limit) {
        List<Object[]> popularRoutes = routeRepository.findMostPopularRoutes(startDate, endDate, limit);
        Map<Route, Long> result = new LinkedHashMap<>();
        
        for (Object[] row : popularRoutes) {
            Route route = (Route) row[0];
            Long bookingCount = (Long) row[1];
            result.put(route, bookingCount);
        }
        
        return result;
    }

    @Override
    public double getAverageFareAmount() {
        Double avgFare = routeRepository.getAverageFareAmount();
        return avgFare != null ? avgFare : 0.0;
    }

    @Override
    public long countAllRoutes() {
        return routeRepository.count();
    }
}
