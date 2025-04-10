package com.busmanagement.controller;

import com.busmanagement.model.Bus;
import com.busmanagement.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for handling bus management requests
 */
@Controller
@RequestMapping("/buses")
public class BusController {
    
    private final BusService busService;
    
    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }
    
    /**
     * Display all buses
     * @param model The model for the view
     * @return The name of the view to render
     */
    @GetMapping
    public String getAllBuses(Model model) {
        List<Bus> buses = busService.findAllBuses();
        
        model.addAttribute("buses", buses);
        model.addAttribute("activeBusCount", busService.countBusesByStatus(Bus.BusStatus.ACTIVE));
        model.addAttribute("maintenanceBusCount", busService.countBusesByStatus(Bus.BusStatus.MAINTENANCE));
        model.addAttribute("pageTitle", "Manage Buses");
        
        return "bus/list";
    }
    
    /**
     * Display a form to add a new bus
     * @param model The model for the view
     * @return The name of the view to render
     */
    @GetMapping("/new")
    public String showNewBusForm(Model model) {
        model.addAttribute("bus", new Bus());
        model.addAttribute("busTypes", Bus.BusType.values());
        model.addAttribute("busStatuses", Bus.BusStatus.values());
        model.addAttribute("pageTitle", "Add New Bus");
        
        return "bus/form";
    }
    
    /**
     * Save a new bus
     * @param bus The bus to save
     * @param redirectAttributes Attributes to pass to the redirect view
     * @return The redirect URL
     */
    @PostMapping
    public String saveBus(@ModelAttribute Bus bus, RedirectAttributes redirectAttributes) {
        try {
            // Set defaults for new buses
            if (bus.getId() == null) {
                bus.setPurchaseDate(LocalDateTime.now());
            }
            
            busService.saveBus(bus);
            redirectAttributes.addFlashAttribute("successMessage", "Bus saved successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save bus: " + e.getMessage());
        }
        
        return "redirect:/buses";
    }
    
    /**
     * Display a form to edit a bus
     * @param id The bus ID
     * @param model The model for the view
     * @return The name of the view to render
     */
    @GetMapping("/edit/{id}")
    public String showEditBusForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Bus bus = busService.findBusById(id);
            
            if (bus == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bus not found");
                return "redirect:/buses";
            }
            
            model.addAttribute("bus", bus);
            model.addAttribute("busTypes", Bus.BusType.values());
            model.addAttribute("busStatuses", Bus.BusStatus.values());
            model.addAttribute("pageTitle", "Edit Bus");
            
            return "bus/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to retrieve bus: " + e.getMessage());
            return "redirect:/buses";
        }
    }
    
    /**
     * Update a bus
     * @param id The bus ID
     * @param busDetails The updated bus details
     * @param redirectAttributes Attributes to pass to the redirect view
     * @return The redirect URL
     */
    @PostMapping("/{id}")
    public String updateBus(@PathVariable Long id, @ModelAttribute Bus busDetails, 
                           RedirectAttributes redirectAttributes) {
        try {
            busService.updateBus(id, busDetails);
            redirectAttributes.addFlashAttribute("successMessage", "Bus updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update bus: " + e.getMessage());
        }
        
        return "redirect:/buses";
    }
    
    /**
     * Delete a bus
     * @param id The bus ID
     * @param redirectAttributes Attributes to pass to the redirect view
     * @return The redirect URL
     */
    @GetMapping("/delete/{id}")
    public String deleteBus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            busService.deleteBus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Bus deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete bus: " + e.getMessage());
        }
        
        return "redirect:/buses";
    }
    
    /**
     * Change the status of a bus to MAINTENANCE
     * @param id The bus ID
     * @param redirectAttributes Attributes to pass to the redirect view
     * @return The redirect URL
     */
    @GetMapping("/maintenance/{id}")
    public String setBusToMaintenance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            busService.changeBusStatus(id, Bus.BusStatus.MAINTENANCE);
            redirectAttributes.addFlashAttribute("successMessage", "Bus set to maintenance mode");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to change bus status: " + e.getMessage());
        }
        
        return "redirect:/buses";
    }
    
    /**
     * Change the status of a bus to ACTIVE
     * @param id The bus ID
     * @param redirectAttributes Attributes to pass to the redirect view
     * @return The redirect URL
     */
    @GetMapping("/activate/{id}")
    public String setBusToActive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            busService.changeBusStatus(id, Bus.BusStatus.ACTIVE);
            redirectAttributes.addFlashAttribute("successMessage", "Bus activated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to change bus status: " + e.getMessage());
        }
        
        return "redirect:/buses";
    }
}