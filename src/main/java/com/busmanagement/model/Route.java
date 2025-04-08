package com.busmanagement.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a bus route in the system.
 */
@Entity
@Table(name = "routes")
public class Route {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private City source;
    
    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private City destination;
    
    @Column(nullable = false)
    private Double distance; // in kilometers
    
    @Column(nullable = false)
    private Double duration; // in hours
    
    @Column(name = "fare_amount", nullable = false)
    private Double fareAmount;
    
    @OneToMany(mappedBy = "route")
    private List<Schedule> schedules;
    
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
    
    public City getSource() {
        return source;
    }
    
    public void setSource(City source) {
        this.source = source;
    }
    
    public City getDestination() {
        return destination;
    }
    
    public void setDestination(City destination) {
        this.destination = destination;
    }
    
    public Double getDistance() {
        return distance;
    }
    
    public void setDistance(Double distance) {
        this.distance = distance;
    }
    
    public Double getDuration() {
        return duration;
    }
    
    public void setDuration(Double duration) {
        this.duration = duration;
    }
    
    public Double getFareAmount() {
        return fareAmount;
    }
    
    public void setFareAmount(Double fareAmount) {
        this.fareAmount = fareAmount;
    }
    
    public List<Schedule> getSchedules() {
        return schedules;
    }
    
    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
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
        return "Route{" +
                "id=" + id +
                ", source=" + source.getName() +
                ", destination=" + destination.getName() +
                ", distance=" + distance +
                ", duration=" + duration +
                ", fareAmount=" + fareAmount +
                '}';
    }
}
