package com.busmanagement.facade;

import com.busmanagement.dto.BookingDTO;
import com.busmanagement.model.Booking;
import com.busmanagement.model.Notification;
import com.busmanagement.model.Schedule;
import com.busmanagement.model.User;
import com.busmanagement.observer.NotificationPublisher;
import com.busmanagement.service.BookingService;
import com.busmanagement.service.ScheduleService;
import com.busmanagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Facade Pattern implementation to simplify the booking process.
 * This class coordinates between different services to create a booking.
 */
@Component
public class BookingFacade {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ScheduleService scheduleService;
    
    @Autowired
    private NotificationPublisher notificationPublisher;
    
    /**
     * Creates a booking using the provided BookingDTO.
     * This method coordinates all the steps needed to create a booking:
     * 1. Check seat availability
     * 2. Create the booking
     * 3. Update seat status
     * 4. Send booking confirmation notification
     * 
     * @param bookingDTO The booking information
     * @return The created Booking entity
     */
    @Transactional
    public Booking createBooking(BookingDTO bookingDTO) {
        // Get required entities
        User user = userService.findUserById(bookingDTO.getUserId());
        Schedule schedule = scheduleService.findScheduleById(bookingDTO.getScheduleId());
        
        // Check seat availability
        for (Integer seatNumber : bookingDTO.getSeatNumbers()) {
            if (!scheduleService.isSeatAvailable(schedule.getId(), seatNumber)) {
                throw new IllegalStateException("Seat " + seatNumber + " is not available.");
            }
        }
        
        // Calculate fare
        double seatFare = schedule.getRoute().getFareAmount();
        double totalAmount = seatFare * bookingDTO.getSeatNumbers().size();
        bookingDTO.setTotalAmount(totalAmount);
        
        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSchedule(schedule);
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setSeatNumbers(String.join(",", bookingDTO.getSeatNumbers().stream()
                              .map(String::valueOf)
                              .toArray(String[]::new)));
        booking.setTotalAmount(totalAmount);
        booking.setStatus("PENDING_PAYMENT");
        
        Booking savedBooking = bookingService.saveBooking(booking);
        
        // Update seat status
        for (Integer seatNumber : bookingDTO.getSeatNumbers()) {
            scheduleService.updateSeatStatus(schedule.getId(), seatNumber, "BOOKED");
        }
        
        // Send notification
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage("Your booking has been created. Total amount: ₹" + totalAmount);
        notification.setTitle("Booking Confirmation");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        
        notificationPublisher.publishNotification(notification);
        
        return savedBooking;
    }
    
    /**
     * Cancels a booking and handles refund process.
     * 
     * @param bookingId The ID of the booking to cancel
     * @return The updated Booking entity
     */
    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingService.findBookingById(bookingId);
        
        // Update booking status
        booking.setStatus("CANCELLED");
        Booking updatedBooking = bookingService.updateBooking(booking);
        
        // Release the seats
        Schedule schedule = booking.getSchedule();
        String[] seatNumbers = booking.getSeatNumbers().split(",");
        
        for (String seatNumber : seatNumbers) {
            scheduleService.updateSeatStatus(schedule.getId(), Integer.parseInt(seatNumber), "AVAILABLE");
        }
        
        // Process refund if applicable
        if ("PAID".equals(booking.getStatus())) {
            // Calculate refund amount based on cancellation policy
            double refundAmount = calculateRefundAmount(booking);
            
            // Process refund
            if (refundAmount > 0) {
                processRefund(booking, refundAmount);
            }
        }
        
        // Send cancellation notification
        Notification notification = new Notification();
        notification.setUser(booking.getUser());
        notification.setMessage("Your booking has been cancelled.");
        notification.setTitle("Booking Cancellation");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        
        notificationPublisher.publishNotification(notification);
        
        return updatedBooking;
    }
    
    /**
     * Calculates the refund amount based on cancellation policy.
     * 
     * @param booking The booking to be cancelled
     * @return The refund amount
     */
    private double calculateRefundAmount(Booking booking) {
        // Example cancellation policy:
        // - More than 24 hours before departure: 90% refund
        // - 12-24 hours before departure: 50% refund
        // - Less than 12 hours before departure: 0% refund
        
        LocalDateTime departureTime = booking.getSchedule().getDepartureTime();
        LocalDateTime now = LocalDateTime.now();
        
        long hoursUntilDeparture = java.time.Duration.between(now, departureTime).toHours();
        
        if (hoursUntilDeparture > 24) {
            return booking.getTotalAmount() * 0.9; // 90% refund
        } else if (hoursUntilDeparture >= 12) {
            return booking.getTotalAmount() * 0.5; // 50% refund
        } else {
            return 0.0; // No refund
        }
    }
    
    /**
     * Processes a refund for a cancelled booking.
     * 
     * @param booking The cancelled booking
     * @param refundAmount The amount to refund
     */
    private void processRefund(Booking booking, double refundAmount) {
        // In a real implementation, this would integrate with a payment gateway
        // to process the refund back to the user's payment method
        
        // For now, we'll just log the refund
        System.out.println("Processing refund of ₹" + refundAmount + " for booking #" + booking.getId());
        
        // Send refund notification
        Notification notification = new Notification();
        notification.setUser(booking.getUser());
        notification.setMessage("A refund of ₹" + refundAmount + " has been processed for your cancelled booking.");
        notification.setTitle("Refund Processed");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        
        notificationPublisher.publishNotification(notification);
    }
}
