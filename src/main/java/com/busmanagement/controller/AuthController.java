package com.busmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for authentication
 */
@Controller
public class AuthController {

    /**
     * Show login page
     *
     * @return the login page view
     */
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    /**
     * Show register page
     *
     * @return the register page view
     */
    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }
}