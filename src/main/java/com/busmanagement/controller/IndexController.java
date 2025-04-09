package com.busmanagement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.busmanagement.security.UserDetailsImpl;

/**
 * Controller for the home page and other public pages
 */
@Controller
public class IndexController {

    /**
     * Display the home page
     */
    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    /**
     * Display the about page
     */
    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }

    /**
     * Display the contact page
     */
    @GetMapping("/contact")
    public String showContactPage() {
        return "contact";
    }

    /**
     * Display the dashboard
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        model.addAttribute("user", userDetails);
        
        return "dashboard";
    }

    /**
     * Display access denied page
     */
    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }
}