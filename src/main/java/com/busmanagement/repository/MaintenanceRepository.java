package com.busmanagement.repository;

import com.busmanagement.model.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for MaintenanceRecord entities.
 * Provides methods to interact with the maintenance_records table in the database.
 */
@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord, Long> {
    
    /**
     * Finds all maintenance records for a specific bus.
     * 
     * @param busId ID of the bus
     * @return List of maintenance records
     */
    List<MaintenanceRecord> findByBusId(Long busId);
    
    /**
     * Finds all maintenance records with a specific status.
     * 
     * @param status Status of the maintenance records
     * @return List of maintenance records
     */
    List<MaintenanceRecord> findByStatus(String status);
    
    /**
     * Finds all maintenance records scheduled for a specific date.
     * 
     * @param maintenanceDate Date of the scheduled maintenance
     * @return List of maintenance records
     */
    List<MaintenanceRecord> findByMaintenanceDate(LocalDate maintenanceDate);
    
    /**
     * Finds all maintenance records of a specific type.
     * 
     * @param maintenanceType Type of maintenance
     * @return List of maintenance records
     */
    List<MaintenanceRecord> findByMaintenanceType(String maintenanceType);
    
    /**
     * Finds all maintenance records for a specific bus with a specific status.
     * 
     * @param busId ID of the bus
     * @param status Status of the maintenance records
     * @return List of maintenance records
     */
    List<MaintenanceRecord> findByBusIdAndStatus(Long busId, String status);
    
    /**
     * Finds the most recent maintenance record for each bus.
     * 
     * @return List of most recent maintenance records
     */
    @Query("SELECT m FROM MaintenanceRecord m WHERE m.createdAt = (SELECT MAX(m2.createdAt) FROM MaintenanceRecord m2 WHERE m2.bus = m.bus)")
    List<MaintenanceRecord> findMostRecentMaintenanceForAllBuses();
    
    /**
     * Finds the most recent maintenance record for a specific bus.
     * 
     * @param busId ID of the bus
     * @return Most recent maintenance record
     */
    @Query("SELECT m FROM MaintenanceRecord m WHERE m.bus.id = :busId ORDER BY m.createdAt DESC")
    List<MaintenanceRecord> findMostRecentMaintenanceForBus(@Param("busId") Long busId);
    
    /**
     * Counts the number of maintenance records with a specific status.
     * 
     * @param status Status of the maintenance records
     * @return Number of maintenance records
     */
    long countByStatus(String status);
    
    /**
     * Gets the total cost of maintenance within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Total maintenance cost
     */
    @Query("SELECT SUM(m.cost) FROM MaintenanceRecord m WHERE m.maintenanceDate BETWEEN :startDate AND :endDate")
    Double getTotalMaintenanceCostBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * Finds all maintenance records scheduled between two dates.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return List of maintenance records
     */
    @Query("SELECT m FROM MaintenanceRecord m WHERE m.maintenanceDate BETWEEN :startDate AND :endDate")
    List<MaintenanceRecord> findMaintenanceRecordsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
