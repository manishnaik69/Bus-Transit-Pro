package com.busmanagement.controller;

import com.busmanagement.model.Bus;
import com.busmanagement.model.Maintenance;
import com.busmanagement.service.BusService;
import com.busmanagement.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final BusService busService;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService, BusService busService) {
        this.maintenanceService = maintenanceService;
        this.busService = busService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Register custom editor for Bus objects
        binder.registerCustomEditor(Bus.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                    return;
                }
                Long busId = Long.parseLong(text);
                Bus bus = busService.getBusById(busId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Bus ID: " + text));
                setValue(bus);
            }
        });
    }

    // Dashboard page showing an overview
    @GetMapping
    public String maintenanceDashboard(Model model) {
        model.addAttribute("scheduled", maintenanceService.getScheduledMaintenance());
        model.addAttribute("inProgress", maintenanceService.getInProgressMaintenance());
        model.addAttribute("upcoming", maintenanceService.getUpcomingMaintenance());
        model.addAttribute("overdue", maintenanceService.getOverdueMaintenance());
        model.addAttribute("title", "Maintenance Dashboard");
        return "maintenance/dashboard";
    }

    // List all maintenance records
    @GetMapping("/list")
    public String listMaintenanceRecords(Model model) {
        model.addAttribute("maintenanceRecords", maintenanceService.getAllMaintenanceRecords());
        model.addAttribute("title", "Maintenance Records");
        return "maintenance/list";
    }

    // Form to create a new maintenance record
    @GetMapping("/schedule")
    public String scheduleMaintenance(Model model) {
        Maintenance maintenance = new Maintenance();
        maintenance.setScheduledDate(LocalDateTime.now().plusDays(1));

        model.addAttribute("maintenance", maintenance);
        model.addAttribute("buses", busService.getAllBuses());
        model.addAttribute("title", "Schedule Maintenance");
        model.addAttribute("isNew", true);

        return "maintenance/form";
    }

    // Form to edit an existing maintenance record
    @GetMapping("/edit/{id}")
    public String editMaintenance(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Maintenance maintenance = maintenanceService.getMaintenanceById(id)
                .orElse(null);

        if (maintenance == null) {
            redirectAttributes.addFlashAttribute("error", "Maintenance record not found with ID: " + id);
            return "redirect:/maintenance";
        }

        model.addAttribute("maintenance", maintenance);
        model.addAttribute("buses", busService.getAllBuses());
        model.addAttribute("title", "Edit Maintenance");
        model.addAttribute("isNew", false);

        return "maintenance/form";
    }

    // Save or update maintenance record
    @PostMapping("/save")
    public String saveMaintenance(
            @ModelAttribute Maintenance maintenance,
            @RequestParam("scheduledDateString") String scheduledDateString,
            RedirectAttributes redirectAttributes) {

        try {
            // Parse scheduled date
            if (scheduledDateString != null && !scheduledDateString.isEmpty()) {
                // Append seconds to match ISO format if needed
                if (!scheduledDateString.contains(":00")) {
                    scheduledDateString += ":00";
                }
                maintenance.setScheduledDate(LocalDateTime.parse(scheduledDateString));
            }

            // Save the maintenance record
            if (maintenance.getId() == null) {
                maintenanceService.scheduleMaintenance(maintenance);
                redirectAttributes.addFlashAttribute("success", "Maintenance scheduled successfully!");
            } else {
                maintenanceService.updateMaintenance(maintenance);
                redirectAttributes.addFlashAttribute("success", "Maintenance record updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/maintenance";
    }

    // View maintenance details
    @GetMapping("/view/{id}")
    public String viewMaintenance(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Maintenance maintenance = maintenanceService.getMaintenanceById(id)
                .orElse(null);

        if (maintenance == null) {
            redirectAttributes.addFlashAttribute("error", "Maintenance record not found with ID: " + id);
            return "redirect:/maintenance";
        }

        model.addAttribute("maintenance", maintenance);
        model.addAttribute("title", "Maintenance Details");

        return "maintenance/view";
    }

    // Delete maintenance record
    @GetMapping("/delete/{id}")
    public String deleteMaintenance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            maintenanceService.deleteMaintenance(id);
            redirectAttributes.addFlashAttribute("success", "Maintenance record deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete maintenance record: " + e.getMessage());
        }

        return "redirect:/maintenance";
    }

    // Start maintenance
    @GetMapping("/start/{id}")
    public String startMaintenance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            maintenanceService.startMaintenance(id);
            redirectAttributes.addFlashAttribute("success", "Maintenance started successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to start maintenance: " + e.getMessage());
        }

        return "redirect:/maintenance";
    }

    // Complete maintenance
    @GetMapping("/complete/{id}")
    public String completeMaintenance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            maintenanceService.completeMaintenance(id);
            redirectAttributes.addFlashAttribute("success", "Maintenance completed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to complete maintenance: " + e.getMessage());
        }

        return "redirect:/maintenance";
    }

    // Cancel maintenance
    @GetMapping("/cancel/{id}")
    public String cancelMaintenance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            maintenanceService.cancelMaintenance(id);
            redirectAttributes.addFlashAttribute("success", "Maintenance cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to cancel maintenance: " + e.getMessage());
        }

        return "redirect:/maintenance";
    }

    // View maintenance history for a specific bus
    @GetMapping("/bus/{busId}")
    public String busMaintenanceHistory(@PathVariable Long busId, Model model, RedirectAttributes redirectAttributes) {
        Bus bus = busService.getBusById(busId)
                .orElse(null);

        if (bus == null) {
            redirectAttributes.addFlashAttribute("error", "Bus not found with ID: " + busId);
            return "redirect:/buses";
        }

        List<Maintenance> maintenanceHistory = maintenanceService.getMaintenanceByBusId(busId);

        model.addAttribute("bus", bus);
        model.addAttribute("maintenanceHistory", maintenanceHistory);
        model.addAttribute("title", "Maintenance History for " + bus.getRegistrationNumber());

        return "maintenance/history";
    }

    // REST API endpoints

    @GetMapping("/api/all")
    @ResponseBody
    public List<Maintenance> getAllMaintenanceRecordsApi() {
        return maintenanceService.getAllMaintenanceRecords();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Maintenance> getMaintenanceApi(@PathVariable Long id) {
        return maintenanceService.getMaintenanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}