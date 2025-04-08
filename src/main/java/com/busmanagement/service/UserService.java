package com.busmanagement.service;

import com.busmanagement.dto.RegistrationDTO;
import com.busmanagement.model.Role;
import com.busmanagement.model.User;

import java.util.List;

/**
 * Service interface for managing user operations.
 */
public interface UserService {
    
    /**
     * Finds a user by their ID.
     * 
     * @param id ID of the user
     * @return The user, or null if not found
     */
    User findUserById(Long id);
    
    /**
     * Finds a user by their username.
     * 
     * @param username Username of the user
     * @return The user, or null if not found
     */
    User findUserByUsername(String username);
    
    /**
     * Finds a user by their email.
     * 
     * @param email Email of the user
     * @return The user, or null if not found
     */
    User findUserByEmail(String email);
    
    /**
     * Finds all users in the system.
     * 
     * @return List of all users
     */
    List<User> findAllUsers();
    
    /**
     * Finds all users with a specific role.
     * 
     * @param roleId ID of the role
     * @return List of users
     */
    List<User> findUsersByRole(Long roleId);
    
    /**
     * Finds all users with a specific role name.
     * 
     * @param roleName Name of the role
     * @return List of users
     */
    List<User> findUsersByRoleName(String roleName);
    
    /**
     * Finds all active users.
     * 
     * @return List of active users
     */
    List<User> findActiveUsers();
    
    /**
     * Registers a new user from a DTO.
     * 
     * @param registrationDTO The registration data
     * @return The registered user
     */
    User registerNewUser(RegistrationDTO registrationDTO);
    
    /**
     * Saves a new user.
     * 
     * @param user The user to save
     * @return The saved user
     */
    User saveUser(User user);
    
    /**
     * Updates an existing user.
     * 
     * @param id ID of the user to update
     * @param user The updated user data
     * @return The updated user
     */
    User updateUser(Long id, User user);
    
    /**
     * Activates a user.
     * 
     * @param userId ID of the user
     * @return The activated user
     */
    User activateUser(Long userId);
    
    /**
     * Deactivates a user.
     * 
     * @param userId ID of the user
     * @return The deactivated user
     */
    User deactivateUser(Long userId);
    
    /**
     * Deletes a user.
     * 
     * @param userId ID of the user to delete
     */
    void deleteUser(Long userId);
    
    /**
     * Changes a user's password.
     * 
     * @param userId ID of the user
     * @param currentPassword Current password
     * @param newPassword New password
     * @return The updated user
     */
    User changePassword(Long userId, String currentPassword, String newPassword);
    
    /**
     * Checks if a username is already taken.
     * 
     * @param username Username to check
     * @return true if the username exists, false otherwise
     */
    boolean isUsernameTaken(String username);
    
    /**
     * Checks if an email is already registered.
     * 
     * @param email Email to check
     * @return true if the email exists, false otherwise
     */
    boolean isEmailTaken(String email);
    
    /**
     * Finds all roles in the system.
     * 
     * @return List of all roles
     */
    List<Role> findAllRoles();
    
    /**
     * Finds a role by its name.
     * 
     * @param name Name of the role
     * @return The role, or null if not found
     */
    Role findRoleByName(String name);
    
    /**
     * Counts the total number of users.
     * 
     * @return Total number of users
     */
    long countAllUsers();
    
    /**
     * Counts the number of users with a specific role.
     * 
     * @param roleId ID of the role
     * @return Number of users
     */
    long countUsersByRole(Long roleId);
}
