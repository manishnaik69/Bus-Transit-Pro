package com.busmanagement.repository;

import com.busmanagement.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryPaymentRepository {
    private static final Map<Long, Payment> payments = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(1);

    public List<Payment> findAll() {
        return new ArrayList<>(payments.values());
    }

    public Optional<Payment> findById(Long id) {
        return Optional.ofNullable(payments.get(id));
    }

    public Optional<Payment> findByBookingId(Long bookingId) {
        return payments.values().stream()
                .filter(payment -> payment.getBooking() != null && payment.getBooking().getId().equals(bookingId))
                .findFirst();
    }

    public Optional<Payment> findByPaymentId(String paymentId) {
        return payments.values().stream()
                .filter(payment -> payment.getPaymentId().equals(paymentId))
                .findFirst();
    }

    public Payment save(Payment payment) {
        if (payment.getId() == null) {
            payment.setId(idCounter.getAndIncrement());
        }
        payments.put(payment.getId(), payment);
        return payment;
    }

    public void deleteById(Long id) {
        payments.remove(id);
    }
}