package com.busmanagement.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a maintenance record for a bus.
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
    
    @Column(name = "maintenance_type", nullable = false)
    private String maintenanceType; // Regular, Repair, Emergency
    
    @Column(name = "description", nullable = false)
    private String description;
    
    @Column(name = "maintenance_date", nullable = false)
    private LocalDate maintenanceDate;
    
    @Column(name = "cost")
    private Double cost;
    
    @Column(name = "status", nullable = false)
    private String status; // Scheduled, In Progress, Completed
    
    @Column(name = "technician_name")
    private String technicianName;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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
    
    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }
    
    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }
    
    public Double getCost() {
        return cost;
    }
    
    public void setCost(Double cost) {
        this.cost = cost;
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
    
    @Override
    public String toString() {
        return "MaintenanceRecord{" +
                "id=" + id +
                ", bus=" + bus.getId() +
                ", maintenanceType='" + maintenanceType + '\'' +
                ", maintenanceDate=" + maintenanceDate +
                ", status='" + status + '\'' +
                '}';
    }
}
