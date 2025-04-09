package com.busmanagement.repository;

import com.busmanagement.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Route entity
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    // Custom queries will be implemented as needed
}