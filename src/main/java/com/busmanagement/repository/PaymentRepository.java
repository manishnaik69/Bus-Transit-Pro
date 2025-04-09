package com.busmanagement.repository;

import com.busmanagement.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Payment entity
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingId(Long bookingId);
    
    List<Payment> findByPaymentStatus(Payment.PaymentStatus status);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = com.busmanagement.model.Payment.PaymentStatus.COMPLETED " +
           "AND p.paymentDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT CAST(p.paymentDate AS date) as paymentDate, SUM(p.amount) " +
           "FROM Payment p WHERE p.paymentStatus = com.busmanagement.model.Payment.PaymentStatus.COMPLETED " +
           "AND p.paymentDate BETWEEN :startDate AND :endDate " +
           "GROUP BY CAST(p.paymentDate AS date) " +
           "ORDER BY paymentDate")
    List<Object[]> getDailyRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                              @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT p.paymentMethod, SUM(p.amount) " +
           "FROM Payment p WHERE p.paymentStatus = com.busmanagement.model.Payment.PaymentStatus.COMPLETED " +
           "AND p.paymentDate BETWEEN :startDate AND :endDate " +
           "GROUP BY p.paymentMethod")
    List<Object[]> getRevenueByPaymentMethod(@Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
}