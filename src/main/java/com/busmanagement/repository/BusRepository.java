package com.busmanagement.repository;

import com.busmanagement.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Bus entity
 */
@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    // Custom queries will be implemented as needed
}