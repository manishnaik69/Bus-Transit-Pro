package com.busmanagement.repository;

import com.busmanagement.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}