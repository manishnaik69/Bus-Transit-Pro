package com.busmanagement.service;

import com.busmanagement.model.Route;
import com.busmanagement.repository.InMemoryRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private final InMemoryRouteRepository routeRepository;

    @Autowired
    public RouteService(InMemoryRouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    // Get all routes
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    // Get route by ID
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    // Save route
    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }

    // Delete route
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }

    // Get routes by source city
    public List<Route> getRoutesBySourceCity(Long sourceId) {
        return routeRepository.findBySourceCity(sourceId);
    }

    // Get popular routes
    public List<Route> getPopularRoutes(int limit) {
        return routeRepository.findPopularRoutes(limit);
    }
}