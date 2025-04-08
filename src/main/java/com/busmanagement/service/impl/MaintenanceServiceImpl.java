package com.busmanagement.service.impl;

import com.busmanagement.model.Bus;
import com.busmanagement.model.MaintenanceRecord;
import com.busmanagement.repository.BusRepository;
import com.busmanagement.repository.MaintenanceRepository;
import com.busmanagement.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the MaintenanceService interface.
 * Provides business logic for maintenance operations.
 */
@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;
    
    @Autowired
    private BusRepository busRepository;

    @Override
    public MaintenanceRecord findMaintenanceRecordById(Long id) {
        Optional<MaintenanceRecord> maintenanceRecord = maintenanceRepository.findById(id);
        return maintenanceRecord.orElse(null);
    }

    @Override
    public List<MaintenanceRecord> findAllMaintenanceRecords() {
        return maintenanceRepository.findAll();
    }

    @Override
    public List<MaintenanceRecord> getMaintenanceHistoryForBus(Long busId) {
        return maintenanceRepository.findByBusId(busId);
    }

    @Override
    public List<MaintenanceRecord> findMaintenanceRecordsByStatus(String status) {
        return maintenanceRepository.findByStatus(status);
    }

    @Override
    public List<MaintenanceRecord> findMaintenanceRecordsByDate(LocalDate maintenanceDate) {
        return maintenanceRepository.findByMaintenanceDate(maintenanceDate);
    }

    @Override
    @Transactional
    public MaintenanceRecord saveMaintenanceRecord(MaintenanceRecord maintenanceRecord) {
        // Validate the maintenance record
        if (!isValidMaintenanceRecord(maintenanceRecord)) {
            throw new IllegalArgumentException("Invalid maintenance record details");
        }
        
        return maintenanceRepository.save(maintenanceRecord);
    }

    @Override
    @Transactional
    public MaintenanceRecord scheduleMaintenance(MaintenanceRecord maintenanceRecord) {
        // Set default status if not provided
        if (maintenanceRecord.getStatus() == null || maintenanceRecord.getStatus().trim().isEmpty()) {
            maintenanceRecord.setStatus("Scheduled");
        }
        
        // Validate the maintenance record
        if (!isValidMaintenanceRecord(maintenanceRecord)) {
            throw new IllegalArgumentException("Invalid maintenance record details");
        }
        
        // Update bus status if it's not already in maintenance
        Bus bus = maintenanceRecord.getBus();
        if (!"Maintenance".equals(bus.getStatus())) {
            bus.setStatus("Maintenance");
            busRepository.save(bus);
        }
        
        return maintenanceRepository.save(maintenanceRecord);
    }

    @Override
    @Transactional
    public MaintenanceRecord updateMaintenanceRecord(Long id, MaintenanceRecord maintenanceRecord) {
        // Check if the maintenance record exists
        if (!maintenanceRepository.existsById(id)) {
            throw new IllegalArgumentException("Maintenance record not found with id: " + id);
        }
        
        // Set the ID on the maintenance record
        maintenanceRecord.setId(id);
        
        // Validate the maintenance record
        if (!isValidMaintenanceRecord(maintenanceRecord)) {
            throw new IllegalArgumentException("Invalid maintenance record details");
        }
        
        return maintenanceRepository.save(maintenanceRecord);
    }

    @Override
    @Transactional
    public MaintenanceRecord updateMaintenanceStatus(Long maintenanceId, String status) {
        MaintenanceRecord maintenanceRecord = findMaintenanceRecordById(maintenanceId);
        if (maintenanceRecord == null) {
            throw new IllegalArgumentException("Maintenance record not found with id: " + maintenanceId);
        }
        
        maintenanceRecord.setStatus(status);
        
        // If maintenance is completed, update the bus status back to Active
        if ("Completed".equals(status)) {
            Bus bus = maintenanceRecord.getBus();
            
            // Check if there are any other scheduled or in-progress maintenance records for this bus
            List<MaintenanceRecord> activeRecords = maintenanceRepository.findByBusIdAndStatus(bus.getId(), "Scheduled");
            activeRecords.addAll(maintenanceRepository.findByBusIdAndStatus(bus.getId(), "In Progress"));
            
            // Filter out the current record
            activeRecords = activeRecords.stream()
                .filter(record -> !record.getId().equals(maintenanceId))
                .collect(Collectors.toList());
            
            // If there are no other active maintenance records, set the bus status back to Active
            if (activeRecords.isEmpty()) {
                bus.setStatus("Active");
                busRepository.save(bus);
            }
        }
        
        return maintenanceRepository.save(maintenanceRecord);
    }

    @Override
    @Transactional
    public void deleteMaintenanceRecord(Long maintenanceId) {
        if (!maintenanceRepository.existsById(maintenanceId)) {
            throw new IllegalArgumentException("Maintenance record not found with id: " + maintenanceId);
        }
        
        maintenanceRepository.deleteById(maintenanceId);
    }

    @Override
    public Map<Long, String> getMaintenanceStatusForAllBuses() {
        List<Bus> buses = busRepository.findAll();
        Map<Long, String> statusMap = new HashMap<>();
        
        for (Bus bus : buses) {
            List<MaintenanceRecord> records = maintenanceRepository.findMostRecentMaintenanceForBus(bus.getId());
            
            if (records.isEmpty()) {
                statusMap.put(bus.getId(), "No maintenance records");
            } else {
                MaintenanceRecord latestRecord = records.get(0);
                statusMap.put(bus.getId(), latestRecord.getStatus());
            }
        }
        
        return statusMap;
    }

    @Override
    public List<MaintenanceRecord> getRecentMaintenanceIssues(int limit) {
        return maintenanceRepository.findAll(
            PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt")))
            .getContent();
    }

    @Override
    public long countBusesNeedingMaintenance() {
        return busRepository.findBusesNeedingMaintenance().size();
    }

    @Override
    public double getTotalMaintenanceCostBetweenDates(LocalDate startDate, LocalDate endDate) {
        Double cost = maintenanceRepository.getTotalMaintenanceCostBetweenDates(startDate, endDate);
        return cost != null ? cost : 0.0;
    }
    
    /**
     * Validates a maintenance record entity.
     * 
     * @param record The maintenance record to validate
     * @return true if the record is valid, false otherwise
     */
    private boolean isValidMaintenanceRecord(MaintenanceRecord record) {
        // Check if bus is valid
        if (record.getBus() == null || record.getBus().getId() == null) {
            return false;
        }
        
        // Check if maintenance type is valid
        if (record.getMaintenanceType() == null || record.getMaintenanceType().trim().isEmpty()) {
            return false;
        }
        
        // Check if description is valid
        if (record.getDescription() == null || record.getDescription().trim().isEmpty()) {
            return false;
        }
        
        // Check if maintenance date is valid
        if (record.getMaintenanceDate() == null) {
            return false;
        }
        
        // Check if status is valid
        String status = record.getStatus();
        if (status == null || (!status.equals("Scheduled") && !status.equals("In Progress") && !status.equals("Completed"))) {
            return false;
        }
        
        // Check if cost is valid (if provided)
        if (record.getCost() != null && record.getCost() < 0) {
            return false;
        }
        
        return true;
    }
}
