package com.busmanagement.controller;

import com.busmanagement.model.Booking;
import com.busmanagement.model.Payment;
import com.busmanagement.model.Schedule;
import com.busmanagement.model.Ticket;
import com.busmanagement.service.BookingService;
import com.busmanagement.service.CityService;
import com.busmanagement.service.ScheduleService;
import com.busmanagement.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/booking")
public class BookingController {

    private final CityService cityService;
    private final ScheduleService scheduleService;
    private final BookingService bookingService;
    private final TicketService ticketService;

    @Autowired
    public BookingController(
            CityService cityService,
            ScheduleService scheduleService,
            BookingService bookingService,
            TicketService ticketService) {
        this.cityService = cityService;
        this.scheduleService = scheduleService;
        this.bookingService = bookingService;
        this.ticketService = ticketService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Register custom editor for Schedule objects
        binder.registerCustomEditor(Schedule.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                    return;
                }
                Long scheduleId = Long.parseLong(text);
                Schedule schedule = scheduleService.getScheduleById(scheduleId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Schedule ID: " + text));
                setValue(schedule);
            }

            @Override
            public String getAsText() {
                Schedule schedule = (Schedule) getValue();
                return (schedule != null ? schedule.getId().toString() : "");
            }
        });
    }

    // Search form
    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("cities", cityService.getAllCities());
        return "booking/search";
    }

    // Search results
    @GetMapping("/results")
    public String showSearchResults(
            @RequestParam Long sourceId,
            @RequestParam Long destinationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate,
            Model model) {

        List<Schedule> schedules = scheduleService.findSchedulesByRouteAndDate(sourceId, destinationId, travelDate);
        model.addAttribute("schedules", schedules);
        model.addAttribute("travelDate", travelDate);

        return "booking/results";
    }

    // Select seats
    @GetMapping("/select-seat/{scheduleId}")
    public String selectSeat(@PathVariable Long scheduleId, Model model) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId)
                .orElse(null);

        if (schedule == null) {
            return "redirect:/booking/search?error=Schedule not found";
        }

        // Get booked seats for this schedule
        List<Integer> bookedSeats = ticketService.getBookedSeatsByScheduleId(scheduleId);

        model.addAttribute("schedule", schedule);
        model.addAttribute("bookedSeats", bookedSeats);

        return "booking/select-seat";
    }

    // Passenger details
    @PostMapping("/passenger-details")
    public String passengerDetails(@RequestParam Long scheduleId,
            @RequestParam int seatNumber,
            Model model) {

        Schedule schedule = scheduleService.getScheduleById(scheduleId)
                .orElse(null);

        if (schedule == null) {
            return "redirect:/booking/search?error=Schedule not found";
        }

        // Create a new booking and ticket
        Booking booking = new Booking();
        booking.setSchedule(schedule);

        // Factory pattern: Creating objects without exposing creation logic
        Ticket ticket = new Ticket();
        ticket.setBooking(booking);
        ticket.setSeatNumber(seatNumber);
        ticket.setFare(schedule.getRoute().getFareAmount());

        model.addAttribute("booking", booking);
        model.addAttribute("ticket", ticket);
        model.addAttribute("schedule", schedule);

        return "booking/passenger-details";
    }

    // Find the payment method and replace it with this implementation
    @PostMapping("/payment")
    public String payment(
            @ModelAttribute Booking booking,
            @RequestParam Long scheduleId,
            @RequestParam int seatNumber,
            @RequestParam double fare,
            Model model) {

        Schedule schedule = scheduleService.getScheduleById(scheduleId)
                .orElse(null);

        if (schedule == null) {
            return "redirect:/booking/search?error=Schedule not found";
        }

        // Set the schedule on the booking
        booking.setSchedule(schedule);

        // Create a ticket for this booking
        Ticket ticket = new Ticket();
        ticket.setSeatNumber(seatNumber);
        ticket.setFare(fare);

        // Create a payment object and set initial values
        Payment payment = new Payment();
        payment.setAmount(fare);

        model.addAttribute("booking", booking);
        model.addAttribute("ticket", ticket);
        model.addAttribute("payment", payment);
        model.addAttribute("scheduleId", scheduleId);
        model.addAttribute("seatNumber", seatNumber);
        model.addAttribute("fare", fare);

        return "booking/payment";
    }

    // Make sure your confirm method has all needed parameters
    // Strategy pattern: Confirming the booking, different behavioural strategies
    // employed
    @PostMapping("/confirm")
    public String confirmBooking(
            @ModelAttribute Booking booking,
            @RequestParam int seatNumber,
            @RequestParam double fare,
            @RequestParam String paymentMethod,
            @RequestParam Long scheduleId,
            RedirectAttributes redirectAttributes) {

        try {
            // Get the schedule from the ID
            Schedule schedule = scheduleService.getScheduleById(scheduleId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Schedule ID"));

            // Create the booking with ticket and payment
            Booking confirmedBooking = bookingService.createSingleTicketBooking(
                    schedule,
                    booking.getPassengerName(),
                    booking.getPassengerEmail(),
                    booking.getPassengerPhone(),
                    seatNumber,
                    fare,
                    paymentMethod,
                    "PAY-" + System.currentTimeMillis());

            redirectAttributes.addFlashAttribute("success", "Booking confirmed successfully!");
            return "redirect:/booking/confirmation/" + confirmedBooking.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to confirm booking: " + e.getMessage());
            return "redirect:/booking/search";
        }
    }

    // Booking confirmation
    @GetMapping("/confirmation/{id}")
    public String confirmationPage(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id)
                .orElse(null);

        if (booking == null) {
            return "redirect:/booking/search?error=Booking not found";
        }

        // Get tickets and payment
        List<Ticket> tickets = ticketService.getTicketsByBookingId(id);

        model.addAttribute("booking", booking);
        model.addAttribute("tickets", tickets);

        return "booking/confirmation";
    }

    @GetMapping("/my-bookings")
    public String myBookings(@RequestParam(required = false) String email, Model model) {
        // If no email is provided, show email input form
        if (email == null || email.isEmpty()) {
            return "booking/email-form";
        }

        // Get bookings by email
        List<Booking> bookings = bookingService.getBookingsByPassengerEmail(email);

        // Get all tickets associated with these bookings
        List<Ticket> tickets = new ArrayList<>();
        for (Booking booking : bookings) {
            tickets.addAll(ticketService.getTicketsByBookingId(booking.getId()));
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("tickets", tickets);
        model.addAttribute("email", email);

        return "booking/my-bookings";
    }

    // Cancel booking
    // Observer Pattern can be implemented here.
    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Booking cancelledBooking = bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("success",
                    "Booking cancelled successfully. Refund will be processed shortly.");
            return "redirect:/booking/confirmation/" + cancelledBooking.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to cancel booking: " + e.getMessage());
            return "redirect:/booking/confirmation/" + id;
        }
    }

    // Add these methods to your BookingController class:

    // Show the find ticket form
    @GetMapping("/find-ticket")
    public String findTicketForm() {
        return "booking/find-ticket";
    }

    // Process the find ticket form submission
    @PostMapping("/find-ticket")
    public String findTicket(
            @RequestParam String bookingReference,
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {

        Optional<Booking> booking = bookingService.getBookingByReference(bookingReference);

        if (booking.isPresent() && booking.get().getPassengerEmail().equals(email)) {
            // Find tickets associated with this booking
            List<Ticket> tickets = ticketService.getTicketsByBookingId(booking.get().getId());

            if (!tickets.isEmpty()) {
                // Redirect to the first ticket (assuming one ticket per booking for now)
                return "redirect:/booking/ticket/" + tickets.get(0).getId();
            } else {
                return "redirect:/booking/confirmation/" + booking.get().getId();
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "No booking found with the provided reference and email");
            return "redirect:/booking/find-ticket";
        }
    }

    // View a specific ticket
    @GetMapping("/ticket/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);

        if (ticket.isPresent()) {
            model.addAttribute("ticket", ticket.get());
            return "booking/ticket";
        } else {
            return "redirect:/booking/find-ticket?error=Ticket not found";
        }
    }
}