package com.busmanagement.service;

import com.busmanagement.model.Bus;
import com.busmanagement.model.Maintenance;
import com.busmanagement.repository.InMemoryMaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceService {

    private final InMemoryMaintenanceRepository maintenanceRepository;
    private final BusService busService;

    @Autowired
    public MaintenanceService(InMemoryMaintenanceRepository maintenanceRepository, BusService busService) {
        this.maintenanceRepository = maintenanceRepository;
        this.busService = busService;
    }

    // Get all maintenance records
    public List<Maintenance> getAllMaintenanceRecords() {
        return maintenanceRepository.findAll();
    }

    // Get maintenance record by id
    public Optional<Maintenance> getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id);
    }

    // Create new maintenance record
    public Maintenance scheduleMaintenance(Maintenance maintenance) {
        // Validate the maintenance record
        validateMaintenance(maintenance);

        // Set bus status to MAINTENANCE if it's starting now
        if (maintenance.getStatus().equals("IN_PROGRESS")) {
            changeBusStatus(maintenance.getBus().getId(), Bus.BusStatus.MAINTENANCE);
        }

        return maintenanceRepository.save(maintenance);
    }

    // Update maintenance record
    public Maintenance updateMaintenance(Maintenance maintenance) {
        // Get the current maintenance record to check status change
        Optional<Maintenance> existingMaintenance = maintenanceRepository.findById(maintenance.getId());

        if (existingMaintenance.isPresent()) {
            Maintenance current = existingMaintenance.get();

            // If status changed from SCHEDULED to IN_PROGRESS
            if (!current.getStatus().equals("IN_PROGRESS") &&
                    maintenance.getStatus().equals("IN_PROGRESS")) {
                // Set bus to maintenance status
                changeBusStatus(maintenance.getBus().getId(), Bus.BusStatus.MAINTENANCE);
            }

            // If status changed to COMPLETED
            if (!current.getStatus().equals("COMPLETED") &&
                    maintenance.getStatus().equals("COMPLETED")) {
                // If not already set, set the completion date
                if (maintenance.getCompletedDate() == null) {
                    maintenance.setCompletedDate(LocalDateTime.now());
                }

                // Set bus back to active status
                changeBusStatus(maintenance.getBus().getId(), Bus.BusStatus.ACTIVE);
            }
        }

        return maintenanceRepository.save(maintenance);
    }

    // Delete maintenance record
    public void deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }

    // Get maintenance records for a specific bus
    public List<Maintenance> getMaintenanceByBusId(Long busId) {
        return maintenanceRepository.findByBusId(busId);
    }

    // Get scheduled maintenance
    public List<Maintenance> getScheduledMaintenance() {
        return maintenanceRepository.findScheduled();
    }

    // Get in-progress maintenance
    public List<Maintenance> getInProgressMaintenance() {
        return maintenanceRepository.findInProgress();
    }

    // Get completed maintenance
    public List<Maintenance> getCompletedMaintenance() {
        return maintenanceRepository.findCompleted();
    }

    // Get upcoming maintenance
    public List<Maintenance> getUpcomingMaintenance() {
        return maintenanceRepository.findUpcoming();
    }

    // Get overdue maintenance
    public List<Maintenance> getOverdueMaintenance() {
        return maintenanceRepository.findOverdue();
    }

    // Complete a maintenance task
    public Maintenance completeMaintenance(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance ID: " + id));

        maintenance.setStatus("COMPLETED");
        maintenance.setCompletedDate(LocalDateTime.now());

        // Set bus back to active status
        changeBusStatus(maintenance.getBus().getId(), Bus.BusStatus.ACTIVE);

        return maintenanceRepository.save(maintenance);
    }

    // Start a maintenance task
    public Maintenance startMaintenance(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance ID: " + id));

        maintenance.setStatus("IN_PROGRESS");

        // Set bus to maintenance status
        changeBusStatus(maintenance.getBus().getId(), Bus.BusStatus.MAINTENANCE);

        return maintenanceRepository.save(maintenance);
    }

    // Cancel a maintenance task
    public Maintenance cancelMaintenance(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance ID: " + id));

        maintenance.setStatus("CANCELLED");

        return maintenanceRepository.save(maintenance);
    }

    // Private helper methods

    // Change bus status
    private void changeBusStatus(Long busId, Bus.BusStatus status) {
        Optional<Bus> busOptional = busService.getBusById(busId);
        if (busOptional.isPresent()) {
            Bus bus = busOptional.get();
            bus.setStatus(status);
            busService.saveBus(bus);
        }
    }

    // Validate maintenance record
    private void validateMaintenance(Maintenance maintenance) {
        if (maintenance.getBus() == null || maintenance.getBus().getId() == null) {
            throw new IllegalArgumentException("Bus must be selected for maintenance");
        }

        if (maintenance.getScheduledDate() == null) {
            throw new IllegalArgumentException("Scheduled date must be specified");
        }

        if (maintenance.getMaintenanceType() == null || maintenance.getMaintenanceType().isEmpty()) {
            throw new IllegalArgumentException("Maintenance type must be specified");
        }
    }
}