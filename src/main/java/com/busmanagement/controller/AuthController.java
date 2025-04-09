package com.busmanagement.controller;

import com.busmanagement.model.ERole;
import com.busmanagement.model.Passenger;
import com.busmanagement.model.Role;
import com.busmanagement.model.User;
import com.busmanagement.repository.RoleRepository;
import com.busmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;

/**
 * Controller for authentication
 */
@Controller
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Display login page
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    /**
     * Display registration page
     */
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new Passenger());
        return "auth/register";
    }

    /**
     * Handle registration
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Passenger user, RedirectAttributes redirectAttributes) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username is already taken!");
            return "redirect:/register";
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email is already in use!");
            return "redirect:/register";
        }

        // Create new user's account
        User newUser = new Passenger(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword())
        );

        // Set additional passenger details
        ((Passenger) newUser).setGender(user.getGender());
        ((Passenger) newUser).setAge(user.getAge());
        ((Passenger) newUser).setPhoneNumber(user.getPhoneNumber());
        ((Passenger) newUser).setAddress(user.getAddress());
        ((Passenger) newUser).setCity(user.getCity());
        ((Passenger) newUser).setState(user.getState());
        ((Passenger) newUser).setPinCode(user.getPinCode());

        // Set roles (default to ROLE_PASSENGER for regular registration)
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_PASSENGER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        newUser.setRoles(roles);

        // Save user
        userRepository.save(newUser);

        redirectAttributes.addFlashAttribute("success", "Registration successful! You can now login.");
        return "redirect:/login";
    }
}