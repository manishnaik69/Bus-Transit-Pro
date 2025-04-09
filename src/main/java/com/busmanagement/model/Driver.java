package com.busmanagement.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity representing a bus driver
 * Extends the base User class
 */
@Entity
@Table(name = "drivers")
public class Driver extends User {

    private String licenseNumber;
    private LocalDate licenseExpiryDate;
    private Integer experienceYears;
    private String licenseType;
    private String adharNumber;
    private String panNumber;
    private String address;
    private String city;
    private String state;
    private String pinCode;
    private Boolean available = Boolean.TRUE;

    @ManyToOne
    @JoinColumn(name = "assigned_bus_id")
    private Bus assignedBus;

    @OneToMany(mappedBy = "driver")
    private Set<Trip> trips = new HashSet<>();

    /**
     * Default constructor
     */
    public Driver() {
        super();
    }

    /**
     * Constructor with basic fields
     * 
     * @param firstName First name
     * @param lastName Last name
     * @param username Username
     * @param email Email
     * @param password Password
     * @param licenseNumber License number
     */
    public Driver(String firstName, String lastName, String username, String email, String password, String licenseNumber) {
        super(firstName, lastName, username, email, password);
        this.licenseNumber = licenseNumber;
    }

    // Getters and Setters
    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public LocalDate getLicenseExpiryDate() {
        return licenseExpiryDate;
    }

    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) {
        this.licenseExpiryDate = licenseExpiryDate;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getAdharNumber() {
        return adharNumber;
    }

    public void setAdharNumber(String adharNumber) {
        this.adharNumber = adharNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Bus getAssignedBus() {
        return assignedBus;
    }

    public void setAssignedBus(Bus assignedBus) {
        this.assignedBus = assignedBus;
    }

    public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }
}