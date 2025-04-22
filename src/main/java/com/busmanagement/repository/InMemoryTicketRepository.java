package com.busmanagement.repository;

import com.busmanagement.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryTicketRepository {
    private static final Map<Long, Ticket> tickets = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(1);

    public List<Ticket> findAll() {
        return new ArrayList<>(tickets.values());
    }

    public Optional<Ticket> findById(Long id) {
        return Optional.ofNullable(tickets.get(id));
    }

    public List<Ticket> findByBookingId(Long bookingId) {
        return tickets.values().stream()
                .filter(ticket -> ticket.getBooking() != null && ticket.getBooking().getId().equals(bookingId))
                .collect(Collectors.toList());
    }

    public List<Ticket> findByScheduleId(Long scheduleId) {
        return tickets.values().stream()
                .filter(ticket -> ticket.getSchedule() != null && ticket.getSchedule().getId().equals(scheduleId))
                .filter(ticket -> !ticket.isCancelled())
                .collect(Collectors.toList());
    }

    public List<Integer> findBookedSeatsByScheduleId(Long scheduleId) {
        return tickets.values().stream()
                .filter(ticket -> ticket.getSchedule() != null && ticket.getSchedule().getId().equals(scheduleId))
                .filter(ticket -> !ticket.isCancelled())
                .map(Ticket::getSeatNumber)
                .collect(Collectors.toList());
    }

    public Ticket save(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setId(idCounter.getAndIncrement());
        }
        tickets.put(ticket.getId(), ticket);
        return ticket;
    }

    public void deleteById(Long id) {
        tickets.remove(id);
    }
}