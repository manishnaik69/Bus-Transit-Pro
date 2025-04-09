package com.busmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for index pages
 */
@Controller
public class IndexController {

    /**
     * Show home page
     *
     * @param model the model to add attributes to
     * @return the home page view
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        return "index";
    }

    /**
     * Show about page
     *
     * @param model the model to add attributes to
     * @return the about page view
     */
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About Us");
        return "about";
    }

    /**
     * Show contact page
     *
     * @param model the model to add attributes to
     * @return the contact page view
     */
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Contact Us");
        return "contact";
    }

    /**
     * Show dashboard page
     *
     * @param model the model to add attributes to
     * @return the dashboard page view
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Dashboard");
        return "dashboard";
    }
}