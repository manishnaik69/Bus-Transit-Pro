package com.busmanagement.service;

import com.busmanagement.model.Booking;
import com.busmanagement.model.Payment;
import com.busmanagement.model.Schedule;
import com.busmanagement.model.Ticket;
import com.busmanagement.repository.InMemoryBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final InMemoryBookingRepository bookingRepository;
    private final PaymentService paymentService;
    private final TicketService ticketService;

    @Autowired
    public BookingService(
            InMemoryBookingRepository bookingRepository,
            PaymentService paymentService,
            TicketService ticketService) {
        this.bookingRepository = bookingRepository;
        this.paymentService = paymentService;
        this.ticketService = ticketService;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Optional<Booking> getBookingByReference(String reference) {
        return bookingRepository.findByBookingReference(reference);
    }

    public List<Booking> getBookingsByPassengerEmail(String email) {
        return bookingRepository.findByPassengerEmail(email);
    }
    // Create booking needs to have all info about Booking so it can be considered as Creator 
    // Create a new booking with tickets and payment
    public Booking createBooking(Booking booking, List<Ticket> tickets, Payment payment) {
        // Save the booking first
        Booking savedBooking = bookingRepository.save(booking);

        // Associate tickets with the booking and save them
        for (Ticket ticket : tickets) {
            ticket.setBooking(savedBooking);
            ticketService.saveTicket(ticket);
        }

        // Associate payment with the booking and save it
        payment.setBooking(savedBooking);
        payment.setAmount(savedBooking.getTotalFare());
        paymentService.savePayment(payment);

        return savedBooking;
    }

    // Simple booking with a single ticket
    public Booking createSingleTicketBooking(Schedule schedule, String passengerName,
            String passengerEmail, String passengerPhone, int seatNumber,
            double fare, String paymentMethod, String paymentId) {

        // Create booking
        Booking booking = new Booking();
        booking.setSchedule(schedule);
        booking.setPassengerName(passengerName);
        booking.setPassengerEmail(passengerEmail);
        booking.setPassengerPhone(passengerPhone);
        Booking savedBooking = bookingRepository.save(booking);

        // Create ticket
        Ticket ticket = new Ticket();
        ticket.setBooking(savedBooking);
        ticket.setSeatNumber(seatNumber);
        ticket.setFare(fare);
        ticketService.saveTicket(ticket);

        // Create payment
        Payment payment = new Payment();
        payment.setBooking(savedBooking);
        payment.setAmount(fare);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentId(paymentId);
        paymentService.savePayment(payment);

        return savedBooking;
    }

    public Booking cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + id));

        // Mark booking as cancelled
        booking.setStatus("CANCELLED");
        booking.setCancelTime(LocalDateTime.now());

        // Mark all tickets as cancelled
        List<Ticket> tickets = ticketService.getTicketsByBookingId(id);
        for (Ticket ticket : tickets) {
            ticket.setStatus("CANCELLED");
            ticketService.saveTicket(ticket);
        }

        // Process refund
        Optional<Payment> payment = paymentService.getPaymentByBookingId(id);
        if (payment.isPresent()) {
            Payment p = payment.get();
            p.setRefundStatus("INITIATED");
            p.setRefundTime(LocalDateTime.now());

            // Calculate refund amount based on cancellation policy
            LocalDateTime departureTime = booking.getSchedule().getDepartureTime();
            LocalDateTime now = LocalDateTime.now();
            long hoursUntilDeparture = java.time.Duration.between(now, departureTime).toHours();

            double refundPercent;
            if (hoursUntilDeparture > 48) {
                refundPercent = 0.9; // 90% refund
            } else if (hoursUntilDeparture > 24) {
                refundPercent = 0.75; // 75% refund
            } else if (hoursUntilDeparture > 12) {
                refundPercent = 0.5; // 50% refund
            } else {
                refundPercent = 0.0; // No refund
            }

            p.setRefundAmount(p.getAmount() * refundPercent);
            paymentService.savePayment(p);
        }

        return bookingRepository.save(booking);
    }
}