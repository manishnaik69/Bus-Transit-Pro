package com.busmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for index pages
 */
@Controller
public class IndexController {

    /**
     * Show home page
     *
     * @return the home page view
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    /**
     * Show about page
     *
     * @return the about page view
     */
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    /**
     * Show contact page
     *
     * @return the contact page view
     */
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    /**
     * Show dashboard page
     *
     * @return the dashboard page view
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}