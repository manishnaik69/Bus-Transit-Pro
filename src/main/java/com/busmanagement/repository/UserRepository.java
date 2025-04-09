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
     * Find a user by their username
     *
     * @param username the username
     * @return the optional user
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if a username exists
     *
     * @param username the username
     * @return true if exists, false otherwise
     */
    Boolean existsByUsername(String username);

    /**
     * Check if an email exists
     *
     * @param email the email
     * @return true if exists, false otherwise
     */
    Boolean existsByEmail(String email);
}