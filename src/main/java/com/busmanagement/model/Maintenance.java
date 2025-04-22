package com.busmanagement.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Maintenance {
    private Long id;
    private Bus bus;
    private String maintenanceType;
    private String description;
    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;
    private String status; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    private String technicianName;
    private Double cost;
    private String notes;
    private LocalDateTime createdAt;

    // Constructor
    public Maintenance() {
        this.createdAt = LocalDateTime.now();
        this.status = "SCHEDULED";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Convenience methods
    public String getFormattedScheduledDate() {
        if (scheduledDate == null)
            return "";
        return scheduledDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
    }

    public String getFormattedCompletedDate() {
        if (completedDate == null)
            return "Not completed";
        return completedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
    }

    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    public boolean isInProgress() {
        return "IN_PROGRESS".equals(status);
    }
}