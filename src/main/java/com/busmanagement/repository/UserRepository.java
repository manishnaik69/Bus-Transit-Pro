package com.busmanagement.repository;

import com.busmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by username
     * 
     * @param username Username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by email
     * 
     * @param email Email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if a username exists
     * 
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    Boolean existsByUsername(String username);
    
    /**
     * Check if an email exists
     * 
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    Boolean existsByEmail(String email);
}