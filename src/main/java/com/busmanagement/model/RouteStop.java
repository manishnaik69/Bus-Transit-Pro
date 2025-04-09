package com.busmanagement.model;

import javax.persistence.Embeddable;

/**
 * Embeddable class for route stops
 */
@Embeddable
public class RouteStop {

    private String name;
    private String city;
    private String landmark;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer minutesFromStart;
    private Integer distanceFromStart; // in kilometers
    private Boolean isPickupPoint = true;
    private Boolean isDropOffPoint = true;

    /**
     * Default constructor
     */
    public RouteStop() {
    }

    /**
     * Constructor with required fields
     * 
     * @param name Stop name
     * @param city City
     */
    public RouteStop(String name, String city) {
        this.name = name;
        this.city = city;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getMinutesFromStart() {
        return minutesFromStart;
    }

    public void setMinutesFromStart(Integer minutesFromStart) {
        this.minutesFromStart = minutesFromStart;
    }

    public Integer getDistanceFromStart() {
        return distanceFromStart;
    }

    public void setDistanceFromStart(Integer distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }

    public Boolean getIsPickupPoint() {
        return isPickupPoint;
    }

    public void setIsPickupPoint(Boolean isPickupPoint) {
        this.isPickupPoint = isPickupPoint;
    }

    public Boolean getIsDropOffPoint() {
        return isDropOffPoint;
    }

    public void setIsDropOffPoint(Boolean isDropOffPoint) {
        this.isDropOffPoint = isDropOffPoint;
    }
}