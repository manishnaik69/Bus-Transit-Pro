package com.busmanagement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling authentication-related requests
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    /**
     * Display the login page
     * 
     * @return The login view
     */
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    /**
     * Handle logout success
     * 
     * @return Redirect to login page with logout parameter
     */
    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "redirect:/auth/login?logout";
    }
}