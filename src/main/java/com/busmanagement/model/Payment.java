package com.busmanagement.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Payment {
    private Long id;
    private Booking booking;
    private double amount;
    private String paymentMethod; // CARD, UPI, NET_BANKING, etc.
    private String paymentId;
    private LocalDateTime paymentTime;
    private double refundAmount;
    private LocalDateTime refundTime;
    private String refundStatus; // NONE, INITIATED, PROCESSED, COMPLETED

    public Payment() {
        this.paymentTime = LocalDateTime.now();
        this.refundStatus = "NONE";
    }

    public String getFormattedPaymentTime() {
        return paymentTime != null ? paymentTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")) : "";
    }

    public String getFormattedRefundTime() {
        return refundTime != null ? refundTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")) : "";
    }

    public boolean isRefunded() {
        return "COMPLETED".equals(refundStatus);
    }

    public boolean isRefundInProcess() {
        return "INITIATED".equals(refundStatus) || "PROCESSED".equals(refundStatus);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public LocalDateTime getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(LocalDateTime refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }
}