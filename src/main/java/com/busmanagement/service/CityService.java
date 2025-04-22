package com.busmanagement.service;

import com.busmanagement.model.City;
import com.busmanagement.repository.InMemoryCityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final InMemoryCityRepository cityRepository;

    @Autowired
    public CityService(InMemoryCityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    // Get all cities
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    // Get city by ID
    public Optional<City> getCityById(Long id) {
        return cityRepository.findById(id);
    }

    // Save city
    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    // Delete city
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }

    // Find city by name
    public Optional<City> findByName(String name) {
        return cityRepository.findByName(name);
    }
}