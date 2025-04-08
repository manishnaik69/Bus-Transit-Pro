package com.busmanagement.repository;

import com.busmanagement.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Payment entities.
 * Provides methods to interact with the payments table in the database.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /**
     * Finds a payment by booking ID.
     * 
     * @param bookingId ID of the booking
     * @return Optional containing the payment, if found
     */
    Optional<Payment> findByBookingId(Long bookingId);
    
    /**
     * Finds all payments with a specific status.
     * 
     * @param paymentStatus Status of the payments
     * @return List of payments
     */
    List<Payment> findByPaymentStatus(String paymentStatus);
    
    /**
     * Finds all payments made using a specific payment method.
     * 
     * @param paymentMethod Method used for payment
     * @return List of payments
     */
    List<Payment> findByPaymentMethod(String paymentMethod);
    
    /**
     * Finds all payments made within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return List of payments
     */
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findByPaymentDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Gets the total revenue from payments within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Total revenue
     */
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = 'Completed' AND p.paymentDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Gets the daily revenue for each day within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return List of daily revenue amounts
     */
    @Query(value = "SELECT CAST(p.payment_date AS DATE) as day, SUM(p.amount) as revenue " +
           "FROM payments p " +
           "WHERE p.payment_status = 'Completed' " +
           "AND p.payment_date BETWEEN :startDate AND :endDate " +
           "GROUP BY CAST(p.payment_date AS DATE) ORDER BY day", 
           nativeQuery = true)
    List<Object[]> getDailyRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Gets the revenue by payment method within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return List of revenue amounts by payment method
     */
    @Query("SELECT p.paymentMethod, SUM(p.amount) FROM Payment p WHERE p.paymentStatus = 'Completed' AND p.paymentDate BETWEEN :startDate AND :endDate GROUP BY p.paymentMethod")
    List<Object[]> getRevenueByPaymentMethod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
