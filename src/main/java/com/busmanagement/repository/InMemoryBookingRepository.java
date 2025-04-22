package com.busmanagement.repository;

import com.busmanagement.model.Booking;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryBookingRepository {
    private static final Map<Long, Booking> bookings = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(1);

    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    public Optional<Booking> findById(Long id) {
        return Optional.ofNullable(bookings.get(id));
    }

    public Optional<Booking> findByBookingReference(String reference) {
        return bookings.values().stream()
                .filter(booking -> booking.getBookingReference().equals(reference))
                .findFirst();
    }

    public List<Booking> findByPassengerEmail(String email) {
        return bookings.values().stream()
                .filter(booking -> booking.getPassengerEmail().equals(email))
                .collect(Collectors.toList());
    }

    public Booking save(Booking booking) {
        if (booking.getId() == null) {
            booking.setId(idCounter.getAndIncrement());
        }
        bookings.put(booking.getId(), booking);
        return booking;
    }

    public void deleteById(Long id) {
        bookings.remove(id);
    }
}