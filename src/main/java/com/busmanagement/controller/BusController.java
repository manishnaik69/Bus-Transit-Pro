package com.busmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for managing buses
 */
@Controller
@RequestMapping("/buses")
public class BusController {

    /**
     * Display all buses
     */
    @GetMapping
    public String showAllBuses(Model model) {
        // This is a placeholder method - we'll implement this fully later
        return "buses/list";
    }
}