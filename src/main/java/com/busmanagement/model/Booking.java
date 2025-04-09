package com.busmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a booking in the system
 */
@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @NotNull
    private LocalDateTime bookingTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ElementCollection
    @CollectionTable(name = "booked_seats", joinColumns = @JoinColumn(name = "booking_id"))
    private List<String> seatNumbers = new ArrayList<>();

    @NotNull
    private Integer numberOfSeats;

    @NotNull
    private Double totalAmount;

    private String cancellationReason;

    private LocalDateTime cancellationTime;

    private Double refundAmount;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    /**
     * Booking status enum
     */
    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }
}