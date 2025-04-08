package com.busmanagement.service;

import com.busmanagement.model.City;

import java.util.List;

/**
 * Service interface for managing city operations.
 */
public interface CityService {
    
    /**
     * Finds a city by its ID.
     * 
     * @param id ID of the city
     * @return The city, or null if not found
     */
    City findCityById(Long id);
    
    /**
     * Finds a city by its name.
     * 
     * @param name Name of the city
     * @return The city, or null if not found
     */
    City findCityByName(String name);
    
    /**
     * Finds all cities in the system.
     * 
     * @return List of all cities
     */
    List<City> findAllCities();
    
    /**
     * Finds all cities in a specific state.
     * 
     * @param state State name
     * @return List of cities
     */
    List<City> findCitiesByState(String state);
    
    /**
     * Finds all cities with names containing the given string.
     * 
     * @param name Name pattern to search for
     * @return List of cities
     */
    List<City> searchCitiesByName(String name);
    
    /**
     * Saves a new city.
     * 
     * @param city The city to save
     * @return The saved city
     */
    City saveCity(City city);
    
    /**
     * Updates an existing city.
     * 
     * @param id ID of the city to update
     * @param city The updated city data
     * @return The updated city
     */
    City updateCity(Long id, City city);
    
    /**
     * Deletes a city.
     * 
     * @param cityId ID of the city to delete
     */
    void deleteCity(Long cityId);
    
    /**
     * Finds all cities that are sources for at least one route.
     * 
     * @return List of source cities
     */
    List<City> findAllSourceCities();
    
    /**
     * Finds all cities that are destinations for at least one route.
     * 
     * @return List of destination cities
     */
    List<City> findAllDestinationCities();
    
    /**
     * Finds possible destination cities from a given source city.
     * 
     * @param sourceId ID of the source city
     * @return List of possible destination cities
     */
    List<City> findDestinationCitiesFromSource(Long sourceId);
}
