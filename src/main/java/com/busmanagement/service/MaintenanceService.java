package com.busmanagement.service;

import com.busmanagement.model.MaintenanceRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing maintenance operations.
 */
public interface MaintenanceService {
    
    /**
     * Finds a maintenance record by its ID.
     * 
     * @param id ID of the maintenance record
     * @return The maintenance record, or null if not found
     */
    MaintenanceRecord findMaintenanceRecordById(Long id);
    
    /**
     * Finds all maintenance records in the system.
     * 
     * @return List of all maintenance records
     */
    List<MaintenanceRecord> findAllMaintenanceRecords();
    
    /**
     * Finds all maintenance records for a specific bus.
     * 
     * @param busId ID of the bus
     * @return List of maintenance records
     */
    List<MaintenanceRecord> getMaintenanceHistoryForBus(Long busId);
    
    /**
     * Finds all maintenance records with a specific status.
     * 
     * @param status Status of the maintenance records
     * @return List of maintenance records
     */
    List<MaintenanceRecord> findMaintenanceRecordsByStatus(String status);
    
    /**
     * Finds all maintenance records scheduled for a specific date.
     * 
     * @param maintenanceDate Date of the scheduled maintenance
     * @return List of maintenance records
     */
    List<MaintenanceRecord> findMaintenanceRecordsByDate(LocalDate maintenanceDate);
    
    /**
     * Saves a new maintenance record.
     * 
     * @param maintenanceRecord The maintenance record to save
     * @return The saved maintenance record
     */
    MaintenanceRecord saveMaintenanceRecord(MaintenanceRecord maintenanceRecord);
    
    /**
     * Schedules maintenance for a bus.
     * 
     * @param maintenanceRecord The maintenance record to schedule
     * @return The saved maintenance record
     */
    MaintenanceRecord scheduleMaintenance(MaintenanceRecord maintenanceRecord);
    
    /**
     * Updates an existing maintenance record.
     * 
     * @param id ID of the maintenance record to update
     * @param maintenanceRecord The updated maintenance record data
     * @return The updated maintenance record
     */
    MaintenanceRecord updateMaintenanceRecord(Long id, MaintenanceRecord maintenanceRecord);
    
    /**
     * Updates the status of a maintenance record.
     * 
     * @param maintenanceId ID of the maintenance record
     * @param status New status
     * @return The updated maintenance record
     */
    MaintenanceRecord updateMaintenanceStatus(Long maintenanceId, String status);
    
    /**
     * Deletes a maintenance record.
     * 
     * @param maintenanceId ID of the maintenance record to delete
     */
    void deleteMaintenanceRecord(Long maintenanceId);
    
    /**
     * Gets the maintenance status for all buses.
     * 
     * @return Map of bus IDs to maintenance statuses
     */
    Map<Long, String> getMaintenanceStatusForAllBuses();
    
    /**
     * Finds the most recent maintenance issues.
     * 
     * @param limit Maximum number of issues to return
     * @return List of recent maintenance records
     */
    List<MaintenanceRecord> getRecentMaintenanceIssues(int limit);
    
    /**
     * Counts the number of buses that need maintenance.
     * 
     * @return Number of buses
     */
    long countBusesNeedingMaintenance();
    
    /**
     * Gets the total cost of maintenance within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Total maintenance cost
     */
    double getTotalMaintenanceCostBetweenDates(LocalDate startDate, LocalDate endDate);
}
