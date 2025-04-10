package com.busmanagement.repository;

import com.busmanagement.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for City entity
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    
    /**
     * Find a city by its name
     * @param name The city name
     * @return The city if found
     */
    City findByName(String name);
    
    /**
     * Find cities by state
     * @param state The state name
     * @return List of cities in the given state
     */
    List<City> findByState(String state);
    
    /**
     * Find cities by containing the given text in their name
     * @param nameKeyword The keyword to search for in city names
     * @return List of cities whose names contain the given keyword
     */
    List<City> findByNameContainingIgnoreCase(String nameKeyword);
    
    /**
     * Check if a city exists with the given name
     * @param name The city name
     * @return True if a city exists with the given name, false otherwise
     */
    boolean existsByName(String name);
}