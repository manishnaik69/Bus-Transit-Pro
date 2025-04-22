package com.busmanagement.repository;

import com.busmanagement.model.Bus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryBusRepository {
    private static InMemoryBusRepository instance;
    private final Map<Long, Bus> buses = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    protected InMemoryBusRepository() {
        // Add some sample data
        Bus bus1 = new Bus();
        bus1.setId(idCounter.getAndIncrement());
        bus1.setRegistrationNumber("BUS-001");
        bus1.setManufacturer("Mercedes");
        bus1.setModel("Sprinter");
        bus1.setCapacity(40);
        bus1.setStatus(Bus.BusStatus.ACTIVE);
        bus1.setCreatedAt(LocalDateTime.now());
        buses.put(bus1.getId(), bus1);

        Bus bus2 = new Bus();
        bus2.setId(idCounter.getAndIncrement());
        bus2.setRegistrationNumber("BUS-002");
        bus2.setManufacturer("Volvo");
        bus2.setModel("9700");
        bus2.setCapacity(60);
        bus2.setStatus(Bus.BusStatus.ACTIVE);
        bus2.setCreatedAt(LocalDateTime.now());
        buses.put(bus2.getId(), bus2);
    }

    // Singleton getInstance method
    public static synchronized InMemoryBusRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryBusRepository();
        }
        return instance;
    }

    public List<Bus> findAll() {
        return new ArrayList<>(buses.values());
    }

    public Optional<Bus> findById(Long id) {
        return Optional.ofNullable(buses.get(id));
    }

    public Bus save(Bus bus) {
        if (bus.getId() == null) {
            bus.setId(idCounter.getAndIncrement());
            bus.setCreatedAt(LocalDateTime.now());
        }
        buses.put(bus.getId(), bus);
        return bus;
    }

    public void deleteById(Long id) {
        buses.remove(id);
    }

    public Optional<Bus> findByRegistrationNumber(String registrationNumber) {
        return buses.values().stream()
                .filter(b -> b.getRegistrationNumber().equals(registrationNumber))
                .findFirst();
    }

    public List<Bus> findByStatus(Bus.BusStatus status) {
        return buses.values().stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }
}