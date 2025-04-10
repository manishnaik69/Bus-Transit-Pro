package com.busmanagement.controller;

import com.busmanagement.model.SimpleBus;
import com.busmanagement.service.SimpleBusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/buses")
public class SimpleBusController {
    
    private final SimpleBusService busService;
    
    @Autowired
    public SimpleBusController(SimpleBusService busService) {
        this.busService = busService;
    }
    
    @GetMapping
    public String listBuses(Model model) {
        model.addAttribute("buses", busService.getAllBuses());
        return "bus/list";
    }
    
    @GetMapping("/form")
    public String busForm(Model model) {
        model.addAttribute("bus", new SimpleBus());
        return "bus/form";
    }
    
    @PostMapping("/save")
    public String saveBus(@Valid @ModelAttribute("bus") SimpleBus bus, 
                        BindingResult result, 
                        Model model) {
        if (result.hasErrors()) {
            return "bus/form";
        }
        
        busService.saveBus(bus);
        return "redirect:/buses";
    }
    
    @GetMapping("/edit/{id}")
    public String editBus(@PathVariable Long id, Model model) {
        SimpleBus bus = busService.getBusById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bus ID: " + id));
        model.addAttribute("bus", bus);
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
    public List<SimpleBus> getAllBusesApi() {
        return busService.getAllBuses();
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<SimpleBus> getBusApi(@PathVariable Long id) {
        return busService.getBusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}