package com.busmanagement.controller;

import com.busmanagement.model.Bus;
import com.busmanagement.service.BusService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/buses")
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @GetMapping
    public String listBuses(Model model) {
        model.addAttribute("buses", busService.getAllBuses());
        model.addAttribute("title", "Bus List");
        return "bus/list";
    }

    @GetMapping("/form")
    public String busForm(Model model) {
        // Factory pattern implementation - creating a new bus instance
        model.addAttribute("bus", createNewBus());
        model.addAttribute("title", "Add Bus");
        return "bus/form";
    }

    // Factory method for creating bus instances
    private Bus createNewBus() {
        return new Bus();
    }

    @PostMapping("/save")
    public String saveBus(@ModelAttribute("bus") Bus bus, Model model) {
        busService.saveBus(bus);
        return "redirect:/buses";
    }

    @GetMapping("/edit/{id}")
    public String editBus(@PathVariable Long id, Model model) {
        Bus bus = busService.getBusById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bus ID: " + id));
        model.addAttribute("bus", bus);
        model.addAttribute("title", "Edit Bus");
        return "bus/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return "redirect:/buses";
    }

    // REST API endpoints for AJAX calls
    @GetMapping("/api/all")
    @ResponseBody
    public List<Bus> getAllBusesApi() {
        return busService.getAllBuses();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Bus> getBusApi(@PathVariable Long id) {
        return busService.getBusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}