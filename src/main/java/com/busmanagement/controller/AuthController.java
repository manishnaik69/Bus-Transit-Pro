package com.busmanagement.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Controller for authentication
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Show login page
     *
     * @param model the model to add attributes to
     * @return the login page view
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login");
        return "auth/login";
    }

    /**
     * Show register page
     *
     * @param model the model to add attributes to
     * @return the register page view
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("user", new RegistrationDTO());
        return "auth/register";
    }
    
    /**
     * Process registration form
     *
     * @param registrationDTO the registration data to process
     * @param result the binding result
     * @param redirectAttributes redirect attributes
     * @return redirect to login page on success, register page on error
     */
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("user") RegistrationDTO registrationDTO, 
                                      BindingResult result, 
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Register");
            return "auth/register";
        }
        
        try {
            userService.registerNewUser(registrationDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("title", "Register");
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }
}