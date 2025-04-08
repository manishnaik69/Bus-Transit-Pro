package com.busmanagement.controller;

import com.busmanagement.model.Bus;
import com.busmanagement.model.MaintenanceRecord;
import com.busmanagement.model.WorkOrder;
import com.busmanagement.service.BusService;
import com.busmanagement.service.MaintenanceService;
import com.busmanagement.service.WorkOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for maintenance staff operations.
 * Manages fleet maintenance and work orders.
 */
@Controller
@RequestMapping("/maintenance")
@PreAuthorize("hasRole('MAINTENANCE')")
public class MaintenanceController {

    @Autowired
    private BusService busService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private WorkOrderService workOrderService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalBuses", busService.countAllBuses());
        model.addAttribute("busesNeedingMaintenance", maintenanceService.countBusesNeedingMaintenance());
        model.addAttribute("pendingWorkOrders", workOrderService.countPendingWorkOrders());
        model.addAttribute("recentIssues", maintenanceService.getRecentMaintenanceIssues(5));
        return "maintenance/dashboard";
    }

    @GetMapping("/fleet")
    public String viewFleetStatus(Model model) {
        List<Bus> buses = busService.findAllBuses();
        model.addAttribute("buses", buses);
        model.addAttribute("maintenanceStatus", maintenanceService.getMaintenanceStatusForAllBuses());
        return "maintenance/fleet";
    }

    @GetMapping("/fleet/{id}")
    public String viewBusDetails(@PathVariable Long id, Model model) {
        Bus bus = busService.findBusById(id);
        List<MaintenanceRecord> maintenanceHistory = maintenanceService.getMaintenanceHistoryForBus(id);
        
        model.addAttribute("bus", bus);
        model.addAttribute("maintenanceHistory", maintenanceHistory);
        model.addAttribute("pendingWorkOrders", workOrderService.getPendingWorkOrdersForBus(id));
        
        return "maintenance/bus-details";
    }

    @GetMapping("/schedule-maintenance/{busId}")
    public String showScheduleMaintenanceForm(@PathVariable Long busId, Model model) {
        Bus bus = busService.findBusById(busId);
        model.addAttribute("bus", bus);
        model.addAttribute("maintenanceRecord", new MaintenanceRecord());
        return "maintenance/schedule-maintenance";
    }

    @PostMapping("/schedule-maintenance")
    public String scheduleMaintenance(@ModelAttribute MaintenanceRecord maintenanceRecord) {
        maintenanceService.scheduleMaintenance(maintenanceRecord);
        return "redirect:/maintenance/fleet";
    }

    @GetMapping("/work-orders")
    public String viewWorkOrders(Model model) {
        model.addAttribute("pendingOrders", workOrderService.getPendingWorkOrders());
        model.addAttribute("completedOrders", workOrderService.getCompletedWorkOrders());
        return "maintenance/work-orders";
    }

    @GetMapping("/work-orders/create")
    public String showCreateWorkOrderForm(Model model) {
        model.addAttribute("buses", busService.findAllBuses());
        model.addAttribute("workOrder", new WorkOrder());
        return "maintenance/create-work-order";
    }

    @PostMapping("/work-orders/create")
    public String createWorkOrder(@ModelAttribute WorkOrder workOrder) {
        workOrderService.createWorkOrder(workOrder);
        return "redirect:/maintenance/work-orders";
    }

    @GetMapping("/work-orders/{id}")
    public String viewWorkOrderDetails(@PathVariable Long id, Model model) {
        WorkOrder workOrder = workOrderService.findWorkOrderById(id);
        model.addAttribute("workOrder", workOrder);
        return "maintenance/work-order-details";
    }

    @PostMapping("/work-orders/{id}/update-status")
    public String updateWorkOrderStatus(@PathVariable Long id, @RequestParam String status) {
        workOrderService.updateWorkOrderStatus(id, status);
        return "redirect:/maintenance/work-orders";
    }

    @PostMapping("/work-orders/{id}/complete")
    public String completeWorkOrder(@PathVariable Long id, @RequestParam String remarks) {
        workOrderService.completeWorkOrder(id, remarks);
        return "redirect:/maintenance/work-orders";
    }
}
