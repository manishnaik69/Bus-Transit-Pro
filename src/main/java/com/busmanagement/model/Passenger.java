package com.busmanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Entity representing a passenger in the system
 */
@Entity
@Table(name = "passengers")
@Data
@NoArgsConstructor
public class Passenger extends User {
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    private Integer age;
    
    private String address;
    
    private String city;
    
    private String state;
    
    private String pinCode;
    
    /**
     * Constructor with basic fields
     */
    public Passenger(String firstName, String lastName, String username, String email, String password) {
        super(firstName, lastName, username, email, password);
    }
    
    /**
     * Gender enum
     */
    public enum Gender {
        MALE, FEMALE, OTHER
    }
}