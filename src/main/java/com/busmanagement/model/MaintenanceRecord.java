package com.busmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Entity representing a maintenance record in the system
 */
@Entity
@Table(name = "maintenance_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private User maintenanceStaff;

    @NotNull
    @Column(name = "maintenance_date")
    private LocalDateTime maintenanceDate;

    @Enumerated(EnumType.STRING)
    private MaintenanceType maintenanceType;

    @NotNull
    private String description;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;

    private Double cost;

    private LocalDateTime nextMaintenanceDate;

    private String remarks;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Maintenance type enum
     */
    public enum MaintenanceType {
        ROUTINE, REPAIR, INSPECTION, EMERGENCY, OVERHAUL
    }

    /**
     * Maintenance status enum
     */
    public enum MaintenanceStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}