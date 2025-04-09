package com.busmanagement.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity representing a passenger user
 * Extends the base User class
 */
@Entity
@Table(name = "passengers")
public class Passenger extends User {

    private String gender;
    private Integer age;
    private String idProofType;
    private String idProofNumber;
    private String address;
    private String city;
    private String state;
    private String pinCode;

    @OneToMany(mappedBy = "passenger")
    private Set<Booking> bookings = new HashSet<>();

    /**
     * Default constructor
     */
    public Passenger() {
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
     */
    public Passenger(String firstName, String lastName, String username, String email, String password) {
        super(firstName, lastName, username, email, password);
    }

    // Getters and Setters
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIdProofType() {
        return idProofType;
    }

    public void setIdProofType(String idProofType) {
        this.idProofType = idProofType;
    }

    public String getIdProofNumber() {
        return idProofNumber;
    }

    public void setIdProofNumber(String idProofNumber) {
        this.idProofNumber = idProofNumber;
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

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }
}