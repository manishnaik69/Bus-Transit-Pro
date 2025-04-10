package com.busmanagement.repository;

import com.busmanagement.model.SimpleBus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SimpleBusRepository extends JpaRepository<SimpleBus, Long> {
    
    Optional<SimpleBus> findByRegistrationNumber(String registrationNumber);
    
    List<SimpleBus> findByStatus(SimpleBus.BusStatus status);
    
    List<SimpleBus> findByCapacityGreaterThanEqual(Integer capacity);
}