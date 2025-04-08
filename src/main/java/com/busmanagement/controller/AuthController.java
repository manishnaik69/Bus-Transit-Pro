package com.busmanagement.controller;

import com.busmanagement.dto.LoginDTO;
import com.busmanagement.dto.RegistrationDTO;
import com.busmanagement.model.User;
import com.busmanagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Controller for authentication related operations.
 * Handles login and registration requests.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationDTO") RegistrationDTO registrationDTO, 
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        if (userService.isUsernameTaken(registrationDTO.getUsername())) {
            model.addAttribute("usernameError", "Username is already taken");
            return "auth/register";
        }

        if (userService.isEmailTaken(registrationDTO.getEmail())) {
            model.addAttribute("emailError", "Email is already registered");
            return "auth/register";
        }

        try {
            User user = userService.registerNewUser(registrationDTO);
            return "redirect:/auth/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        // Spring Security handles the logout functionality
        return "redirect:/auth/login?logout=true";
    }
}
