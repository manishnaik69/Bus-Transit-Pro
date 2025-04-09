package com.busmanagement.factory;

import com.busmanagement.model.Role;
import com.busmanagement.model.Role.ERole;
import com.busmanagement.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

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
        
        // Set roles collection
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        
        // Set user role enum based on role
        switch (role.getName()) {
            case ROLE_USER:
                user.setRole(User.UserRole.PASSENGER);
                break;
                
            case ROLE_DRIVER:
                user.setRole(User.UserRole.DRIVER);
                break;
                
            case ROLE_ADMIN:
                user.setRole(User.UserRole.ADMIN);
                break;
                
            case ROLE_MAINTENANCE:
                user.setRole(User.UserRole.MAINTENANCE_STAFF);
                break;
                
            default:
                throw new IllegalArgumentException("Invalid role: " + role.getName());
        }
        
        user.setIsActive(true);
        
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
