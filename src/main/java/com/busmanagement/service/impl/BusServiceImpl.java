package com.busmanagement.service.impl;

import com.busmanagement.model.Bus;
import com.busmanagement.repository.BusRepository;
import com.busmanagement.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of BusService interface
 */
@Service
public class BusServiceImpl implements BusService {
    
    private final BusRepository busRepository;
    
    @Autowired
    public BusServiceImpl(BusRepository busRepository) {
        this.busRepository = busRepository;
    }
    
    @Override
    public List<Bus> findAllBuses() {
        return busRepository.findAll();
    }
    
    @Override
    public Bus findBusById(Long id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bus not found with id: " + id));
    }
    
    @Override
    public Bus findBusByRegistrationNumber(String registrationNumber) {
        return busRepository.findByRegistrationNumber(registrationNumber);
    }
    
    @Override
    public Bus saveBus(Bus bus) {
        // Check if a bus with the same registration number already exists
        Bus existingBus = busRepository.findByRegistrationNumber(bus.getRegistrationNumber());
        if (existingBus != null && !existingBus.getId().equals(bus.getId())) {
            throw new IllegalArgumentException("A bus with registration number " + bus.getRegistrationNumber() + " already exists");
        }
        
        return busRepository.save(bus);
    }
    
    @Override
    public Bus updateBus(Long id, Bus busDetails) {
        Bus bus = findBusById(id);
        
        // Update bus properties
        bus.setRegistrationNumber(busDetails.getRegistrationNumber());
        bus.setManufacturer(busDetails.getManufacturer());
        bus.setModel(busDetails.getModel());
        bus.setYearManufactured(busDetails.getYearManufactured());
        bus.setBusType(busDetails.getBusType());
        bus.setCapacity(busDetails.getCapacity());
        bus.setStatus(busDetails.getStatus());
        bus.setFuelType(busDetails.getFuelType());
        bus.setFuelEfficiency(busDetails.getFuelEfficiency());
        bus.setNotes(busDetails.getNotes());
        
        if (busDetails.getLastMaintenanceDate() != null) {
            bus.setLastMaintenanceDate(busDetails.getLastMaintenanceDate());
        }
        
        return busRepository.save(bus);
    }
    
    @Override
    public void deleteBus(Long id) {
        Bus bus = findBusById(id);
        
        // Check if the bus has any associated schedules
        if (bus.getSchedules() != null && !bus.getSchedules().isEmpty()) {
            throw new IllegalStateException("Cannot delete bus as it has associated schedules");
        }
        
        busRepository.delete(bus);
    }
    
    @Override
    public List<Bus> findBusesByStatus(Bus.BusStatus status) {
        return busRepository.findByStatus(status);
    }
    
    @Override
    public long countBusesByStatus(Bus.BusStatus status) {
        return busRepository.countByStatus(status);
    }
    
    @Override
    public List<Bus> findBusesByType(Bus.BusType busType) {
        return busRepository.findByBusType(busType);
    }
    
    @Override
    public Bus changeBusStatus(Long id, Bus.BusStatus status) {
        Bus bus = findBusById(id);
        
        // Update bus status
        bus.setStatus(status);
        
        // If changing to maintenance, update the last maintenance date
        if (status == Bus.BusStatus.MAINTENANCE) {
            bus.setLastMaintenanceDate(LocalDateTime.now());
        }
        
        return busRepository.save(bus);
    }
}