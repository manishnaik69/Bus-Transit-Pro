package com.busmanagement.dto;

/**
 * Data Transfer Object for route information.
 * Used to transfer route data between the frontend and backend.
 */
public class RouteDTO {
    private Long id;
    private Long sourceId;
    private Long destinationId;
    private Double distance;
    private Double duration; // in hours
    private Double fareAmount;
    
    // Default constructor
    public RouteDTO() {
    }
    
    // Constructor with fields
    public RouteDTO(Long id, Long sourceId, Long destinationId, Double distance, Double duration, Double fareAmount) {
        this.id = id;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.distance = distance;
        this.duration = duration;
        this.fareAmount = fareAmount;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getSourceId() {
        return sourceId;
    }
    
    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
    
    public Long getDestinationId() {
        return destinationId;
    }
    
    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
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
    
    @Override
    public String toString() {
        return "RouteDTO{" +
                "id=" + id +
                ", sourceId=" + sourceId +
                ", destinationId=" + destinationId +
                ", distance=" + distance +
                ", duration=" + duration +
                ", fareAmount=" + fareAmount +
                '}';
    }
}
