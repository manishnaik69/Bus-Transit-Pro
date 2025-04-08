package com.busmanagement.service.impl;

import com.busmanagement.model.Bus;
import com.busmanagement.model.WorkOrder;
import com.busmanagement.repository.BusRepository;
import com.busmanagement.repository.WorkOrderRepository;
import com.busmanagement.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the WorkOrderService interface.
 * Provides business logic for work order operations.
 */
@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;
    
    @Autowired
    private BusRepository busRepository;

    @Override
    public WorkOrder findWorkOrderById(Long id) {
        Optional<WorkOrder> workOrder = workOrderRepository.findById(id);
        return workOrder.orElse(null);
    }

    @Override
    public List<WorkOrder> findAllWorkOrders() {
        return workOrderRepository.findAll();
    }

    @Override
    public List<WorkOrder> findWorkOrdersByBus(Long busId) {
        return workOrderRepository.findByBusId(busId);
    }

    @Override
    public List<WorkOrder> findWorkOrdersByStatus(String status) {
        return workOrderRepository.findByStatus(status);
    }

    @Override
    public List<WorkOrder> getPendingWorkOrders() {
        List<WorkOrder> pendingOrders = workOrderRepository.findByStatus("Pending");
        pendingOrders.addAll(workOrderRepository.findByStatus("In Progress"));
        return pendingOrders;
    }

    @Override
    public List<WorkOrder> getCompletedWorkOrders() {
        return workOrderRepository.findByStatus("Completed");
    }

    @Override
    public List<WorkOrder> getPendingWorkOrdersForBus(Long busId) {
        List<WorkOrder> pendingOrders = workOrderRepository.findByBusIdAndStatus(busId, "Pending");
        pendingOrders.addAll(workOrderRepository.findByBusIdAndStatus(busId, "In Progress"));
        return pendingOrders;
    }

    @Override
    @Transactional
    public WorkOrder createWorkOrder(WorkOrder workOrder) {
        // Validate work order
        if (!validateWorkOrder(workOrder)) {
            throw new IllegalArgumentException("Invalid work order details");
        }
        
        // Set default values if not provided
        if (workOrder.getIssueDate() == null) {
            workOrder.setIssueDate(LocalDate.now());
        }
        
        if (workOrder.getStatus() == null) {
            workOrder.setStatus("Pending");
        }
        
        // Save work order
        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);
        
        // Update bus status to Maintenance if necessary
        Bus bus = workOrder.getBus();
        if (!"Maintenance".equals(bus.getStatus())) {
            bus.setStatus("Maintenance");
            busRepository.save(bus);
        }
        
        return savedWorkOrder;
    }

    @Override
    @Transactional
    public WorkOrder updateWorkOrder(Long id, WorkOrder workOrder) {
        // Check if work order exists
        if (!workOrderRepository.existsById(id)) {
            throw new IllegalArgumentException("Work order not found with id: " + id);
        }
        
        // Set ID to ensure we're updating the existing record
        workOrder.setId(id);
        
        // Validate work order
        if (!validateWorkOrder(workOrder)) {
            throw new IllegalArgumentException("Invalid work order details");
        }
        
        return workOrderRepository.save(workOrder);
    }

    @Override
    @Transactional
    public WorkOrder updateWorkOrderStatus(Long workOrderId, String status) {
        WorkOrder workOrder = findWorkOrderById(workOrderId);
        if (workOrder == null) {
            throw new IllegalArgumentException("Work order not found with id: " + workOrderId);
        }
        
        workOrder.setStatus(status);
        
        // If status is Completed, set completion date
        if ("Completed".equals(status) && workOrder.getCompletionDate() == null) {
            workOrder.setCompletionDate(LocalDate.now());
        }
        
        return workOrderRepository.save(workOrder);
    }

    @Override
    @Transactional
    public WorkOrder completeWorkOrder(Long workOrderId, String remarks) {
        WorkOrder workOrder = findWorkOrderById(workOrderId);
        if (workOrder == null) {
            throw new IllegalArgumentException("Work order not found with id: " + workOrderId);
        }
        
        workOrder.setStatus("Completed");
        workOrder.setCompletionDate(LocalDate.now());
        workOrder.setRemarks(remarks);
        
        WorkOrder completedWorkOrder = workOrderRepository.save(workOrder);
        
        // Check if the bus has any pending work orders
        List<WorkOrder> pendingWorkOrders = getPendingWorkOrdersForBus(workOrder.getBus().getId());
        
        if (pendingWorkOrders.isEmpty()) {
            // Update bus status back to Active
            Bus bus = workOrder.getBus();
            bus.setStatus("Active");
            busRepository.save(bus);
        }
        
        return completedWorkOrder;
    }

    @Override
    @Transactional
    public WorkOrder cancelWorkOrder(Long workOrderId) {
        WorkOrder workOrder = findWorkOrderById(workOrderId);
        if (workOrder == null) {
            throw new IllegalArgumentException("Work order not found with id: " + workOrderId);
        }
        
        workOrder.setStatus("Cancelled");
        
        WorkOrder cancelledWorkOrder = workOrderRepository.save(workOrder);
        
        // Check if the bus has any pending work orders
        List<WorkOrder> pendingWorkOrders = getPendingWorkOrdersForBus(workOrder.getBus().getId());
        
        if (pendingWorkOrders.isEmpty()) {
            // Update bus status back to Active
            Bus bus = workOrder.getBus();
            bus.setStatus("Active");
            busRepository.save(bus);
        }
        
        return cancelledWorkOrder;
    }

    @Override
    @Transactional
    public void deleteWorkOrder(Long workOrderId) {
        if (!workOrderRepository.existsById(workOrderId)) {
            throw new IllegalArgumentException("Work order not found with id: " + workOrderId);
        }
        
        workOrderRepository.deleteById(workOrderId);
    }

    @Override
    public long countPendingWorkOrders() {
        return workOrderRepository.countByStatus("Pending") + workOrderRepository.countByStatus("In Progress");
    }

    @Override
    public double getTotalCostBetweenDates(LocalDate startDate, LocalDate endDate) {
        Double cost = workOrderRepository.getTotalCostBetweenDates(startDate, endDate);
        return cost != null ? cost : 0.0;
    }

    @Override
    public Map.Entry<Long, Long> getBusWithMostWorkOrders() {
        List<Object[]> result = workOrderRepository.findBusWithMostWorkOrders();
        if (result.isEmpty()) {
            return null;
        }
        
        Object[] row = result.get(0);
        Long busId = (Long) row[0];
        Long count = (Long) row[1];
        
        return Map.entry(busId, count);
    }
    
    /**
     * Validates a work order entity.
     */
    private boolean validateWorkOrder(WorkOrder workOrder) {
        // Check if bus is valid
        if (workOrder.getBus() == null || workOrder.getBus().getId() == null) {
            return false;
        }
        
        // Check if work type is valid
        if (workOrder.getWorkType() == null || workOrder.getWorkType().trim().isEmpty()) {
            return false;
        }
        
        // Check if description is valid
        if (workOrder.getDescription() == null || workOrder.getDescription().trim().isEmpty()) {
            return false;
        }
        
        // Check if priority is valid
        String priority = workOrder.getPriority();
        if (priority == null || (!priority.equals("High") && !priority.equals("Medium") && !priority.equals("Low"))) {
            return false;
        }
        
        // Check if issue date is valid
        if (workOrder.getIssueDate() == null) {
            return false;
        }
        
        // Check if due date is valid
        if (workOrder.getDueDate() != null && workOrder.getDueDate().isBefore(workOrder.getIssueDate())) {
            return false;
        }
        
        // Check if status is valid
        String status = workOrder.getStatus();
        if (status == null || (!status.equals("Pending") && !status.equals("In Progress") && 
                              !status.equals("Completed") && !status.equals("Cancelled"))) {
            return false;
        }
        
        // Check if estimated cost is valid
        if (workOrder.getEstimatedCost() != null && workOrder.getEstimatedCost() < 0) {
            return false;
        }
        
        // Check if actual cost is valid
        if (workOrder.getActualCost() != null && workOrder.getActualCost() < 0) {
            return false;
        }
        
        return true;
    }
}
