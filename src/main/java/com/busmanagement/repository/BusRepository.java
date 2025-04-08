package com.busmanagement.repository;

import com.busmanagement.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Bus entities.
 * Provides methods to interact with the buses table in the database.
 */
@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    
    /**
     * Finds a bus by its registration number.
     * 
     * @param registrationNumber Registration number of the bus
     * @return Optional containing the bus, if found
     */
    Optional<Bus> findByRegistrationNumber(String registrationNumber);
    
    /**
     * Finds all buses with a specific status.
     * 
     * @param status Status of the buses
     * @return List of buses
     */
    List<Bus> findByStatus(String status);
    
    /**
     * Finds all buses by type.
     * 
     * @param type Type of the buses
     * @return List of buses
     */
    List<Bus> findByType(String type);
    
    /**
     * Finds all buses that need maintenance.
     * This includes buses with status "Maintenance" and those due for scheduled maintenance.
     * 
     * @return List of buses
     */
    @Query("SELECT b FROM Bus b WHERE b.status = 'Maintenance' OR EXISTS (SELECT m FROM MaintenanceRecord m WHERE m.bus = b AND m.status = 'Scheduled')")
    List<Bus> findBusesNeedingMaintenance();
    
    /**
     * Counts the number of buses with a specific status.
     * 
     * @param status Status of the buses
     * @return Number of buses
     */
    long countByStatus(String status);
    
    /**
     * Finds all active buses that are available for scheduling.
     * 
     * @return List of available buses
     */
    @Query("SELECT b FROM Bus b WHERE b.status = 'Active' AND NOT EXISTS (SELECT s FROM Schedule s WHERE s.bus = b AND s.departureTime < CURRENT_TIMESTAMP AND s.arrivalTime > CURRENT_TIMESTAMP)")
    List<Bus> findAvailableBuses();
}
