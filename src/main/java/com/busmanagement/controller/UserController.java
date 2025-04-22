package com.busmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Welcome - User Dashboard");
        return "user/home";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("title", "My Profile");
        // Add user details to the model if needed
        return "user/profile"; // You can create this profile page later
    }
}