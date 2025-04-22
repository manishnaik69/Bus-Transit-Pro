package com.busmanagement.repository;

import com.busmanagement.model.Bus;
import com.busmanagement.model.Maintenance;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryMaintenanceRepository {
    private static InMemoryMaintenanceRepository instance;
    private final Map<Long, Maintenance> maintenances = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // Constructor
    protected InMemoryMaintenanceRepository() {
        // Initialize with some sample data
        createSampleMaintenanceData();
    }

    // Singleton pattern
    public static synchronized InMemoryMaintenanceRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryMaintenanceRepository();
        }
        return instance;
    }

    // Create sample maintenance data
    private void createSampleMaintenanceData() {
        // This would be populated with real data in production
    }

    // Find all maintenances
    public List<Maintenance> findAll() {
        return new ArrayList<>(maintenances.values());
    }

    // Find maintenance by id
    public Optional<Maintenance> findById(Long id) {
        return Optional.ofNullable(maintenances.get(id));
    }

    // Save a maintenance record
    public Maintenance save(Maintenance maintenance) {
        if (maintenance.getId() == null) {
            maintenance.setId(idCounter.getAndIncrement());
            maintenance.setCreatedAt(LocalDateTime.now());
        }
        maintenances.put(maintenance.getId(), maintenance);
        return maintenance;
    }

    // Delete a maintenance record
    public void deleteById(Long id) {
        maintenances.remove(id);
    }

    // Find by bus id
    public List<Maintenance> findByBusId(Long busId) {
        return maintenances.values().stream()
                .filter(m -> m.getBus() != null && m.getBus().getId().equals(busId))
                .collect(Collectors.toList());
    }

    // Find scheduled maintenances
    public List<Maintenance> findScheduled() {
        return maintenances.values().stream()
                .filter(m -> "SCHEDULED".equals(m.getStatus()))
                .collect(Collectors.toList());
    }

    // Find in-progress maintenances
    public List<Maintenance> findInProgress() {
        return maintenances.values().stream()
                .filter(m -> "IN_PROGRESS".equals(m.getStatus()))
                .collect(Collectors.toList());
    }

    // Find completed maintenances
    public List<Maintenance> findCompleted() {
        return maintenances.values().stream()
                .filter(m -> "COMPLETED".equals(m.getStatus()))
                .collect(Collectors.toList());
    }

    // Find upcoming scheduled maintenance (future dates)
    public List<Maintenance> findUpcoming() {
        LocalDateTime now = LocalDateTime.now();
        return maintenances.values().stream()
                .filter(m -> "SCHEDULED".equals(m.getStatus()) && m.getScheduledDate().isAfter(now))
                .collect(Collectors.toList());
    }

    // Find overdue maintenance
    public List<Maintenance> findOverdue() {
        LocalDateTime now = LocalDateTime.now();
        return maintenances.values().stream()
                .filter(m -> "SCHEDULED".equals(m.getStatus()) && m.getScheduledDate().isBefore(now))
                .collect(Collectors.toList());
    }
}