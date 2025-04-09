package com.busmanagement.service.impl;

import com.busmanagement.dto.BusDTO;
import com.busmanagement.model.Bus;
import com.busmanagement.repository.BusRepository;
import com.busmanagement.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the BusService interface.
 * Provides business logic for bus operations.
 */
@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;

    @Override
    public Bus findBusById(Long id) {
        Optional<Bus> bus = busRepository.findById(id);
        return bus.orElse(null);
    }

    @Override
    public Bus findBusByRegistrationNumber(String registrationNumber) {
        Optional<Bus> bus = busRepository.findByRegistrationNumber(registrationNumber);
        return bus.orElse(null);
    }

    @Override
    public List<Bus> findAllBuses() {
        return busRepository.findAll();
    }

    @Override
    public List<Bus> findBusesByStatus(String status) {
        return busRepository.findByStatus(status);
    }

    @Override
    public List<Bus> findBusesByType(String type) {
        // Convert string to enum for the repository query
        try {
            Bus.BusType busType = Bus.BusType.valueOf(type);
            return busRepository.findByType(busType);
        } catch (IllegalArgumentException e) {
            // Handle case where the string doesn't match any enum value
            return List.of(); // Return empty list if the type is not valid
        }
    }

    @Override
    public List<Bus> findAvailableBuses() {
        return busRepository.findAvailableBuses();
    }

    @Override
    @Transactional
    public Bus saveBus(BusDTO busDTO) {
        // Check if a bus with the same registration number already exists
        Optional<Bus> existingBus = busRepository.findByRegistrationNumber(busDTO.getRegistrationNumber());
        if (existingBus.isPresent()) {
            throw new IllegalArgumentException("A bus with registration number " + busDTO.getRegistrationNumber() + " already exists");
        }
        
        // Create and validate a new bus entity
        Bus bus = new Bus();
        bus.setRegistrationNumber(busDTO.getRegistrationNumber());
        bus.setModel(busDTO.getModel());
        bus.setCapacity(busDTO.getCapacity());
        // Convert string to enum with error handling
        try {
            bus.setType(Bus.BusType.valueOf(busDTO.getType()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid bus type: " + busDTO.getType());
        }
        bus.setStatus(busDTO.getStatus());
        
        if (!validateBus(bus)) {
            throw new IllegalArgumentException("Invalid bus details");
        }
        
        return busRepository.save(bus);
    }

    @Override
    @Transactional
    public Bus updateBus(Long id, BusDTO busDTO) {
        // Check if the bus exists
        Bus existingBus = findBusById(id);
        if (existingBus == null) {
            throw new IllegalArgumentException("Bus not found with id: " + id);
        }
        
        // Check if the registration number is being changed and if it conflicts with an existing bus
        if (!existingBus.getRegistrationNumber().equals(busDTO.getRegistrationNumber())) {
            Optional<Bus> busWithSameRegNo = busRepository.findByRegistrationNumber(busDTO.getRegistrationNumber());
            if (busWithSameRegNo.isPresent() && !busWithSameRegNo.get().getId().equals(id)) {
                throw new IllegalArgumentException("A bus with registration number " + busDTO.getRegistrationNumber() + " already exists");
            }
        }
        
        // Update and validate the bus entity
        existingBus.setRegistrationNumber(busDTO.getRegistrationNumber());
        existingBus.setModel(busDTO.getModel());
        existingBus.setCapacity(busDTO.getCapacity());
        // Convert string to enum with error handling
        try {
            existingBus.setType(Bus.BusType.valueOf(busDTO.getType()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid bus type: " + busDTO.getType());
        }
        existingBus.setStatus(busDTO.getStatus());
        
        if (!validateBus(existingBus)) {
            throw new IllegalArgumentException("Invalid bus details");
        }
        
        return busRepository.save(existingBus);
    }

    @Override
    @Transactional
    public Bus updateBusStatus(Long busId, String status) {
        Bus bus = findBusById(busId);
        if (bus == null) {
            throw new IllegalArgumentException("Bus not found with id: " + busId);
        }
        
        bus.setStatus(status);
        return busRepository.save(bus);
    }

    @Override
    @Transactional
    public void deleteBus(Long busId) {
        if (!busRepository.existsById(busId)) {
            throw new IllegalArgumentException("Bus not found with id: " + busId);
        }
        
        busRepository.deleteById(busId);
    }

    @Override
    public boolean validateBus(Bus bus) {
        // Check if registration number is valid
        if (bus.getRegistrationNumber() == null || bus.getRegistrationNumber().trim().isEmpty()) {
            return false;
        }
        
        // Check if model is valid
        if (bus.getModel() == null || bus.getModel().trim().isEmpty()) {
            return false;
        }
        
        // Check if capacity is valid
        if (bus.getCapacity() <= 0) {
            return false;
        }
        
        // Check if type is valid - simplified for college project
        if (bus.getType() == null) {
            return false;
        }
        
        // Check if status is valid
        String status = bus.getStatus();
        if (status == null || (!status.equals("Active") && !status.equals("Maintenance") && !status.equals("Retired"))) {
            return false;
        }
        
        return true;
    }

    @Override
    public List<Bus> findBusesNeedingMaintenance() {
        return busRepository.findBusesNeedingMaintenance();
    }

    @Override
    public long countAllBuses() {
        return busRepository.count();
    }

    @Override
    public long countBusesByStatus(String status) {
        return busRepository.countByStatus(status);
    }
}
