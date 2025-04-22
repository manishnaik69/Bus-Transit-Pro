package com.busmanagement.service;

import com.busmanagement.model.Ticket;
import com.busmanagement.repository.InMemoryTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final InMemoryTicketRepository ticketRepository;

    @Autowired
    public TicketService(InMemoryTicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> getTicketsByBookingId(Long bookingId) {
        return ticketRepository.findByBookingId(bookingId);
    }

    public List<Ticket> getTicketsByScheduleId(Long scheduleId) {
        return ticketRepository.findByScheduleId(scheduleId);
    }

    public List<Integer> getBookedSeatsByScheduleId(Long scheduleId) {
        return ticketRepository.findBookedSeatsByScheduleId(scheduleId);
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public Ticket cancelTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with ID: " + id));
        ticket.setStatus("CANCELLED");
        return ticketRepository.save(ticket);
    }
}