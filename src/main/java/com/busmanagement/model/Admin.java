package com.busmanagement.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity representing an admin user
 * Extends the base User class
 */
@Entity
@Table(name = "admins")
public class Admin extends User {

    private String department;
    private String designation;
    private String employeeId;

    /**
     * Default constructor
     */
    public Admin() {
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
    public Admin(String firstName, String lastName, String username, String email, String password) {
        super(firstName, lastName, username, email, password);
    }

    // Getters and Setters
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}