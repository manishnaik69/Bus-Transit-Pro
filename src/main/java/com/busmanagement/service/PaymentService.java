package com.busmanagement.service;

import com.busmanagement.model.Payment;
import com.busmanagement.repository.InMemoryPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private final InMemoryPaymentRepository paymentRepository;

    @Autowired
    public PaymentService(InMemoryPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Optional<Payment> getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    public Optional<Payment> getPaymentByPaymentId(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId);
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    // Sample payment gateway integration method
    public Payment processPayment(Payment payment) {
        // In a real system, this would integrate with a payment gateway
        // For demo purposes, we'll simulate a successful payment
        payment.setPaymentId("PAY-" + System.currentTimeMillis());
        return savePayment(payment);
    }

    // Sample refund processing method
    public Payment processRefund(Payment payment, double amount) {
        // In a real system, this would integrate with a payment gateway for refund
        payment.setRefundAmount(amount);
        payment.setRefundStatus("PROCESSED");
        return savePayment(payment);
    }
}