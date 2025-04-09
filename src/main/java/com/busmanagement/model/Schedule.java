package com.busmanagement.model;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity representing a bus schedule in the system
 */
@Entity
@Table(name = "schedules")
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

    @OneToMany(mappedBy = "schedule")
    private Set<Trip> trips = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public Schedule() {
    }

    /**
     * Parameterized constructor
     * 
     * @param scheduleCode Schedule code
     * @param departureTime Departure time
     * @param arrivalTime Arrival time
     * @param route Route
     */
    public Schedule(String scheduleCode, LocalTime departureTime, LocalTime arrivalTime, Route route) {
        this.scheduleCode = scheduleCode;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.route = route;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Set<DayOfWeek> getOperatingDays() {
        return operatingDays;
    }

    public void setOperatingDays(Set<DayOfWeek> operatingDays) {
        this.operatingDays = operatingDays;
    }

    public BigDecimal getFareMultiplier() {
        return fareMultiplier;
    }

    public void setFareMultiplier(BigDecimal fareMultiplier) {
        this.fareMultiplier = fareMultiplier;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
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
     * Add an operating day to the schedule
     * 
     * @param day The day to add
     */
    public void addOperatingDay(DayOfWeek day) {
        operatingDays.add(day);
    }

    /**
     * Remove an operating day from the schedule
     * 
     * @param day The day to remove
     */
    public void removeOperatingDay(DayOfWeek day) {
        operatingDays.remove(day);
    }

    /**
     * Check if the schedule operates on a specific day
     * 
     * @param day The day to check
     * @return true if the schedule operates on the specified day
     */
    public boolean operatesOn(DayOfWeek day) {
        return operatingDays.contains(day);
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
    public BigDecimal calculateAdjustedFare(BigDecimal baseFare) {
        return baseFare.multiply(fareMultiplier);
    }
}