package com.busmanagement.service.impl;

import com.busmanagement.model.City;
import com.busmanagement.repository.CityRepository;
import com.busmanagement.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the CityService interface.
 * Provides business logic for city operations.
 */
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public City findCityById(Long id) {
        Optional<City> city = cityRepository.findById(id);
        return city.orElse(null);
    }

    @Override
    public City findCityByName(String name) {
        Optional<City> city = cityRepository.findByName(name);
        return city.orElse(null);
    }

    @Override
    public List<City> findAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public List<City> findCitiesByState(String state) {
        return cityRepository.findByState(state);
    }

    @Override
    public List<City> searchCitiesByName(String name) {
        return cityRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional
    public City saveCity(City city) {
        // Check if a city with the same name already exists
        Optional<City> existingCity = cityRepository.findByName(city.getName());
        if (existingCity.isPresent()) {
            throw new IllegalArgumentException("A city with name " + city.getName() + " already exists");
        }
        
        // Validate the city
        if (!isValidCity(city)) {
            throw new IllegalArgumentException("Invalid city details");
        }
        
        return cityRepository.save(city);
    }

    @Override
    @Transactional
    public City updateCity(Long id, City city) {
        // Check if the city exists
        City existingCity = findCityById(id);
        if (existingCity == null) {
            throw new IllegalArgumentException("City not found with id: " + id);
        }
        
        // Check if the name is being changed and if it conflicts with an existing city
        if (!existingCity.getName().equals(city.getName())) {
            Optional<City> cityWithSameName = cityRepository.findByName(city.getName());
            if (cityWithSameName.isPresent() && !cityWithSameName.get().getId().equals(id)) {
                throw new IllegalArgumentException("A city with name " + city.getName() + " already exists");
            }
        }
        
        // Update the city
        existingCity.setName(city.getName());
        existingCity.setState(city.getState());
        existingCity.setDescription(city.getDescription());
        
        // Validate the city
        if (!isValidCity(existingCity)) {
            throw new IllegalArgumentException("Invalid city details");
        }
        
        return cityRepository.save(existingCity);
    }

    @Override
    @Transactional
    public void deleteCity(Long cityId) {
        // Check if the city exists
        if (!cityRepository.existsById(cityId)) {
            throw new IllegalArgumentException("City not found with id: " + cityId);
        }
        
        // Check if the city is used in any routes before deleting
        City city = findCityById(cityId);
        if ((city.getSourcesRoutes() != null && !city.getSourcesRoutes().isEmpty()) || 
            (city.getDestinationRoutes() != null && !city.getDestinationRoutes().isEmpty())) {
            throw new IllegalStateException("Cannot delete city as it is used in routes");
        }
        
        cityRepository.deleteById(cityId);
    }

    @Override
    public List<City> findAllSourceCities() {
        return cityRepository.findAllSourceCities();
    }

    @Override
    public List<City> findAllDestinationCities() {
        return cityRepository.findAllDestinationCities();
    }

    @Override
    public List<City> findDestinationCitiesFromSource(Long sourceId) {
        return cityRepository.findDestinationCitiesFromSource(sourceId);
    }
    
    /**
     * Validates a city entity.
     * 
     * @param city The city to validate
     * @return true if the city is valid, false otherwise
     */
    private boolean isValidCity(City city) {
        // Check if name is valid
        if (city.getName() == null || city.getName().trim().isEmpty()) {
            return false;
        }
        
        // Check if state is valid
        if (city.getState() == null || city.getState().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
}
