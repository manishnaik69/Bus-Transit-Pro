package com.busmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a bus schedule in the system
 */
@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "schedule_code", nullable = false, unique = true)
    private String scheduleCode;

    @NotNull
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;
    
    @NotNull
    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @NotNull
    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    @Column(name = "is_active")
    private boolean isActive = true;

    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(name = "schedule_days", joinColumns = @JoinColumn(name = "schedule_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private Set<DayOfWeek> operatingDays = new HashSet<>();

    @Column(name = "fare_multiplier", precision = 10, scale = 2)
    private BigDecimal fareMultiplier = BigDecimal.ONE;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @Column(name = "available_seats")
    private Integer availableSeats;

    @Column(name = "status")
    private String status;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        
        // Initialize available seats based on bus capacity if null
        if (availableSeats == null && bus != null) {
            availableSeats = bus.getCapacity();
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Get the total travel time in minutes
     * 
     * @return The travel time in minutes
     */
    public int getTravelTimeMinutes() {
        int departureMinutes = departureTime.getHour() * 60 + departureTime.getMinute();
        int arrivalMinutes = arrivalTime.getHour() * 60 + arrivalTime.getMinute();
        
        if (arrivalMinutes < departureMinutes) {
            // If arrival is on the next day
            arrivalMinutes += 24 * 60;
        }
        
        return arrivalMinutes - departureMinutes;
    }

    /**
     * Calculate the adjusted fare for this schedule
     * 
     * @param baseFare The base fare from the route
     * @return The adjusted fare
     */
    public BigDecimal calculateAdjustedFare() {
        if (route == null || route.getFareAmount() == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(route.getFareAmount()).multiply(fareMultiplier);
    }
}