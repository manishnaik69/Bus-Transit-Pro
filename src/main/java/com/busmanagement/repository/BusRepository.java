package com.busmanagement.repository;

import com.busmanagement.model.Bus;
import com.busmanagement.model.BusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Bus entity
 */
@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    
    /**
     * Find a bus by registration number
     * 
     * @param registrationNumber Registration number to search for
     * @return Optional containing the bus if found
     */
    Optional<Bus> findByRegistrationNumber(String registrationNumber);
    
    /**
     * Find buses by type
     * 
     * @param type Bus type to search for
     * @return List of buses of the specified type
     */
    List<Bus> findByType(BusType type);
    
    /**
     * Find active buses
     * 
     * @return List of active buses
     */
    List<Bus> findByActiveTrue();
    
    /**
     * Find buses with tracking enabled
     * 
     * @return List of buses with tracking enabled
     */
    List<Bus> findByTrackingEnabledTrue();
    
    /**
     * Find buses with maintenance due
     * 
     * @param today Today's date
     * @return List of buses with maintenance due
     */
    List<Bus> findByNextMaintenanceDateLessThanEqual(LocalDate today);
    
    /**
     * Find available buses (not assigned to any trips on a given date)
     * 
     * @return List of available buses
     */
    @Query("SELECT b FROM Bus b WHERE b.active = true AND b.id NOT IN " +
           "(SELECT t.bus.id FROM Trip t WHERE t.scheduledDepartureTime <= CURRENT_TIMESTAMP AND " +
           "t.scheduledArrivalTime >= CURRENT_TIMESTAMP AND t.status <> 'COMPLETED' AND t.status <> 'CANCELLED')")
    List<Bus> findAvailableBuses();
}