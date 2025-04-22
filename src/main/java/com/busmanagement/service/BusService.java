package com.busmanagement.service;

import com.busmanagement.model.Bus;
import com.busmanagement.repository.InMemoryBusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusService {
    private final InMemoryBusRepository busRepository;

    public BusService(InMemoryBusRepository busRepository) {
        this.busRepository = busRepository;
    }

    // Facade method - simplifies access to the repository
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    public Optional<Bus> getBusById(Long id) {
        return busRepository.findById(id);
    }

    public Bus saveBus(Bus bus) {
        return busRepository.save(bus);
    }

    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }

    public List<Bus> getActiveBuses() {
        return busRepository.findByStatus(Bus.BusStatus.ACTIVE);
    }

    public Optional<Bus> findByRegistrationNumber(String registrationNumber) {
        return busRepository.findByRegistrationNumber(registrationNumber);
    }
}