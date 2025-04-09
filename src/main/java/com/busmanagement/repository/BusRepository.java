package com.busmanagement.repository;

import com.busmanagement.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Bus entity
 */
@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    /**
     * Find buses that need maintenance based on mileage and last maintenance date
     * @return List of buses that need maintenance
     */
    @Query("SELECT b FROM Bus b WHERE b.mileage > b.maintenanceThreshold OR " +
           "(b.lastMaintenanceDate IS NOT NULL AND DATEDIFF(CURRENT_DATE, b.lastMaintenanceDate) > 30)")
    List<Bus> findBusesNeedingMaintenance();
    
    /**
     * Find a bus by registration number
     * @param registrationNumber the registration number to search for
     * @return the bus if found
     */
    @Query("SELECT b FROM Bus b WHERE b.registrationNumber = :registrationNumber")
    Optional<Bus> findByRegistrationNumber(@Param("registrationNumber") String registrationNumber);
    
    /**
     * Find buses by status
     * @param status the status to search for
     * @return list of buses with the status
     */
    @Query("SELECT b FROM Bus b WHERE b.status = :status")
    List<Bus> findByStatus(@Param("status") String status);
    
    /**
     * Find buses by type
     * @param type the bus type
     * @return list of buses with the type
     */
    @Query("SELECT b FROM Bus b WHERE b.type = :type")
    List<Bus> findByType(@Param("type") Bus.BusType type);
    
    /**
     * Find available buses
     * @return list of available buses
     */
    @Query("SELECT b FROM Bus b WHERE b.status = 'Active'")
    List<Bus> findAvailableBuses();
    
    /**
     * Count buses by status
     * @param status the status to count
     * @return count of buses with the status
     */
    @Query("SELECT COUNT(b) FROM Bus b WHERE b.status = :status")
    long countByStatus(@Param("status") String status);
}