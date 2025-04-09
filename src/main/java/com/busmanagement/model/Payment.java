package com.busmanagement.model;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity representing a payment in the system
 */
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionId;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paymentTime;
    private String paymentGatewayResponse;
    private String paymentGatewayTransactionId;
    private String failureReason;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public Payment() {
    }

    /**
     * Constructor with required fields
     * 
     * @param transactionId Transaction ID
     * @param booking Booking
     * @param amount Amount
     * @param paymentMethod Payment method
     */
    public Payment(String transactionId, Booking booking, Double amount, PaymentMethod paymentMethod) {
        this.transactionId = transactionId;
        this.booking = booking;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.PENDING;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentGatewayResponse() {
        return paymentGatewayResponse;
    }

    public void setPaymentGatewayResponse(String paymentGatewayResponse) {
        this.paymentGatewayResponse = paymentGatewayResponse;
    }

    public String getPaymentGatewayTransactionId() {
        return paymentGatewayTransactionId;
    }

    public void setPaymentGatewayTransactionId(String paymentGatewayTransactionId) {
        this.paymentGatewayTransactionId = paymentGatewayTransactionId;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Process this payment
     * 
     * @return true if payment is successful, false otherwise
     */
    public boolean processPayment() {
        // In a real application, this would integrate with a payment gateway
        this.status = PaymentStatus.COMPLETED;
        this.paymentTime = LocalDateTime.now();
        return true;
    }

    /**
     * Mark this payment as failed
     * 
     * @param reason Failure reason
     */
    public void markAsFailed(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
    }

    /**
     * Process refund for this payment
     * 
     * @param amount Refund amount
     * @return true if refund is successful, false otherwise
     */
    public boolean processRefund(Double amount) {
        if (this.status != PaymentStatus.COMPLETED) {
            return false;
        }
        // In a real application, this would integrate with a payment gateway
        this.status = PaymentStatus.REFUNDED;
        return true;
    }
}