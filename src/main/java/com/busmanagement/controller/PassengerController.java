package com.busmanagement.controller;

import com.busmanagement.dto.BookingDTO;
import com.busmanagement.facade.BookingFacade;
import com.busmanagement.model.Booking;
import com.busmanagement.model.Route;
import com.busmanagement.model.Schedule;
import com.busmanagement.model.User;
import com.busmanagement.service.BookingService;
import com.busmanagement.service.RouteService;
import com.busmanagement.service.ScheduleService;
import com.busmanagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for passenger operations.
 * Handles bus search, ticket booking, and booking history.
 */
@Controller
@RequestMapping("/passenger")
@PreAuthorize("hasRole('PASSENGER')")
public class PassengerController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingFacade bookingFacade;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User passenger = userService.findUserByUsername(auth.getName());
        
        List<Booking> upcomingBookings = bookingService.findUpcomingBookingsByUserId(passenger.getId());
        model.addAttribute("upcomingBookings", upcomingBookings);
        model.addAttribute("passenger", passenger);
        
        return "passenger/dashboard";
    }

    @GetMapping("/search")
    public String searchBusForm(Model model) {
        List<Route> routes = routeService.findAllRoutes();
        model.addAttribute("routes", routes);
        model.addAttribute("cities", routeService.findAllCities());
        
        return "passenger/search";
    }

    @PostMapping("/search")
    public String searchBus(@RequestParam Long sourceId, 
                          @RequestParam Long destinationId,
                          @RequestParam String travelDate,
                          Model model) {
        List<Schedule> availableSchedules = scheduleService.findSchedulesByRouteAndDate(
            sourceId, destinationId, LocalDate.parse(travelDate));
            
        model.addAttribute("schedules", availableSchedules);
        model.addAttribute("travelDate", travelDate);
        model.addAttribute("source", routeService.findCityById(sourceId));
        model.addAttribute("destination", routeService.findCityById(destinationId));
        
        return "passenger/search-results";
    }

    @GetMapping("/booking/{scheduleId}")
    public String showBookingForm(@PathVariable Long scheduleId, Model model) {
        Schedule schedule = scheduleService.findScheduleById(scheduleId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User passenger = userService.findUserByUsername(auth.getName());
        
        model.addAttribute("schedule", schedule);
        model.addAttribute("bookingDTO", new BookingDTO());
        model.addAttribute("availableSeats", scheduleService.getAvailableSeats(scheduleId));
        model.addAttribute("passenger", passenger);
        
        return "passenger/booking";
    }

    @PostMapping("/booking")
    public String bookTicket(@ModelAttribute BookingDTO bookingDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User passenger = userService.findUserByUsername(auth.getName());
        
        bookingDTO.setUserId(passenger.getId());
        Booking booking = bookingFacade.createBooking(bookingDTO);
        
        return "redirect:/passenger/payment/" + booking.getId();
    }

    @GetMapping("/history")
    public String bookingHistory(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User passenger = userService.findUserByUsername(auth.getName());
        
        List<Booking> bookings = bookingService.findAllBookingsByUserId(passenger.getId());
        model.addAttribute("bookings", bookings);
        
        return "passenger/history";
    }

    @GetMapping("/ticket/{bookingId}")
    public String viewTicket(@PathVariable Long bookingId, Model model) {
        Booking booking = bookingService.findBookingById(bookingId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User passenger = userService.findUserByUsername(auth.getName());
        
        // Security check - ensure the booking belongs to the logged-in passenger
        if (!booking.getUser().getId().equals(passenger.getId())) {
            return "redirect:/passenger/dashboard";
        }
        
        model.addAttribute("booking", booking);
        return "passenger/ticket";
    }

    @GetMapping("/track/{scheduleId}")
    public String trackBus(@PathVariable Long scheduleId, Model model) {
        Schedule schedule = scheduleService.findScheduleById(scheduleId);
        model.addAttribute("schedule", schedule);
        model.addAttribute("currentLocation", scheduleService.getCurrentBusLocation(scheduleId));
        
        return "passenger/track";
    }

    @GetMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User passenger = userService.findUserByUsername(auth.getName());
        
        Booking booking = bookingService.findBookingById(bookingId);
        
        // Security check - ensure the booking belongs to the logged-in passenger
        if (!booking.getUser().getId().equals(passenger.getId())) {
            return "redirect:/passenger/dashboard";
        }
        
        bookingService.cancelBooking(bookingId);
        return "redirect:/passenger/history";
    }
}
