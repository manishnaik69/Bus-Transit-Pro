package com.busmanagement.repository;

import com.busmanagement.model.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for WorkOrder entities.
 * Provides methods to interact with the work_orders table in the database.
 */
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    
    /**
     * Finds all work orders for a specific bus.
     * 
     * @param busId ID of the bus
     * @return List of work orders
     */
    List<WorkOrder> findByBusId(Long busId);
    
    /**
     * Finds all work orders with a specific status.
     * 
     * @param status Status of the work orders
     * @return List of work orders
     */
    List<WorkOrder> findByStatus(String status);
    
    /**
     * Finds all work orders assigned to a specific person.
     * 
     * @param assignedTo Name of the person assigned
     * @return List of work orders
     */
    List<WorkOrder> findByAssignedTo(String assignedTo);
    
    /**
     * Finds all work orders with a specific priority.
     * 
     * @param priority Priority of the work orders
     * @return List of work orders
     */
    List<WorkOrder> findByPriority(String priority);
    
    /**
     * Finds all work orders for a specific bus with a specific status.
     * 
     * @param busId ID of the bus
     * @param status Status of the work orders
     * @return List of work orders
     */
    List<WorkOrder> findByBusIdAndStatus(Long busId, String status);
    
    /**
     * Finds all work orders with due date before a specific date.
     * 
     * @param dueDate Maximum due date
     * @return List of work orders
     */
    List<WorkOrder> findByDueDateBefore(LocalDate dueDate);
    
    /**
     * Finds all work orders issued within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return List of work orders
     */
    @Query("SELECT w FROM WorkOrder w WHERE w.issueDate BETWEEN :startDate AND :endDate")
    List<WorkOrder> findByIssueDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * Counts the number of work orders with a specific status.
     * 
     * @param status Status of the work orders
     * @return Number of work orders
     */
    long countByStatus(String status);
    
    /**
     * Gets the total cost of work orders within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Total cost
     */
    @Query("SELECT SUM(w.actualCost) FROM WorkOrder w WHERE w.status = 'Completed' AND w.completionDate BETWEEN :startDate AND :endDate")
    Double getTotalCostBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * Finds the bus with most work orders.
     * 
     * @return Bus ID and work order count
     */
    @Query("SELECT w.bus.id, COUNT(w) as orderCount FROM WorkOrder w GROUP BY w.bus.id ORDER BY orderCount DESC")
    List<Object[]> findBusWithMostWorkOrders();
}
