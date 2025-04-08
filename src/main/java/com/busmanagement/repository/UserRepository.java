package com.busmanagement.repository;

import com.busmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entities.
 * Provides methods to interact with the users table in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Finds a user by username.
     * 
     * @param username Username to search for
     * @return Optional containing the user, if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Finds a user by email.
     * 
     * @param email Email to search for
     * @return Optional containing the user, if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Finds all users with a specific role.
     * 
     * @param roleId ID of the role
     * @return List of users
     */
    List<User> findByRoleId(Long roleId);
    
    /**
     * Finds all active users.
     * 
     * @return List of active users
     */
    List<User> findByActiveTrue();
    
    /**
     * Finds all users with a specific role name.
     * 
     * @param roleName Name of the role
     * @return List of users
     */
    @Query("SELECT u FROM User u JOIN u.role r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
    
    /**
     * Checks if a username is already taken.
     * 
     * @param username Username to check
     * @return true if the username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Checks if an email is already registered.
     * 
     * @param email Email to check
     * @return true if the email exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Counts the number of users with a specific role.
     * 
     * @param roleId ID of the role
     * @return Number of users
     */
    long countByRoleId(Long roleId);
    
    /**
     * Finds users by their full name (partial match).
     * 
     * @param fullName Full name to search for
     * @return List of users
     */
    List<User> findByFullNameContainingIgnoreCase(String fullName);
    
    /**
     * Finds users by their phone number.
     * 
     * @param phoneNumber Phone number to search for
     * @return List of users
     */
    List<User> findByPhoneNumber(String phoneNumber);
}
