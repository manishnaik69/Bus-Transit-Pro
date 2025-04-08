package com.busmanagement.service;

import com.busmanagement.dto.BusDTO;
import com.busmanagement.model.Bus;

import java.util.List;

/**
 * Service interface for managing bus operations.
 */
public interface BusService {
    
    /**
     * Finds a bus by its ID.
     * 
     * @param id ID of the bus
     * @return The bus, or null if not found
     */
    Bus findBusById(Long id);
    
    /**
     * Finds a bus by its registration number.
     * 
     * @param registrationNumber Registration number of the bus
     * @return The bus, or null if not found
     */
    Bus findBusByRegistrationNumber(String registrationNumber);
    
    /**
     * Finds all buses in the system.
     * 
     * @return List of all buses
     */
    List<Bus> findAllBuses();
    
    /**
     * Finds all buses with a specific status.
     * 
     * @param status Status of the buses
     * @return List of buses
     */
    List<Bus> findBusesByStatus(String status);
    
    /**
     * Finds all buses of a specific type.
     * 
     * @param type Type of the buses
     * @return List of buses
     */
    List<Bus> findBusesByType(String type);
    
    /**
     * Finds all buses that are available for scheduling.
     * 
     * @return List of available buses
     */
    List<Bus> findAvailableBuses();
    
    /**
     * Saves a new bus from a DTO.
     * 
     * @param busDTO The bus data to save
     * @return The saved bus
     */
    Bus saveBus(BusDTO busDTO);
    
    /**
     * Updates an existing bus from a DTO.
     * 
     * @param id ID of the bus to update
     * @param busDTO The updated bus data
     * @return The updated bus
     */
    Bus updateBus(Long id, BusDTO busDTO);
    
    /**
     * Updates the status of a bus.
     * 
     * @param busId ID of the bus
     * @param status New status
     * @return The updated bus
     */
    Bus updateBusStatus(Long busId, String status);
    
    /**
     * Deletes a bus.
     * 
     * @param busId ID of the bus to delete
     */
    void deleteBus(Long busId);
    
    /**
     * Validates a bus to ensure it meets all business rules.
     * 
     * @param bus The bus to validate
     * @return true if the bus is valid, false otherwise
     */
    boolean validateBus(Bus bus);
    
    /**
     * Finds all buses that need maintenance.
     * 
     * @return List of buses
     */
    List<Bus> findBusesNeedingMaintenance();
    
    /**
     * Counts the total number of buses.
     * 
     * @return Total number of buses
     */
    long countAllBuses();
    
    /**
     * Counts the number of buses with a specific status.
     * 
     * @param status Status of the buses
     * @return Number of buses
     */
    long countBusesByStatus(String status);
}
