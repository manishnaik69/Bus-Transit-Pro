package com.busmanagement.model;

/**
 * Enum representing the status of a trip
 */
public enum TripStatus {
    SCHEDULED,    // Trip is scheduled but not yet started
    BOARDING,     // Passengers are boarding
    DEPARTED,     // Bus has departed from origin
    IN_TRANSIT,   // Bus is currently in transit
    ARRIVED,      // Bus has arrived at destination
    COMPLETED,    // Trip has been completed
    DELAYED,      // Trip is delayed
    CANCELLED     // Trip has been cancelled
}