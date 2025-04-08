package com.busmanagement.service;

import com.busmanagement.model.WorkOrder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing work order operations.
 */
public interface WorkOrderService {
    
    /**
     * Finds a work order by its ID.
     * 
     * @param id ID of the work order
     * @return The work order, or null if not found
     */
    WorkOrder findWorkOrderById(Long id);
    
    /**
     * Finds all work orders in the system.
     * 
     * @return List of all work orders
     */
    List<WorkOrder> findAllWorkOrders();
    
    /**
     * Finds all work orders for a specific bus.
     * 
     * @param busId ID of the bus
     * @return List of work orders
     */
    List<WorkOrder> findWorkOrdersByBus(Long busId);
    
    /**
     * Finds all work orders with a specific status.
     * 
     * @param status Status of the work orders
     * @return List of work orders
     */
    List<WorkOrder> findWorkOrdersByStatus(String status);
    
    /**
     * Finds all pending work orders.
     * 
     * @return List of pending work orders
     */
    List<WorkOrder> getPendingWorkOrders();
    
    /**
     * Finds all completed work orders.
     * 
     * @return List of completed work orders
     */
    List<WorkOrder> getCompletedWorkOrders();
    
    /**
     * Finds all pending work orders for a specific bus.
     * 
     * @param busId ID of the bus
     * @return List of pending work orders
     */
    List<WorkOrder> getPendingWorkOrdersForBus(Long busId);
    
    /**
     * Creates a new work order.
     * 
     * @param workOrder The work order to create
     * @return The created work order
     */
    WorkOrder createWorkOrder(WorkOrder workOrder);
    
    /**
     * Updates an existing work order.
     * 
     * @param id ID of the work order to update
     * @param workOrder The updated work order data
     * @return The updated work order
     */
    WorkOrder updateWorkOrder(Long id, WorkOrder workOrder);
    
    /**
     * Updates the status of a work order.
     * 
     * @param workOrderId ID of the work order
     * @param status New status
     * @return The updated work order
     */
    WorkOrder updateWorkOrderStatus(Long workOrderId, String status);
    
    /**
     * Completes a work order.
     * 
     * @param workOrderId ID of the work order
     * @param remarks Remarks about the completion
     * @return The completed work order
     */
    WorkOrder completeWorkOrder(Long workOrderId, String remarks);
    
    /**
     * Cancels a work order.
     * 
     * @param workOrderId ID of the work order
     * @return The cancelled work order
     */
    WorkOrder cancelWorkOrder(Long workOrderId);
    
    /**
     * Deletes a work order.
     * 
     * @param workOrderId ID of the work order to delete
     */
    void deleteWorkOrder(Long workOrderId);
    
    /**
     * Counts the number of pending work orders.
     * 
     * @return Number of pending work orders
     */
    long countPendingWorkOrders();
    
    /**
     * Gets the total cost of work orders within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Total cost
     */
    double getTotalCostBetweenDates(LocalDate startDate, LocalDate endDate);
    
    /**
     * Finds the bus with the most work orders.
     * 
     * @return Map entry with bus ID and work order count
     */
    Map.Entry<Long, Long> getBusWithMostWorkOrders();
}
