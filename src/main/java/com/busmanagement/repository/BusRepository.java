package com.busmanagement.repository;

import com.busmanagement.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Bus entity
 */
@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    
    /**
     * Find a bus by its registration number
     * @param registrationNumber The registration number
     * @return The bus if found
     */
    Bus findByRegistrationNumber(String registrationNumber);
    
    /**
     * Find buses by their status
     * @param status The status to search for
     * @return List of buses with the given status
     */
    List<Bus> findByStatus(Bus.BusStatus status);
    
    /**
     * Count buses by their status
     * @param status The status to count
     * @return The count of buses with the given status
     */
    long countByStatus(Bus.BusStatus status);
    
    /**
     * Find buses by manufacturer
     * @param manufacturer The manufacturer to search for
     * @return List of buses from the given manufacturer
     */
    List<Bus> findByManufacturer(String manufacturer);
    
    /**
     * Find buses by bus type
     * @param busType The bus type to search for
     * @return List of buses of the given type
     */
    List<Bus> findByBusType(Bus.BusType busType);
    
    /**
     * Find buses by capacity greater than or equal to the given value
     * @param capacity The minimum capacity
     * @return List of buses with capacity >= the given value
     */
    List<Bus> findByCapacityGreaterThanEqual(int capacity);
}