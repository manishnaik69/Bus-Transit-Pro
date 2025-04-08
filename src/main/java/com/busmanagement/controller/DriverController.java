package com.busmanagement.controller;

import com.busmanagement.model.Schedule;
import com.busmanagement.model.User;
import com.busmanagement.service.ScheduleService;
import com.busmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for driver-related operations.
 * Manages route assignments and status updates.
 */
@Controller
@RequestMapping("/driver")
@PreAuthorize("hasRole('DRIVER')")
public class DriverController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User driver = userService.findUserByUsername(auth.getName());
        
        List<Schedule> assignedRoutes = scheduleService.findSchedulesByDriverId(driver.getId());
        model.addAttribute("assignedRoutes", assignedRoutes);
        model.addAttribute("currentRoute", scheduleService.findCurrentScheduleForDriver(driver.getId()));
        
        return "driver/dashboard";
    }

    @GetMapping("/routes")
    public String viewRoutes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User driver = userService.findUserByUsername(auth.getName());
        
        List<Schedule> allAssignedRoutes = scheduleService.findAllSchedulesByDriverId(driver.getId());
        model.addAttribute("routes", allAssignedRoutes);
        
        return "driver/routes";
    }

    @PostMapping("/update-location")
    @ResponseBody
    public String updateBusLocation(@RequestParam Long scheduleId, 
                                   @RequestParam Double latitude, 
                                   @RequestParam Double longitude) {
        try {
            scheduleService.updateBusLocation(scheduleId, latitude, longitude);
            return "Location updated successfully";
        } catch (Exception e) {
            return "Error updating location: " + e.getMessage();
        }
    }

    @PostMapping("/update-status")
    public String updateRouteStatus(@RequestParam Long scheduleId, 
                                  @RequestParam String status) {
        scheduleService.updateScheduleStatus(scheduleId, status);
        return "redirect:/driver/routes";
    }

    @GetMapping("/report-issue")
    public String showReportIssueForm(@RequestParam Long scheduleId, Model model) {
        model.addAttribute("scheduleId", scheduleId);
        return "driver/report-issue";
    }

    @PostMapping("/report-issue")
    public String reportIssue(@RequestParam Long scheduleId, 
                             @RequestParam String issueType,
                             @RequestParam String description) {
        scheduleService.reportIssue(scheduleId, issueType, description);
        return "redirect:/driver/routes";
    }
}
