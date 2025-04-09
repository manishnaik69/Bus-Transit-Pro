package com.busmanagement.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity representing a booking in the system
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bookingNumber;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    private Integer numberOfSeats;
    private Double totalFare;
    private String pickupPoint;
    private String dropoffPoint;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    private LocalDateTime bookingTime;
    private LocalDateTime cancellationTime;
    private String cancellationReason;
    private Boolean refundIssued = false;
    private Double refundAmount;

    @OneToMany(mappedBy = "booking")
    private Set<Seat> seats = new HashSet<>();

    @OneToOne(mappedBy = "booking")
    private Payment payment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public Booking() {
    }

    /**
     * Constructor with required fields
     * 
     * @param bookingNumber Booking number
     * @param passenger Passenger
     * @param trip Trip
     * @param numberOfSeats Number of seats
     * @param totalFare Total fare
     */
    public Booking(String bookingNumber, Passenger passenger, Trip trip, Integer numberOfSeats, Double totalFare) {
        this.bookingNumber = bookingNumber;
        this.passenger = passenger;
        this.trip = trip;
        this.numberOfSeats = numberOfSeats;
        this.totalFare = totalFare;
        this.bookingTime = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(Double totalFare) {
        this.totalFare = totalFare;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public String getDropoffPoint() {
        return dropoffPoint;
    }

    public void setDropoffPoint(String dropoffPoint) {
        this.dropoffPoint = dropoffPoint;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public LocalDateTime getCancellationTime() {
        return cancellationTime;
    }

    public void setCancellationTime(LocalDateTime cancellationTime) {
        this.cancellationTime = cancellationTime;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Boolean getRefundIssued() {
        return refundIssued;
    }

    public void setRefundIssued(Boolean refundIssued) {
        this.refundIssued = refundIssued;
    }

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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
     * Cancel this booking
     * 
     * @param reason Cancellation reason
     * @param refundAmount Refund amount
     */
    public void cancel(String reason, Double refundAmount) {
        this.status = BookingStatus.CANCELLED;
        this.cancellationTime = LocalDateTime.now();
        this.cancellationReason = reason;
        this.refundAmount = refundAmount;
        
        if (refundAmount > 0) {
            this.refundIssued = true;
        }
        
        // Release the seats
        if (this.trip != null) {
            this.trip.cancelBooking(this.numberOfSeats);
        }
    }
}