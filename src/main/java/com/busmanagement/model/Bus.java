package com.busmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Entity representing a bus in the system
 */
@Entity
@Table(name = "buses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(unique = true)
    private String registrationNumber;

    @NotBlank
    @Size(max = 50)
    private String model;

    @NotNull
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private BusType type;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    private LocalDate manufacturingDate;

    private Boolean isAirConditioned;

    private Boolean hasWifi;

    private Boolean hasEntertainmentSystem;

    private Boolean hasChargingPoints;

    private String features;

    private String currentLocation;

    private String status;

    /**
     * Bus type enum
     */
    public enum BusType {
        SLEEPER, SEMI_SLEEPER, SEATER, LUXURY, SUPER_LUXURY, DOUBLE_DECKER
    }

    /**
     * Fuel type enum
     */
    public enum FuelType {
        DIESEL, PETROL, CNG, ELECTRIC, HYBRID
    }
}