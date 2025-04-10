package com.busmanagement.service;

import com.busmanagement.model.Bus;
import java.util.List;

/**
 * Service interface for bus operations
 */
public interface BusService {
    
    /**
     * Find all buses
     * @return List of all buses
     */
    List<Bus> findAllBuses();
    
    /**
     * Find bus by ID
     * @param id The bus ID
     * @return The bus if found
     */
    Bus findBusById(Long id);
    
    /**
     * Find bus by registration number
     * @param registrationNumber The registration number
     * @return The bus if found
     */
    Bus findBusByRegistrationNumber(String registrationNumber);
    
    /**
     * Save a new bus
     * @param bus The bus to save
     * @return The saved bus
     */
    Bus saveBus(Bus bus);
    
    /**
     * Update an existing bus
     * @param id The bus ID
     * @param busDetails The updated bus details
     * @return The updated bus
     */
    Bus updateBus(Long id, Bus busDetails);
    
    /**
     * Delete a bus
     * @param id The bus ID
     */
    void deleteBus(Long id);
    
    /**
     * Find buses by status
     * @param status The bus status
     * @return List of buses with the given status
     */
    List<Bus> findBusesByStatus(Bus.BusStatus status);
    
    /**
     * Count buses by status
     * @param status The bus status
     * @return Count of buses with the given status
     */
    long countBusesByStatus(Bus.BusStatus status);
    
    /**
     * Find buses by bus type
     * @param busType The bus type
     * @return List of buses of the given type
     */
    List<Bus> findBusesByType(Bus.BusType busType);
    
    /**
     * Change the status of a bus
     * @param id The bus ID
     * @param status The new status
     * @return The updated bus
     */
    Bus changeBusStatus(Long id, Bus.BusStatus status);
}