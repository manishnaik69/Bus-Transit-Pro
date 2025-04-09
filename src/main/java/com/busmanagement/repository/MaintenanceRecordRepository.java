package com.busmanagement.repository;

import com.busmanagement.model.Bus;
import com.busmanagement.model.MaintenanceRecord;
import com.busmanagement.model.MaintenanceStaff;
import com.busmanagement.model.MaintenanceStatus;
import com.busmanagement.model.MaintenanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for MaintenanceRecord entity
 */
@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    
    /**
     * Find maintenance records by bus
     * 
     * @param bus Bus to search for
     * @return List of maintenance records for the specified bus
     */
    List<MaintenanceRecord> findByBus(Bus bus);
    
    /**
     * Find maintenance records by assigned staff
     * 
     * @param staff Staff to search for
     * @return List of maintenance records assigned to the specified staff
     */
    List<MaintenanceRecord> findByAssignedStaff(MaintenanceStaff staff);
    
    /**
     * Find maintenance records by type
     * 
     * @param type Maintenance type to search for
     * @return List of maintenance records with the specified type
     */
    List<MaintenanceRecord> findByType(MaintenanceType type);
    
    /**
     * Find maintenance records by status
     * 
     * @param status Maintenance status to search for
     * @return List of maintenance records with the specified status
     */
    List<MaintenanceRecord> findByStatus(MaintenanceStatus status);
    
    /**
     * Find maintenance records by maintenance date
     * 
     * @param startDate Start of maintenance date range
     * @param endDate End of maintenance date range
     * @return List of maintenance records with maintenance date within the specified range
     */
    List<MaintenanceRecord> findByMaintenanceDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find maintenance records by next due date
     * 
     * @param fromDate Start of next due date range
     * @param toDate End of next due date range
     * @return List of maintenance records with next due date within the specified range
     */
    List<MaintenanceRecord> findByNextDueDateBetween(LocalDate fromDate, LocalDate toDate);
    
    /**
     * Find maintenance records with next due date before or equal to the specified date
     * 
     * @param date Date to compare with next due date
     * @return List of maintenance records with next due date before or equal to the specified date
     */
    List<MaintenanceRecord> findByNextDueDateLessThanEqual(LocalDate date);
    
    /**
     * Find recurring maintenance records
     * 
     * @return List of recurring maintenance records
     */
    List<MaintenanceRecord> findByIsRecurringTrue();
    
    /**
     * Find maintenance records by bus and status
     * 
     * @param bus Bus to search for
     * @param status Maintenance status to search for
     * @return List of maintenance records for the specified bus with the specified status
     */
    List<MaintenanceRecord> findByBusAndStatus(Bus bus, MaintenanceStatus status);
}