package com.busmanagement.repository;

import com.busmanagement.model.City;
import com.busmanagement.model.Route;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryRouteRepository {
    private static InMemoryRouteRepository instance;
    private final Map<Long, Route> routes = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // Change from private to protected constructor to allow Spring to create
    // proxies
    protected InMemoryRouteRepository() {
        // Add sample data
        City mumbai = new City();
        mumbai.setId(1L);
        mumbai.setName("Mumbai");
        mumbai.setState("Maharashtra");
        mumbai.setDescription("Financial capital of India");

        City delhi = new City();
        delhi.setId(2L);
        delhi.setName("Delhi");
        delhi.setState("Delhi");
        delhi.setDescription("Capital city of India");

        City bangalore = new City();
        bangalore.setId(3L);
        bangalore.setName("Bangalore");
        bangalore.setState("Karnataka");
        bangalore.setDescription("Silicon Valley of India");

        // Create sample routes
        Route route1 = new Route();
        route1.setId(idCounter.getAndIncrement());
        route1.setSource(mumbai);
        route1.setDestination(delhi);
        route1.setDistance(1400);
        route1.setDuration(120); // 2 hours in minutes
        route1.setFareAmount(1200.0);
        route1.setCreatedAt(LocalDateTime.now());
        route1.setUpdatedAt(LocalDateTime.now());
        routes.put(route1.getId(), route1);

        Route route2 = new Route();
        route2.setId(idCounter.getAndIncrement());
        route2.setSource(delhi);
        route2.setDestination(bangalore);
        route2.setDistance(2100);
        route2.setDuration(180); // 3 hours in minutes
        route2.setFareAmount(1800.0);
        route2.setCreatedAt(LocalDateTime.now());
        route2.setUpdatedAt(LocalDateTime.now());
        routes.put(route2.getId(), route2);
    }

    // Singleton getInstance method
    public static synchronized InMemoryRouteRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryRouteRepository();
        }
        return instance;
    }

    // Repository methods
    public List<Route> findAll() {
        return new ArrayList<>(routes.values());
    }

    public Optional<Route> findById(Long id) {
        return Optional.ofNullable(routes.get(id));
    }

    public Route save(Route route) {
        if (route.getId() == null) {
            route.setId(idCounter.getAndIncrement());
            route.setCreatedAt(LocalDateTime.now());
        }
        route.setUpdatedAt(LocalDateTime.now());
        routes.put(route.getId(), route);
        return route;
    }

    public void deleteById(Long id) {
        routes.remove(id);
    }

    public List<Route> findBySourceCity(Long sourceId) {
        return routes.values().stream()
                .filter(route -> route.getSource().getId().equals(sourceId))
                .collect(Collectors.toList());
    }

    public List<Route> findPopularRoutes(int limit) {
        // In a real app, this would be based on usage statistics
        // For now, just return the first few routes
        return routes.values().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}