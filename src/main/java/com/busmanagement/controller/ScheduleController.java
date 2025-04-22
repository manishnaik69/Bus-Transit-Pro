package com.busmanagement.controller;

import com.busmanagement.model.Bus;
import com.busmanagement.model.Route;
import com.busmanagement.model.Schedule;
import com.busmanagement.service.BusService;
import com.busmanagement.service.RouteService;
import com.busmanagement.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final BusService busService;
    private final RouteService routeService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, BusService busService, RouteService routeService) {
        this.scheduleService = scheduleService;
        this.busService = busService;
        this.routeService = routeService;
    }

    @InitBinder
    public void initBinder(org.springframework.web.bind.WebDataBinder binder) {
        // Bus editor
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

        // Route editor
        binder.registerCustomEditor(Route.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                    return;
                }
                Long routeId = Long.parseLong(text);
                Route route = routeService.getRouteById(routeId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Route ID: " + text));
                setValue(route);
            }
        });
    }

    // List all schedules
    @GetMapping
    public String listSchedules(Model model) {
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        model.addAttribute("pageTitle", "Manage Schedules");
        return "schedule/list";
    }

    // Form to create a new schedule
    @GetMapping("/new")
    public String createScheduleForm(Model model) {
        Schedule schedule = new Schedule();
        schedule.setStatus("Scheduled");

        // Set default times
        LocalDateTime departure = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0);
        LocalDateTime arrival = departure.plusHours(2);
        schedule.setDepartureTime(departure);
        schedule.setArrivalTime(arrival);

        model.addAttribute("schedule", schedule);
        // Get buses that aren't scheduled for this time period
        model.addAttribute("buses", scheduleService.getAvailableBusesForTimeSlot(departure, arrival, null));
        model.addAttribute("routes", routeService.getAllRoutes());
        model.addAttribute("pageTitle", "Add New Schedule");
        model.addAttribute("isNew", true);
        return "schedule/form";
    }

    // Form to edit an existing schedule
    @GetMapping("/edit/{id}")
    public String editScheduleForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Schedule schedule = scheduleService.getScheduleById(id)
                .orElse(null);

        if (schedule == null) {
            redirectAttributes.addFlashAttribute("error", "Schedule not found with ID: " + id);
            return "redirect:/schedules";
        }

        model.addAttribute("schedule", schedule);
        // When editing, get available buses plus the currently assigned bus
        model.addAttribute("buses", scheduleService.getAvailableBusesForTimeSlot(
                schedule.getDepartureTime(),
                schedule.getArrivalTime(),
                schedule.getId()));
        model.addAttribute("routes", routeService.getAllRoutes());
        model.addAttribute("pageTitle", "Edit Schedule");
        model.addAttribute("isNew", false);
        return "schedule/form";
    }

    // Save or update schedule (Fixed version)
    @PostMapping("/save")
    public String saveSchedule(
            @ModelAttribute Schedule schedule,
            @RequestParam("departureTimeString") String departureTimeString,
            @RequestParam("arrivalTimeString") String arrivalTimeString,
            RedirectAttributes redirectAttributes) {
        try {
            // Convert string datetime to LocalDateTime (format: yyyy-MM-dd'T'HH:mm)
            if (departureTimeString != null && !departureTimeString.isEmpty()) {
                // Append seconds to match ISO format
                if (!departureTimeString.contains(":00")) {
                    departureTimeString += ":00";
                }
                schedule.setDepartureTime(LocalDateTime.parse(departureTimeString));
            }

            if (arrivalTimeString != null && !arrivalTimeString.isEmpty()) {
                // Append seconds to match ISO format
                if (!arrivalTimeString.contains(":00")) {
                    arrivalTimeString += ":00";
                }
                schedule.setArrivalTime(LocalDateTime.parse(arrivalTimeString));
            }

            if (schedule.getBus() != null) {
                schedule.setAvailableSeats(schedule.getBus().getCapacity());
            }

            Schedule savedSchedule = scheduleService.saveSchedule(schedule);
            redirectAttributes.addFlashAttribute("success", "Schedule saved successfully!");
            return "redirect:/schedules";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save schedule: " + e.getMessage());
            return schedule.getId() == null ? "redirect:/schedules/new"
                    : "redirect:/schedules/edit/" + schedule.getId();
        }
    }

    // Delete schedule
    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            scheduleService.deleteSchedule(id);
            redirectAttributes.addFlashAttribute("success", "Schedule deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete schedule: " + e.getMessage());
        }
        return "redirect:/schedules";
    }

    // View schedule details
    @GetMapping("/view/{id}")
    public String viewSchedule(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Schedule schedule = scheduleService.getScheduleById(id)
                .orElse(null);

        if (schedule == null) {
            redirectAttributes.addFlashAttribute("error", "Schedule not found with ID: " + id);
            return "redirect:/schedules";
        }

        model.addAttribute("schedule", schedule);
        model.addAttribute("pageTitle", "Schedule Details");
        return "schedule/view";
    }

    // REST API endpoints
    @GetMapping("/api/all")
    @ResponseBody
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Schedule> getSchedule(@PathVariable Long id) {
        return scheduleService.getScheduleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/available-buses")
    @ResponseBody
    public List<Bus> getAvailableBuses(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(name = "excludeId", required = false) Long excludeId) {

        return scheduleService.getAvailableBusesForTimeSlot(start, end, excludeId);
    }
}