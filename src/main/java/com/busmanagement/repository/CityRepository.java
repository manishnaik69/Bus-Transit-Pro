package com.busmanagement.repository;

import com.busmanagement.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for City entities.
 * Provides methods to interact with the cities table in the database.
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    
    /**
     * Finds a city by its name.
     * 
     * @param name Name of the city
     * @return Optional containing the city, if found
     */
    Optional<City> findByName(String name);
    
    /**
     * Finds all cities in a specific state.
     * 
     * @param state State name
     * @return List of cities
     */
    List<City> findByState(String state);
    
    /**
     * Finds all cities with names containing the given string.
     * 
     * @param name Name pattern to search for
     * @return List of cities
     */
    List<City> findByNameContainingIgnoreCase(String name);
    
    /**
     * Finds all cities that are sources for at least one route.
     * 
     * @return List of source cities
     */
    @Query("SELECT DISTINCT c FROM City c JOIN c.sourcesRoutes r")
    List<City> findAllSourceCities();
    
    /**
     * Finds all cities that are destinations for at least one route.
     * 
     * @return List of destination cities
     */
    @Query("SELECT DISTINCT c FROM City c JOIN c.destinationRoutes r")
    List<City> findAllDestinationCities();
    
    /**
     * Finds possible destination cities from a given source city.
     * 
     * @param sourceId ID of the source city
     * @return List of possible destination cities
     */
    @Query("SELECT c FROM City c JOIN Route r ON c.id = r.destination.id WHERE r.source.id = ?1")
    List<City> findDestinationCitiesFromSource(Long sourceId);
}
