package com.busmanagement.factory;

import com.busmanagement.model.Role;
import com.busmanagement.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Factory Pattern implementation for creating different types of User objects.
 * This class abstracts the creation logic of User objects based on their roles.
 */
@Component
public class UserFactory {
    
    private final BCryptPasswordEncoder passwordEncoder;
    
    public UserFactory() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Creates a User object based on the specified role.
     * 
     * @param username Username for the user
     * @param password Raw password (will be encoded)
     * @param email Email address
     * @param fullName Full name of the user
     * @param phoneNumber Phone number
     * @param address Address
     * @param role Role of the user
     * @return A configured User object
     */
    public User createUser(String username, String password, String email, 
                          String fullName, String phoneNumber, String address, Role role) {
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        user.setRole(role);
        user.setActive(true);
        
        // Additional initialization based on user role
        switch (role.getName()) {
            case "ROLE_PASSENGER":
                // Initialize passenger-specific properties
                break;
                
            case "ROLE_DRIVER":
                // Initialize driver-specific properties
                break;
                
            case "ROLE_ADMIN":
                // Initialize admin-specific properties
                break;
                
            case "ROLE_MAINTENANCE":
                // Initialize maintenance-specific properties
                break;
                
            default:
                throw new IllegalArgumentException("Invalid role: " + role.getName());
        }
        
        return user;
    }
    
    /**
     * Convenience method to create a Passenger user
     */
    public User createPassenger(String username, String password, String email, 
                              String fullName, String phoneNumber, String address, Role role) {
        return createUser(username, password, email, fullName, phoneNumber, address, role);
    }
    
    /**
     * Convenience method to create a Driver user
     */
    public User createDriver(String username, String password, String email, 
                           String fullName, String phoneNumber, String address, Role role) {
        return createUser(username, password, email, fullName, phoneNumber, address, role);
    }
    
    /**
     * Convenience method to create an Admin user
     */
    public User createAdmin(String username, String password, String email, 
                          String fullName, String phoneNumber, String address, Role role) {
        return createUser(username, password, email, fullName, phoneNumber, address, role);
    }
    
    /**
     * Convenience method to create a Maintenance user
     */
    public User createMaintenanceStaff(String username, String password, String email, 
                                     String fullName, String phoneNumber, String address, Role role) {
        return createUser(username, password, email, fullName, phoneNumber, address, role);
    }
}
