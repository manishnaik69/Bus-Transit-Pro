package com.busmanagement.controller;

import com.busmanagement.dto.PaymentDTO;
import com.busmanagement.model.Booking;
import com.busmanagement.model.Payment;
import com.busmanagement.model.User;
import com.busmanagement.service.BookingService;
import com.busmanagement.service.PaymentService;
import com.busmanagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for payment operations.
 * Handles payment processing for bookings.
 */
@Controller
@RequestMapping("/passenger")
@PreAuthorize("hasRole('PASSENGER')")
public class PaymentController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @GetMapping("/payment/{bookingId}")
    public String showPaymentForm(@PathVariable Long bookingId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User passenger = userService.findUserByUsername(auth.getName());
        
        Booking booking = bookingService.findBookingById(bookingId);
        
        // Security check - ensure the booking belongs to the logged-in passenger
        if (!booking.getUser().getId().equals(passenger.getId())) {
            return "redirect:/passenger/dashboard";
        }
        
        model.addAttribute("booking", booking);
        model.addAttribute("paymentDTO", new PaymentDTO());
        model.addAttribute("totalAmount", booking.getTotalAmount());
        
        return "passenger/payment";
    }

    @PostMapping("/payment/{bookingId}")
    public String processPayment(@PathVariable Long bookingId, 
                               @ModelAttribute PaymentDTO paymentDTO,
                               Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User passenger = userService.findUserByUsername(auth.getName());
        
        Booking booking = bookingService.findBookingById(bookingId);
        
        // Security check - ensure the booking belongs to the logged-in passenger
        if (!booking.getUser().getId().equals(passenger.getId())) {
            return "redirect:/passenger/dashboard";
        }
        
        try {
            paymentDTO.setBookingId(bookingId);
            paymentDTO.setAmount(booking.getTotalAmount());
            Payment payment = paymentService.processPayment(paymentDTO);
            
            return "redirect:/passenger/payment/success/" + payment.getId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Payment failed: " + e.getMessage());
            model.addAttribute("booking", booking);
            model.addAttribute("totalAmount", booking.getTotalAmount());
            
            return "passenger/payment";
        }
    }

    @GetMapping("/payment/success/{paymentId}")
    public String paymentSuccess(@PathVariable Long paymentId, Model model) {
        Payment payment = paymentService.findPaymentById(paymentId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User passenger = userService.findUserByUsername(auth.getName());
        
        // Security check - ensure the payment belongs to the logged-in passenger
        if (!payment.getBooking().getUser().getId().equals(passenger.getId())) {
            return "redirect:/passenger/dashboard";
        }
        
        model.addAttribute("payment", payment);
        model.addAttribute("booking", payment.getBooking());
        
        return "passenger/payment-success";
    }
}
