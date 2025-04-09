package com.busmanagement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity representing a maintenance record for a bus
 */
@Entity
@Table(name = "maintenance_records")
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private MaintenanceStaff assignedStaff;

    @Enumerated(EnumType.STRING)
    private MaintenanceType type;

    @Column(nullable = false)
    private String description;

    private Integer odometerReading;
    private LocalDate maintenanceDate;
    private LocalDate nextDueDate;
    private Double cost;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status = MaintenanceStatus.SCHEDULED;

    private String resolution;
    private String remarks;
    private Boolean isRecurring = false;
    private Integer recurringIntervalDays;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public MaintenanceRecord() {
    }

    /**
     * Constructor with required fields
     * 
     * @param bus Bus
     * @param type Maintenance type
     * @param description Description
     * @param maintenanceDate Maintenance date
     */
    public MaintenanceRecord(Bus bus, MaintenanceType type, String description, LocalDate maintenanceDate) {
        this.bus = bus;
        this.type = type;
        this.description = description;
        this.maintenanceDate = maintenanceDate;
    }

    // Getters and setters
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

    public MaintenanceStaff getAssignedStaff() {
        return assignedStaff;
    }

    public void setAssignedStaff(MaintenanceStaff assignedStaff) {
        this.assignedStaff = assignedStaff;
    }

    public MaintenanceType getType() {
        return type;
    }

    public void setType(MaintenanceType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOdometerReading() {
        return odometerReading;
    }

    public void setOdometerReading(Integer odometerReading) {
        this.odometerReading = odometerReading;
    }

    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public LocalDate getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public MaintenanceStatus getStatus() {
        return status;
    }

    public void setStatus(MaintenanceStatus status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public Integer getRecurringIntervalDays() {
        return recurringIntervalDays;
    }

    public void setRecurringIntervalDays(Integer recurringIntervalDays) {
        this.recurringIntervalDays = recurringIntervalDays;
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
     * Complete this maintenance
     * 
     * @param resolution Resolution
     * @param cost Cost
     */
    public void complete(String resolution, Double cost) {
        this.status = MaintenanceStatus.COMPLETED;
        this.resolution = resolution;
        this.cost = cost;
        
        if (this.isRecurring && this.recurringIntervalDays != null) {
            this.nextDueDate = this.maintenanceDate.plusDays(this.recurringIntervalDays);
        }
        
        if (this.bus != null) {
            this.bus.setLastMaintenanceDate(this.maintenanceDate);
            this.bus.setNextMaintenanceDate(this.nextDueDate);
        }
    }
}