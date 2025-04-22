package com.busmanagement.model;

import java.time.LocalDateTime;

public class Ticket {
    private Long id;
    private Booking booking; // Reference to parent booking
    private int seatNumber;
    private double fare;
    private String status; // CONFIRMED, CANCELLED

    public Ticket() {
        this.status = "CONFIRMED";
    }

    // Convenience methods to access booking data
    public Schedule getSchedule() {
        return booking != null ? booking.getSchedule() : null;
    }

    public String getBookingReference() {
        return booking != null ? booking.getBookingReference() : null;
    }

    public String getPassengerName() {
        return booking != null ? booking.getPassengerName() : null;
    }

    public String getPassengerEmail() {
        return booking != null ? booking.getPassengerEmail() : null;
    }

    public String getPassengerPhone() {
        return booking != null ? booking.getPassengerPhone() : null;
    }

    public String getPaymentMethod() {
        return booking != null && booking.getPayment() != null ? booking.getPayment().getPaymentMethod() : null;
    }

    public LocalDateTime getBookingTime() {
        return booking != null ? booking.getBookingTime() : null;
    }

    public LocalDateTime getCancelTime() {
        return booking != null ? booking.getCancelTime() : null;
    }

    public String getFormattedBookingTime() {
        return booking != null ? booking.getFormattedBookingTime() : "";
    }

    public String getFormattedCancelTime() {
        return booking != null ? booking.getFormattedCancelTime() : "";
    }

    public boolean isCancelled() {
        return "CANCELLED".equals(status);
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

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}