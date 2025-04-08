package com.busmanagement.service;

import com.busmanagement.dto.PaymentDTO;
import com.busmanagement.model.Payment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing payment operations.
 */
public interface PaymentService {
    
    /**
     * Finds a payment by its ID.
     * 
     * @param id ID of the payment
     * @return The payment, or null if not found
     */
    Payment findPaymentById(Long id);
    
    /**
     * Finds a payment by booking ID.
     * 
     * @param bookingId ID of the booking
     * @return The payment, or null if not found
     */
    Payment findPaymentByBookingId(Long bookingId);
    
    /**
     * Finds all payments in the system.
     * 
     * @return List of all payments
     */
    List<Payment> findAllPayments();
    
    /**
     * Finds all payments with a specific status.
     * 
     * @param status Status of the payments
     * @return List of payments
     */
    List<Payment> findPaymentsByStatus(String status);
    
    /**
     * Processes a payment from a DTO.
     * 
     * @param paymentDTO The payment data
     * @return The processed payment
     */
    Payment processPayment(PaymentDTO paymentDTO);
    
    /**
     * Saves a new payment.
     * 
     * @param payment The payment to save
     * @return The saved payment
     */
    Payment savePayment(Payment payment);
    
    /**
     * Updates an existing payment.
     * 
     * @param payment The payment to update
     * @return The updated payment
     */
    Payment updatePayment(Payment payment);
    
    /**
     * Updates the status of a payment.
     * 
     * @param paymentId ID of the payment
     * @param status New status
     * @return The updated payment
     */
    Payment updatePaymentStatus(Long paymentId, String status);
    
    /**
     * Processes a refund for a payment.
     * 
     * @param paymentId ID of the payment
     * @param amount Amount to refund
     * @return The updated payment
     */
    Payment processRefund(Long paymentId, double amount);
    
    /**
     * Gets the total revenue within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Total revenue
     */
    double getTotalRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Gets the daily revenue within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Map of dates to revenue amounts
     */
    Map<LocalDate, Double> getDailyRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Gets the revenue by payment method within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Map of payment methods to revenue amounts
     */
    Map<String, Double> getRevenueByPaymentMethod(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Validates a payment to ensure it meets all business rules.
     * 
     * @param payment The payment to validate
     * @return true if the payment is valid, false otherwise
     */
    boolean validatePayment(Payment payment);
}
