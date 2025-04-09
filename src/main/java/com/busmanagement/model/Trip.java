package com.busmanagement.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity representing a trip on a specific route with a specific bus and driver
 */
@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tripCode;
    
    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;
    
    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;
    
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;
    
    private LocalDateTime scheduledDepartureTime;
    private LocalDateTime scheduledArrivalTime;
    private LocalDateTime actualDepartureTime;
    private LocalDateTime actualArrivalTime;
    
    private Double fareAmount;
    private Integer availableSeats;
    private Integer bookedSeats;
    
    @Enumerated(EnumType.STRING)
    private TripStatus status = TripStatus.SCHEDULED;
    
    private String cancellationReason;
    private String delayReason;
    
    private Double currentLatitude;
    private Double currentLongitude;
    private String currentLocation;
    private Integer currentSpeed;
    
    private Boolean isActive = true;

    @OneToMany(mappedBy = "trip")
    private Set<Booking> bookings = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public Trip() {
    }

    /**
     * Constructor with required fields
     * 
     * @param route Route
     * @param bus Bus
     * @param scheduledDepartureTime Scheduled departure time
     * @param scheduledArrivalTime Scheduled arrival time
     * @param fareAmount Fare amount
     */
    public Trip(Route route, Bus bus, LocalDateTime scheduledDepartureTime, 
               LocalDateTime scheduledArrivalTime, Double fareAmount) {
        this.route = route;
        this.bus = bus;
        this.scheduledDepartureTime = scheduledDepartureTime;
        this.scheduledArrivalTime = scheduledArrivalTime;
        this.fareAmount = fareAmount;
        this.availableSeats = bus.getSeatingCapacity();
        this.bookedSeats = 0;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
        if (bus != null && bus.getSeatingCapacity() != null) {
            this.availableSeats = bus.getSeatingCapacity() - (this.bookedSeats != null ? this.bookedSeats : 0);
        }
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public LocalDateTime getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public void setScheduledDepartureTime(LocalDateTime scheduledDepartureTime) {
        this.scheduledDepartureTime = scheduledDepartureTime;
    }

    public LocalDateTime getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public void setScheduledArrivalTime(LocalDateTime scheduledArrivalTime) {
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public LocalDateTime getActualDepartureTime() {
        return actualDepartureTime;
    }

    public void setActualDepartureTime(LocalDateTime actualDepartureTime) {
        this.actualDepartureTime = actualDepartureTime;
    }

    public LocalDateTime getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setActualArrivalTime(LocalDateTime actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }

    public Double getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(Double fareAmount) {
        this.fareAmount = fareAmount;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Integer getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(Integer bookedSeats) {
        this.bookedSeats = bookedSeats;
        if (this.bus != null && this.bus.getSeatingCapacity() != null) {
            this.availableSeats = this.bus.getSeatingCapacity() - bookedSeats;
        }
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getDelayReason() {
        return delayReason;
    }

    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Integer getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(Integer currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
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
     * Book seats for this trip
     * 
     * @param numberOfSeats Number of seats to book
     * @return true if booking is successful, false otherwise
     */
    public boolean bookSeats(int numberOfSeats) {
        if (this.availableSeats >= numberOfSeats) {
            this.bookedSeats += numberOfSeats;
            this.availableSeats -= numberOfSeats;
            return true;
        }
        return false;
    }

    /**
     * Cancel booking and release seats
     * 
     * @param numberOfSeats Number of seats to release
     */
    public void cancelBooking(int numberOfSeats) {
        this.bookedSeats -= numberOfSeats;
        this.availableSeats += numberOfSeats;
    }
}