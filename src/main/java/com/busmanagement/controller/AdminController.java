package com.busmanagement.controller;

import com.busmanagement.dto.BusDTO;
import com.busmanagement.dto.RouteDTO;
import com.busmanagement.model.Bus;
import com.busmanagement.model.Route;
import com.busmanagement.model.Schedule;
import com.busmanagement.model.User;
import com.busmanagement.service.BusService;
import com.busmanagement.service.ReportService;
import com.busmanagement.service.RouteService;
import com.busmanagement.service.ScheduleService;
import com.busmanagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for administrative functions of the Bus Management System.
 * Handles requests for user management, bus fleet management, routes and schedule management.
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BusService busService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalBuses", busService.countAllBuses());
        model.addAttribute("totalRoutes", routeService.countAllRoutes());
        model.addAttribute("totalUsers", userService.countAllUsers());
        model.addAttribute("totalBookings", reportService.countTotalBookings());
        model.addAttribute("recentBookings", reportService.getRecentBookings(5));
        return "admin/dashboard";
    }

    // User Management
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.findAllRoles());
        return "admin/users-add";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.findAllRoles());
        return "admin/users-edit";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // Bus Management
    @GetMapping("/buses")
    public String listBuses(Model model) {
        List<Bus> buses = busService.findAllBuses();
        model.addAttribute("buses", buses);
        return "admin/buses";
    }

    @GetMapping("/buses/add")
    public String showAddBusForm(Model model) {
        model.addAttribute("bus", new BusDTO());
        return "admin/buses-add";
    }

    @PostMapping("/buses/add")
    public String addBus(@ModelAttribute BusDTO busDTO) {
        busService.saveBus(busDTO);
        return "redirect:/admin/buses";
    }

    @GetMapping("/buses/edit/{id}")
    public String showEditBusForm(@PathVariable Long id, Model model) {
        Bus bus = busService.findBusById(id);
        model.addAttribute("bus", bus);
        return "admin/buses-edit";
    }

    @PostMapping("/buses/edit/{id}")
    public String updateBus(@PathVariable Long id, @ModelAttribute BusDTO busDTO) {
        busService.updateBus(id, busDTO);
        return "redirect:/admin/buses";
    }

    @GetMapping("/buses/delete/{id}")
    public String deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return "redirect:/admin/buses";
    }

    // Route Management
    @GetMapping("/routes")
    public String listRoutes(Model model) {
        List<Route> routes = routeService.findAllRoutes();
        model.addAttribute("routes", routes);
        return "admin/routes";
    }

    @GetMapping("/routes/add")
    public String showAddRouteForm(Model model) {
        model.addAttribute("route", new RouteDTO());
        model.addAttribute("cities", routeService.findAllCities());
        return "admin/routes-add";
    }

    @PostMapping("/routes/add")
    public String addRoute(@ModelAttribute RouteDTO routeDTO) {
        routeService.saveRoute(routeDTO);
        return "redirect:/admin/routes";
    }

    @GetMapping("/routes/edit/{id}")
    public String showEditRouteForm(@PathVariable Long id, Model model) {
        Route route = routeService.findRouteById(id);
        model.addAttribute("route", route);
        model.addAttribute("cities", routeService.findAllCities());
        return "admin/routes-edit";
    }

    @PostMapping("/routes/edit/{id}")
    public String updateRoute(@PathVariable Long id, @ModelAttribute RouteDTO routeDTO) {
        routeService.updateRoute(id, routeDTO);
        return "redirect:/admin/routes";
    }

    @GetMapping("/routes/delete/{id}")
    public String deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return "redirect:/admin/routes";
    }

    // Schedule Management
    @GetMapping("/schedules")
    public String listSchedules(Model model) {
        List<Schedule> schedules = scheduleService.findAllSchedules();
        model.addAttribute("schedules", schedules);
        model.addAttribute("buses", busService.findAllBuses());
        model.addAttribute("routes", routeService.findAllRoutes());
        return "admin/schedules";
    }

    @GetMapping("/schedules/add")
    public String showAddScheduleForm(Model model) {
        model.addAttribute("schedule", new Schedule());
        model.addAttribute("buses", busService.findAllBuses());
        model.addAttribute("routes", routeService.findAllRoutes());
        return "admin/schedules-add";
    }

    @PostMapping("/schedules/add")
    public String addSchedule(@ModelAttribute Schedule schedule) {
        scheduleService.saveSchedule(schedule);
        return "redirect:/admin/schedules";
    }

    @GetMapping("/schedules/edit/{id}")
    public String showEditScheduleForm(@PathVariable Long id, Model model) {
        Schedule schedule = scheduleService.findScheduleById(id);
        model.addAttribute("schedule", schedule);
        model.addAttribute("buses", busService.findAllBuses());
        model.addAttribute("routes", routeService.findAllRoutes());
        return "admin/schedules-edit";
    }

    @PostMapping("/schedules/edit/{id}")
    public String updateSchedule(@PathVariable Long id, @ModelAttribute Schedule schedule) {
        scheduleService.updateSchedule(id, schedule);
        return "redirect:/admin/schedules";
    }

    @GetMapping("/schedules/delete/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return "redirect:/admin/schedules";
    }

    // Reports
    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("dailyRevenue", reportService.getDailyRevenue());
        model.addAttribute("weeklyRevenue", reportService.getWeeklyRevenue());
        model.addAttribute("monthlyRevenue", reportService.getMonthlyRevenue());
        model.addAttribute("popularRoutes", reportService.getPopularRoutes());
        model.addAttribute("busOccupancyRates", reportService.getBusOccupancyRates());
        return "admin/reports";
    }
}
