package com.busmanagement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity representing a bus in the system
 */
@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String registrationNumber;

    @Column(nullable = false)
    private String model;

    private String manufacturer;

    @Enumerated(EnumType.STRING)
    private BusType type;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    private Integer seatingCapacity;
    private Integer standingCapacity;
    private Integer totalCapacity;
    private Boolean acAvailable;
    private Boolean wifiAvailable;
    private Boolean usbChargingAvailable;
    private Boolean waterBottleAvailable;
    private Boolean blanketAvailable;
    private Boolean trackingEnabled;
    
    private Double fuelEfficiency; // km/liter
    private LocalDate registrationDate;
    private LocalDate insuranceExpiryDate;
    private LocalDate fitnessExpiryDate;
    private LocalDate permitExpiryDate;
    private LocalDate pollutionExpiryDate;
    
    private LocalDate lastMaintenanceDate;
    private LocalDate nextMaintenanceDate;
    
    private Integer currentOdometerReading;
    
    @Column(name = "is_active")
    private Boolean active = true;

    @OneToMany(mappedBy = "bus")
    private Set<MaintenanceRecord> maintenanceRecords = new HashSet<>();

    @OneToMany(mappedBy = "bus")
    private Set<Trip> trips = new HashSet<>();

    @OneToMany(mappedBy = "assignedBus")
    private Set<Driver> drivers = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public Bus() {
    }

    /**
     * Constructor with required fields
     * 
     * @param registrationNumber Registration number
     * @param model Model
     * @param type Bus type
     * @param seatingCapacity Seating capacity
     */
    public Bus(String registrationNumber, String model, BusType type, Integer seatingCapacity) {
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.type = type;
        this.seatingCapacity = seatingCapacity;
        this.totalCapacity = seatingCapacity;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public BusType getType() {
        return type;
    }

    public void setType(BusType type) {
        this.type = type;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
        updateTotalCapacity();
    }

    public Integer getStandingCapacity() {
        return standingCapacity;
    }

    public void setStandingCapacity(Integer standingCapacity) {
        this.standingCapacity = standingCapacity;
        updateTotalCapacity();
    }

    private void updateTotalCapacity() {
        int seating = (seatingCapacity != null) ? seatingCapacity : 0;
        int standing = (standingCapacity != null) ? standingCapacity : 0;
        this.totalCapacity = seating + standing;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Boolean getAcAvailable() {
        return acAvailable;
    }

    public void setAcAvailable(Boolean acAvailable) {
        this.acAvailable = acAvailable;
    }

    public Boolean getWifiAvailable() {
        return wifiAvailable;
    }

    public void setWifiAvailable(Boolean wifiAvailable) {
        this.wifiAvailable = wifiAvailable;
    }

    public Boolean getUsbChargingAvailable() {
        return usbChargingAvailable;
    }

    public void setUsbChargingAvailable(Boolean usbChargingAvailable) {
        this.usbChargingAvailable = usbChargingAvailable;
    }

    public Boolean getWaterBottleAvailable() {
        return waterBottleAvailable;
    }

    public void setWaterBottleAvailable(Boolean waterBottleAvailable) {
        this.waterBottleAvailable = waterBottleAvailable;
    }

    public Boolean getBlanketAvailable() {
        return blanketAvailable;
    }

    public void setBlanketAvailable(Boolean blanketAvailable) {
        this.blanketAvailable = blanketAvailable;
    }

    public Boolean getTrackingEnabled() {
        return trackingEnabled;
    }

    public void setTrackingEnabled(Boolean trackingEnabled) {
        this.trackingEnabled = trackingEnabled;
    }

    public Double getFuelEfficiency() {
        return fuelEfficiency;
    }

    public void setFuelEfficiency(Double fuelEfficiency) {
        this.fuelEfficiency = fuelEfficiency;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(LocalDate insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public LocalDate getFitnessExpiryDate() {
        return fitnessExpiryDate;
    }

    public void setFitnessExpiryDate(LocalDate fitnessExpiryDate) {
        this.fitnessExpiryDate = fitnessExpiryDate;
    }

    public LocalDate getPermitExpiryDate() {
        return permitExpiryDate;
    }

    public void setPermitExpiryDate(LocalDate permitExpiryDate) {
        this.permitExpiryDate = permitExpiryDate;
    }

    public LocalDate getPollutionExpiryDate() {
        return pollutionExpiryDate;
    }

    public void setPollutionExpiryDate(LocalDate pollutionExpiryDate) {
        this.pollutionExpiryDate = pollutionExpiryDate;
    }

    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public LocalDate getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(LocalDate nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public Integer getCurrentOdometerReading() {
        return currentOdometerReading;
    }

    public void setCurrentOdometerReading(Integer currentOdometerReading) {
        this.currentOdometerReading = currentOdometerReading;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<MaintenanceRecord> getMaintenanceRecords() {
        return maintenanceRecords;
    }

    public void setMaintenanceRecords(Set<MaintenanceRecord> maintenanceRecords) {
        this.maintenanceRecords = maintenanceRecords;
    }

    public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }

    public Set<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
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
}