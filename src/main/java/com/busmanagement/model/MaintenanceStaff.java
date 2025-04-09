package com.busmanagement.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity representing a maintenance staff user
 * Extends the base User class
 */
@Entity
@Table(name = "maintenance_staff")
public class MaintenanceStaff extends User {

    private String specialization;
    private String employeeId;
    private String department;
    private Integer experienceYears;
    private String certification;

    @OneToMany(mappedBy = "assignedStaff")
    private Set<MaintenanceRecord> maintenanceRecords = new HashSet<>();

    /**
     * Default constructor
     */
    public MaintenanceStaff() {
        super();
    }

    /**
     * Constructor with basic fields
     * 
     * @param username Username
     * @param email Email
     * @param password Password
     * @param fullName Full name
     * @param specialization Specialization
     */
    public MaintenanceStaff(String username, String email, String password, String fullName, String specialization) {
        super(username, email, password, fullName);
        this.specialization = specialization;
    }

    // Getters and Setters
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public Set<MaintenanceRecord> getMaintenanceRecords() {
        return maintenanceRecords;
    }

    public void setMaintenanceRecords(Set<MaintenanceRecord> maintenanceRecords) {
        this.maintenanceRecords = maintenanceRecords;
    }
}