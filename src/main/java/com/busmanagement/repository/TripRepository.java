package com.busmanagement.repository;

import com.busmanagement.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Trip entity
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    // Custom queries will be implemented as needed
}