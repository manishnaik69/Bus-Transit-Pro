package com.busmanagement.model;

import javax.persistence.*;

/**
 * Entity representing a seat in a booking
 */
@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String seatNumber;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private String passengerName;
    private Integer passengerAge;
    private String passengerGender;
    private String idProofType;
    private String idProofNumber;
    private Boolean isBooked = false;

    /**
     * Default constructor
     */
    public Seat() {
    }

    /**
     * Constructor with required fields
     * 
     * @param seatNumber Seat number
     */
    public Seat(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * Constructor with booking and seat number
     * 
     * @param seatNumber Seat number
     * @param booking Booking
     */
    public Seat(String seatNumber, Booking booking) {
        this.seatNumber = seatNumber;
        this.booking = booking;
        this.isBooked = true;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
        if (booking != null) {
            this.isBooked = true;
        } else {
            this.isBooked = false;
        }
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Integer getPassengerAge() {
        return passengerAge;
    }

    public void setPassengerAge(Integer passengerAge) {
        this.passengerAge = passengerAge;
    }

    public String getPassengerGender() {
        return passengerGender;
    }

    public void setPassengerGender(String passengerGender) {
        this.passengerGender = passengerGender;
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

    public Boolean getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Boolean isBooked) {
        this.isBooked = isBooked;
    }
}