package com.busmanagement.service;

import com.busmanagement.model.SimpleBus;
import com.busmanagement.repository.SimpleBusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleBusService {
    
    private final SimpleBusRepository busRepository;
    
    @Autowired
    public SimpleBusService(SimpleBusRepository busRepository) {
        this.busRepository = busRepository;
    }
    
    public List<SimpleBus> getAllBuses() {
        return busRepository.findAll();
    }
    
    public Optional<SimpleBus> getBusById(Long id) {
        return busRepository.findById(id);
    }
    
    public SimpleBus saveBus(SimpleBus bus) {
        return busRepository.save(bus);
    }
    
    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }
    
    public List<SimpleBus> getActiveBuses() {
        return busRepository.findByStatus(SimpleBus.BusStatus.ACTIVE);
    }
    
    public Optional<SimpleBus> findByRegistrationNumber(String registrationNumber) {
        return busRepository.findByRegistrationNumber(registrationNumber);
    }
}