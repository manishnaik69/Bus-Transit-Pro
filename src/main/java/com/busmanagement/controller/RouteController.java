package com.busmanagement.controller;

import com.busmanagement.model.City;
import com.busmanagement.model.Route;
import com.busmanagement.service.CityService;
import com.busmanagement.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.InitBinder;
import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;
    private final CityService cityService;

    @Autowired
    public RouteController(RouteService routeService, CityService cityService) {
        this.routeService = routeService;
        this.cityService = cityService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(City.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                    return;
                }
                Long cityId = Long.parseLong(text);
                City city = cityService.getCityById(cityId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid City ID: " + text));
                setValue(city);
            }
        });
    }

    // Show all routes
    @GetMapping
    public String listRoutes(Model model) {
        model.addAttribute("routes", routeService.getAllRoutes());
        model.addAttribute("pageTitle", "Manage Routes");
        return "route/list";
    }

    // Form to create a new route
    @GetMapping("/new")
    public String createRouteForm(Model model) {
        model.addAttribute("route", new Route());
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("pageTitle", "Add New Route");
        model.addAttribute("isNew", true);
        return "route/form";
    }

    // Form to edit an existing route
    @GetMapping("/edit/{id}")
    public String editRouteForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Route route = routeService.getRouteById(id)
                .orElse(null);

        if (route == null) {
            redirectAttributes.addFlashAttribute("error", "Route not found with ID: " + id);
            return "redirect:/routes";
        }

        model.addAttribute("route", route);
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("pageTitle", "Edit Route");
        model.addAttribute("isNew", false);
        return "route/form";
    }

    // Save or update route
    @PostMapping("/save")
    public String saveRoute(@ModelAttribute Route route, RedirectAttributes redirectAttributes) {
        try {
            if (route.getId() == null) {
                route.setCreatedAt(LocalDateTime.now());
            }
            route.setUpdatedAt(LocalDateTime.now());

            Route savedRoute = routeService.saveRoute(route);
            redirectAttributes.addFlashAttribute("success", "Route saved successfully!");
            return "redirect:/routes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save route: " + e.getMessage());
            return "redirect:/routes/new";
        }
    }

    // Delete route
    @GetMapping("/delete/{id}")
    public String deleteRoute(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            routeService.deleteRoute(id);
            redirectAttributes.addFlashAttribute("success", "Route deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete route: " + e.getMessage());
        }
        return "redirect:/routes";
    }

    // View route details
    @GetMapping("/view/{id}")
    public String viewRoute(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Route route = routeService.getRouteById(id)
                .orElse(null);

        if (route == null) {
            redirectAttributes.addFlashAttribute("error", "Route not found with ID: " + id);
            return "redirect:/routes";
        }

        model.addAttribute("route", route);
        model.addAttribute("pageTitle", "Route Details");
        return "route/view";
    }

    // REST API Methods
    @GetMapping("/api/all")
    @ResponseBody
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Route> getRoute(@PathVariable Long id) {
        return routeService.getRouteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/popular")
    @ResponseBody
    public ResponseEntity<List<Route>> getPopularRoutes(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(routeService.getPopularRoutes(limit));
    }
}