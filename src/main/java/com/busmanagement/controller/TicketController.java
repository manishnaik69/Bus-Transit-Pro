package com.busmanagement.controller;

import com.busmanagement.model.Ticket;
import com.busmanagement.service.BookingService;
import com.busmanagement.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final BookingService bookingService;

    @Autowired
    public TicketController(TicketService ticketService, BookingService bookingService) {
        this.ticketService = ticketService;
        this.bookingService = bookingService;
    }

    // Admin: View all tickets
    @GetMapping
    public String listTickets(Model model) {
        model.addAttribute("tickets", ticketService.getAllTickets());
        return "admin/tickets";
    }

    // View single ticket
    @GetMapping("/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);

        if (ticket.isPresent()) {
            model.addAttribute("ticket", ticket.get());
            return "ticket/view";
        } else {
            return "redirect:/booking/find?error=Ticket not found";
        }
    }

    // Admin: View tickets by schedule
    @GetMapping("/schedule/{scheduleId}")
    public String viewTicketsBySchedule(@PathVariable Long scheduleId, Model model) {
        List<Ticket> tickets = ticketService.getTicketsByScheduleId(scheduleId);
        model.addAttribute("tickets", tickets);
        model.addAttribute("scheduleId", scheduleId);
        return "admin/schedule-tickets";
    }

    @PostMapping("/cancel/{id}")
    public String cancelTicket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Ticket> ticketOpt = ticketService.getTicketById(id);

            if (ticketOpt.isPresent()) {
                Ticket ticket = ticketOpt.get();
                ticket.setStatus("CANCELLED");
                ticketService.saveTicket(ticket);

                // If this is the only ticket, also cancel the booking
                Long bookingId = ticket.getBooking().getId();
                bookingService.cancelBooking(bookingId);

                redirectAttributes.addFlashAttribute("success",
                        "Ticket cancelled successfully. Refund will be processed shortly.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Ticket not found");
            }

            return "redirect:/booking/ticket/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to cancel ticket: " + e.getMessage());
            return "redirect:/booking/ticket/" + id;
        }
    }

    // REST API endpoints for mobile app or AJAX calls
    @GetMapping("/api/schedule/{scheduleId}")
    @ResponseBody
    public ResponseEntity<List<Integer>> getBookedSeats(@PathVariable Long scheduleId) {
        List<Integer> bookedSeats = ticketService.getBookedSeatsByScheduleId(scheduleId);
        return ResponseEntity.ok(bookedSeats);
    }
}