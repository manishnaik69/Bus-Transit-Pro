package com.busmanagement.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Schedule {
    private Long id;
    private Route route;
    private Bus bus;
    private String driverName; // Simple string for now
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int availableSeats;
    private String status; // Scheduled, In-Progress, Completed, Cancelled
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    // Formatted date methods for display
    public String getFormattedDepartureTime() {
        return departureTime != null ? departureTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")) : "";
    }

    public String getFormattedArrivalTime() {
        return arrivalTime != null ? arrivalTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")) : "";
    }

    // Method to check if schedule is upcoming
    public boolean isUpcoming() {
        return departureTime != null && departureTime.isAfter(LocalDateTime.now());
    }

    public long getEstimatedTravelTime() {
        if (departureTime != null && arrivalTime != null) {
            return java.time.Duration.between(departureTime, arrivalTime).toHours();
        } else if (route != null) {
            // Fallback to route duration in minutes converted to hours
            return route.getDuration() / 60;
        }
        return 0;
    }
}