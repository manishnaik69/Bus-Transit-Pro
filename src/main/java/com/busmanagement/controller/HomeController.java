package com.busmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling home page requests
 */
@Controller
public class HomeController {
    
    /**
     * Display the home page
     * @param model The model for the view
     * @return The name of the view to render
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Home - Bus Management System");
        model.addAttribute("welcomeMessage", "Welcome to the Bus Management System");
        return "home";
    }
    
    /**
     * Display the about page
     * @param model The model for the view
     * @return The name of the view to render
     */
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "About - Bus Management System");
        return "about";
    }
}