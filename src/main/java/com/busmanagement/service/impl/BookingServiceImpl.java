package com.busmanagement.service.impl;

import com.busmanagement.model.Booking;
import com.busmanagement.model.Schedule;
import com.busmanagement.repository.BookingRepository;
import com.busmanagement.service.BookingService;
import com.busmanagement.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the BookingService interface.
 * Provides business logic for booking operations.
 */
@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private ScheduleService scheduleService;

    @Override
    public Booking findBookingById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.orElse(null);
    }

    @Override
    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> findAllBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> findUpcomingBookingsByUserId(Long userId) {
        return bookingRepository.findUpcomingBookingsByUserId(userId);
    }

    @Override
    public List<Booking> findBookingsByScheduleId(Long scheduleId) {
        return bookingRepository.findByScheduleId(scheduleId);
    }

    @Override
    @Transactional
    public Booking saveBooking(Booking booking) {
        if (!validateBooking(booking)) {
            throw new IllegalArgumentException("Invalid booking details");
        }
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking updateBooking(Booking booking) {
        if (!bookingRepository.existsById(booking.getId())) {
            throw new IllegalArgumentException("Booking not found with id: " + booking.getId());
        }
        
        if (!validateBooking(booking)) {
            throw new IllegalArgumentException("Invalid booking details");
        }
        
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking updateBookingStatus(Long bookingId, String status) {
        Booking booking = findBookingById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found with id: " + bookingId);
        }
        
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = findBookingById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found with id: " + bookingId);
        }
        
        // Only allow cancellation if the booking is not already cancelled
        if (Booking.BookingStatus.CANCELLED.equals(booking.getStatus())) {
            throw new IllegalStateException("Booking is already cancelled");
        }
        
        // Update booking status
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        Booking cancelledBooking = bookingRepository.save(booking);
        
        // Release the seats
        Schedule schedule = booking.getSchedule();
        List<String> seatNumbers = booking.getSeatNumbers();
        
        for (String seatNumber : seatNumbers) {
            scheduleService.updateSeatStatus(schedule.getId(), Integer.parseInt(seatNumber.trim()), "AVAILABLE");
        }
        
        // Update available seats count in the schedule
        schedule.setAvailableSeats(schedule.getAvailableSeats() + seatNumbers.size());
        scheduleService.updateSchedule(schedule.getId(), schedule);
        
        return cancelledBooking;
    }

    @Override
    @Transactional
    public void deleteBooking(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new IllegalArgumentException("Booking not found with id: " + bookingId);
        }
        
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public boolean validateBooking(Booking booking) {
        // Check if booking has a valid user
        if (booking.getUser() == null || booking.getUser().getId() == null) {
            return false;
        }
        
        // Check if booking has a valid schedule
        if (booking.getSchedule() == null || booking.getSchedule().getId() == null) {
            return false;
        }
        
        // Check if the booking has seat numbers
        if (booking.getSeatNumbers() == null || booking.getSeatNumbers().isEmpty()) {
            return false;
        }
        
        // Check if total amount is valid
        if (booking.getTotalAmount() <= 0) {
            return false;
        }
        
        // Check if booking status is valid
        if (booking.getStatus() == null) {
            return false;
        }
        
        return true;
    }

    @Override
    public long countTotalBookings() {
        return bookingRepository.count();
    }

    @Override
    public long countBookingsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.countBookingsBetweenDates(startDate, endDate);
    }

    @Override
    public double getTotalRevenueBetweenDates(LocalDate startDate, LocalDate endDate) {
        Double revenue = bookingRepository.getTotalRevenueBetweenDates(startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }
}
