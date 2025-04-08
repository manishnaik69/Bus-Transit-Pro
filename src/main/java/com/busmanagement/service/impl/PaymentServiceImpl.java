package com.busmanagement.service.impl;

import com.busmanagement.dto.PaymentDTO;
import com.busmanagement.model.Booking;
import com.busmanagement.model.Payment;
import com.busmanagement.model.User;
import com.busmanagement.observer.NotificationPublisher;
import com.busmanagement.model.Notification;
import com.busmanagement.repository.BookingRepository;
import com.busmanagement.repository.PaymentRepository;
import com.busmanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the PaymentService interface.
 * Provides business logic for payment operations.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private NotificationPublisher notificationPublisher;

    @Override
    public Payment findPaymentById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        return payment.orElse(null);
    }

    @Override
    public Payment findPaymentByBookingId(Long bookingId) {
        Optional<Payment> payment = paymentRepository.findByBookingId(bookingId);
        return payment.orElse(null);
    }

    @Override
    public List<Payment> findAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> findPaymentsByStatus(String status) {
        return paymentRepository.findByPaymentStatus(status);
    }

    @Override
    @Transactional
    public Payment processPayment(PaymentDTO paymentDTO) {
        // Validate input
        if (paymentDTO == null || paymentDTO.getBookingId() == null || 
            paymentDTO.getAmount() == null || paymentDTO.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Invalid payment details");
        }
        
        // Find booking
        Optional<Booking> bookingOpt = bookingRepository.findById(paymentDTO.getBookingId());
        if (!bookingOpt.isPresent()) {
            throw new IllegalArgumentException("Booking not found with id: " + paymentDTO.getBookingId());
        }
        
        Booking booking = bookingOpt.get();
        
        // Check if payment already exists
        Optional<Payment> existingPayment = paymentRepository.findByBookingId(booking.getId());
        if (existingPayment.isPresent()) {
            throw new IllegalStateException("Payment already exists for this booking");
        }
        
        // Validate payment amount
        if (Math.abs(paymentDTO.getAmount() - booking.getTotalAmount()) > 0.01) {
            throw new IllegalArgumentException("Payment amount does not match booking amount");
        }
        
        // Create payment
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus("Completed");
        payment.setPaymentDate(LocalDateTime.now());
        
        // Generate transaction ID
        String transactionId = "TXN" + System.currentTimeMillis() + "-" + booking.getId();
        payment.setTransactionId(transactionId);
        
        // Save payment
        payment = paymentRepository.save(payment);
        
        // Update booking status
        booking.setStatus("PAID");
        booking.setPayment(payment);
        bookingRepository.save(booking);
        
        // Send notification
        User user = booking.getUser();
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle("Payment Successful");
        notification.setMessage("Your payment of ₹" + payment.getAmount() + " for booking #" + 
                                booking.getId() + " was successful. Transaction ID: " + transactionId);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        
        notificationPublisher.publishNotification(notification);
        
        return payment;
    }

    @Override
    @Transactional
    public Payment savePayment(Payment payment) {
        // Validate payment
        if (!validatePayment(payment)) {
            throw new IllegalArgumentException("Invalid payment details");
        }
        
        // Save payment
        Payment savedPayment = paymentRepository.save(payment);
        
        // Update booking status if payment is completed
        if ("Completed".equals(payment.getPaymentStatus())) {
            Booking booking = payment.getBooking();
            booking.setStatus("PAID");
            booking.setPayment(savedPayment);
            bookingRepository.save(booking);
        }
        
        return savedPayment;
    }

    @Override
    @Transactional
    public Payment updatePayment(Payment payment) {
        // Check if payment exists
        if (!paymentRepository.existsById(payment.getId())) {
            throw new IllegalArgumentException("Payment not found with id: " + payment.getId());
        }
        
        // Validate payment
        if (!validatePayment(payment)) {
            throw new IllegalArgumentException("Invalid payment details");
        }
        
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment updatePaymentStatus(Long paymentId, String status) {
        Payment payment = findPaymentById(paymentId);
        if (payment == null) {
            throw new IllegalArgumentException("Payment not found with id: " + paymentId);
        }
        
        payment.setPaymentStatus(status);
        
        // Update booking status if payment status changes
        if ("Completed".equals(status)) {
            Booking booking = payment.getBooking();
            booking.setStatus("PAID");
            bookingRepository.save(booking);
        } else if ("Failed".equals(status) || "Refunded".equals(status)) {
            Booking booking = payment.getBooking();
            booking.setStatus("PENDING_PAYMENT");
            bookingRepository.save(booking);
        }
        
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment processRefund(Long paymentId, double amount) {
        Payment payment = findPaymentById(paymentId);
        if (payment == null) {
            throw new IllegalArgumentException("Payment not found with id: " + paymentId);
        }
        
        // Validate refund amount
        if (amount <= 0 || amount > payment.getAmount()) {
            throw new IllegalArgumentException("Invalid refund amount");
        }
        
        // Process refund
        payment.setPaymentStatus("Refunded");
        payment = paymentRepository.save(payment);
        
        // Update booking status
        Booking booking = payment.getBooking();
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
        
        // Send notification
        User user = booking.getUser();
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle("Refund Processed");
        notification.setMessage("A refund of ₹" + amount + " for booking #" + 
                                booking.getId() + " has been processed.");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        
        notificationPublisher.publishNotification(notification);
        
        return payment;
    }

    @Override
    public double getTotalRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        Double totalRevenue = paymentRepository.getTotalRevenueBetweenDates(startDate, endDate);
        return totalRevenue != null ? totalRevenue : 0.0;
    }

    @Override
    public Map<LocalDate, Double> getDailyRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> result = paymentRepository.getDailyRevenueBetweenDates(startDate, endDate);
        Map<LocalDate, Double> revenueMap = new LinkedHashMap<>();
        
        for (Object[] row : result) {
            if (row[0] instanceof java.sql.Date) {
                LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
                Double amount = (Double) row[1];
                revenueMap.put(date, amount);
            } else if (row[0] instanceof LocalDate) {
                LocalDate date = (LocalDate) row[0];
                Double amount = (Double) row[1];
                revenueMap.put(date, amount);
            }
        }
        
        return revenueMap;
    }

    @Override
    public Map<String, Double> getRevenueByPaymentMethod(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> result = paymentRepository.getRevenueByPaymentMethod(startDate, endDate);
        Map<String, Double> revenueMap = new HashMap<>();
        
        for (Object[] row : result) {
            String paymentMethod = (String) row[0];
            Double amount = (Double) row[1];
            revenueMap.put(paymentMethod, amount);
        }
        
        return revenueMap;
    }

    @Override
    public boolean validatePayment(Payment payment) {
        // Check if booking is valid
        if (payment.getBooking() == null || payment.getBooking().getId() == null) {
            return false;
        }
        
        // Check if amount is valid
        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            return false;
        }
        
        // Check if payment method is valid
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().trim().isEmpty()) {
            return false;
        }
        
        // Check if payment status is valid
        String status = payment.getPaymentStatus();
        if (status == null || (!status.equals("Pending") && !status.equals("Completed") && 
                              !status.equals("Failed") && !status.equals("Refunded"))) {
            return false;
        }
        
        // Check if payment date is valid
        if (payment.getPaymentDate() == null) {
            return false;
        }
        
        return true;
    }
}
