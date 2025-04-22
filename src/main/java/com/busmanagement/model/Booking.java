package com.busmanagement.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Booking {
    private Long id;
    private String bookingReference;
    private Schedule schedule;
    private String passengerName;
    private String passengerEmail;
    private String passengerPhone;
    private String status; // CONFIRMED, CANCELLED
    private LocalDateTime bookingTime;
    private LocalDateTime cancelTime;
    private List<Ticket> tickets = new ArrayList<>();
    private Payment payment;

    public Booking() {
        this.bookingReference = generateBookingReference();
        this.bookingTime = LocalDateTime.now();
        this.status = "CONFIRMED";
    }

    private String generateBookingReference() {
        return "BTP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Add a ticket to this booking
    public void addTicket(Ticket ticket) {
        if (tickets == null) {
            tickets = new ArrayList<>();
        }
        tickets.add(ticket);
        ticket.setBooking(this);
    }

    // Calculate total fare for all tickets
    public double getTotalFare() {
        if (tickets == null || tickets.isEmpty()) {
            return 0.0;
        }
        return tickets.stream().mapToDouble(Ticket::getFare).sum();
    }

    public String getFormattedBookingTime() {
        return bookingTime != null ? bookingTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")) : "";
    }

    public String getFormattedCancelTime() {
        return cancelTime != null ? cancelTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")) : "";
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

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public String getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public LocalDateTime getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(LocalDateTime cancelTime) {
        this.cancelTime = cancelTime;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}