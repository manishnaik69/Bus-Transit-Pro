package com.busmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Entity representing a trip in the system
 */
@Entity
@Table(name = "trips")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    @NotNull
    private Double fareAmount;

    private Integer availableSeats;

    @Enumerated(EnumType.STRING)
    private TripStatus status;

    private String cancellationReason;

    private LocalDateTime actualDepartureTime;

    private LocalDateTime actualArrivalTime;

    private String remarks;

    /**
     * Trip status enum
     */
    public enum TripStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, DELAYED
    }
}