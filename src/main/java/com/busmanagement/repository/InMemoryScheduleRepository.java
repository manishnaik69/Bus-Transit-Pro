package com.busmanagement.repository;

import com.busmanagement.model.Schedule;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryScheduleRepository {
    private static InMemoryScheduleRepository instance;
    private final Map<Long, Schedule> schedules = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // Change from private to protected constructor
    protected InMemoryScheduleRepository() {
        // Will be populated with sample data in the service layer
    }

    public static synchronized InMemoryScheduleRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryScheduleRepository();
        }
        return instance;
    }

    public List<Schedule> findAll() {
        return new ArrayList<>(schedules.values());
    }

    public Optional<Schedule> findById(Long id) {
        return Optional.ofNullable(schedules.get(id));
    }

    public Schedule save(Schedule schedule) {
        if (schedule.getId() == null) {
            schedule.setId(idCounter.getAndIncrement());
            schedule.setCreatedAt(LocalDateTime.now());
        }
        schedule.setUpdatedAt(LocalDateTime.now());
        schedules.put(schedule.getId(), schedule);
        return schedule;
    }

    public void deleteById(Long id) {
        schedules.remove(id);
    }

    public List<Schedule> findByRouteId(Long routeId) {
        return schedules.values().stream()
                .filter(schedule -> schedule.getRoute().getId().equals(routeId))
                .collect(Collectors.toList());
    }

    public List<Schedule> findByBusId(Long busId) {
        return schedules.values().stream()
                .filter(schedule -> schedule.getBus().getId().equals(busId))
                .collect(Collectors.toList());
    }

    public List<Schedule> findActiveSchedulesForBus(Long busId) {
        LocalDateTime now = LocalDateTime.now();
        return schedules.values().stream()
                .filter(schedule -> schedule.getBus().getId().equals(busId))
                .filter(schedule -> "Scheduled".equals(schedule.getStatus()) ||
                        "In-Progress".equals(schedule.getStatus()))
                .filter(schedule -> schedule.getDepartureTime().isAfter(now) ||
                        schedule.getArrivalTime().isAfter(now))
                .collect(Collectors.toList());
    }

    public boolean isBusScheduledBetween(Long busId, LocalDateTime start, LocalDateTime end) {
        return schedules.values().stream()
                .filter(schedule -> schedule.getBus().getId().equals(busId))
                .filter(schedule -> "Scheduled".equals(schedule.getStatus()) ||
                        "In-Progress".equals(schedule.getStatus()))
                .anyMatch(schedule -> (start.isBefore(schedule.getArrivalTime())
                        && end.isAfter(schedule.getDepartureTime())));
    }
}