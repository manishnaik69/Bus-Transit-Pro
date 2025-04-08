package com.busmanagement.dto;

/**
 * Data Transfer Object for bus information.
 * Used to transfer bus data between the frontend and backend.
 */
public class BusDTO {
    private Long id;
    private String registrationNumber;
    private String model;
    private int capacity;
    private String type; // AC, Non-AC, Sleeper, etc.
    private String status; // Active, Maintenance, Retired

    // Default constructor
    public BusDTO() {
    }

    // Constructor with fields
    public BusDTO(Long id, String registrationNumber, String model, int capacity, String type, String status) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.capacity = capacity;
        this.type = type;
        this.status = status;
    }

    // Getters and Setters
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BusDTO{" +
                "id=" + id +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", model='" + model + '\'' +
                ", capacity=" + capacity +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
