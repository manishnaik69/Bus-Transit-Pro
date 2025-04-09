package com.busmanagement.service.impl;

import com.busmanagement.model.Bus;
import com.busmanagement.model.Route;
import com.busmanagement.model.Schedule;
import com.busmanagement.model.User;
import com.busmanagement.repository.BusRepository;
import com.busmanagement.repository.RouteRepository;
import com.busmanagement.repository.ScheduleRepository;
import com.busmanagement.repository.UserRepository;
import com.busmanagement.service.ScheduleService;
import com.busmanagement.service.MaintenanceService;
import com.busmanagement.model.MaintenanceRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Implementation of the ScheduleService interface.
 * Provides business logic for schedule operations.
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @Autowired
    private RouteRepository routeRepository;
    
    @Autowired
    private BusRepository busRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MaintenanceService maintenanceService;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Schedule findScheduleById(Long id) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        return schedule.orElse(null);
    }

    @Override
    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public List<Schedule> findSchedulesByRoute(Long routeId) {
        return scheduleRepository.findByRouteId(routeId);
    }

    @Override
    public List<Schedule> findSchedulesByBus(Long busId) {
        return scheduleRepository.findByBusId(busId);
    }

    @Override
    public List<Schedule> findSchedulesByDriverId(Long driverId) {
        // Get current and upcoming schedules for the driver
        List<Schedule> schedules = scheduleRepository.findByDriverId(driverId);
        LocalTime currentTime = LocalTime.now();
        return schedules.stream()
                .filter(s -> s.getDepartureTime().isAfter(currentTime.minusHours(24)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> findAllSchedulesByDriverId(Long driverId) {
        return scheduleRepository.findByDriverId(driverId);
    }

    @Override
    public List<Schedule> findSchedulesByStatus(String status) {
        return scheduleRepository.findByStatus(status);
    }

    @Override
    public List<Schedule> findSchedulesAfterDateTime(LocalDateTime dateTime) {
        // Convert LocalDateTime to LocalTime for querying
        LocalTime time = dateTime.toLocalTime();
        // For implementation purposes, we'll get all schedules and filter by time
        List<Schedule> allSchedules = scheduleRepository.findAll();
        return allSchedules.stream()
                .filter(s -> s.getDepartureTime().isAfter(time))
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> findSchedulesByRouteAndDate(Long routeId, LocalDate date) {
        return scheduleRepository.findByRouteIdAndDepartureDate(routeId, date);
    }

    @Override
    public List<Schedule> findSchedulesByRouteAndDate(Long sourceId, Long destinationId, LocalDate date) {
        return scheduleRepository.findBySourceAndDestinationAndDate(sourceId, destinationId, date);
    }

    @Override
    public Schedule findCurrentScheduleForDriver(Long driverId) {
        List<Schedule> currentSchedules = scheduleRepository.findCurrentScheduleForDriver(driverId);
        return currentSchedules.isEmpty() ? null : currentSchedules.get(0);
    }

    @Override
    @Transactional
    public Schedule saveSchedule(Schedule schedule) {
        // Validate the schedule
        if (!validateSchedule(schedule)) {
            throw new IllegalArgumentException("Invalid schedule details");
        }
        
        // Check if the bus is available for the scheduled time
        Bus bus = schedule.getBus();
        if ("Maintenance".equals(bus.getStatus()) || "Retired".equals(bus.getStatus())) {
            throw new IllegalStateException("Bus is not available for scheduling: " + bus.getStatus());
        }
        
        // Check if the bus is already scheduled for the given time range
        List<Schedule> existingSchedules = scheduleRepository.findByBusId(bus.getId());
        for (Schedule existingSchedule : existingSchedules) {
            if (isTimeOverlap(schedule.getDepartureTime(), schedule.getArrivalTime(), 
                            existingSchedule.getDepartureTime(), existingSchedule.getArrivalTime())) {
                throw new IllegalStateException("Bus is already scheduled during this time period");
            }
        }
        
        // Check if the driver is available for the scheduled time
        User driver = schedule.getDriver();
        List<Schedule> driverSchedules = scheduleRepository.findByDriverId(driver.getId());
        for (Schedule driverSchedule : driverSchedules) {
            if (isTimeOverlap(schedule.getDepartureTime(), schedule.getArrivalTime(), 
                            driverSchedule.getDepartureTime(), driverSchedule.getArrivalTime())) {
                throw new IllegalStateException("Driver is already assigned to another schedule during this time period");
            }
        }
        
        // Initialize seat map if not set
        if (schedule.getSeatMap() == null) {
            initializeSeatMap(schedule);
        }
        
        // Set available seats based on bus capacity if not set
        if (schedule.getAvailableSeats() == null) {
            schedule.setAvailableSeats(bus.getCapacity());
        }
        
        // Set status to "Scheduled" if not set
        if (schedule.getStatus() == null) {
            schedule.setStatus("Scheduled");
        }
        
        return scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public Schedule updateSchedule(Long id, Schedule schedule) {
        // Check if schedule exists
        Schedule existingSchedule = findScheduleById(id);
        if (existingSchedule == null) {
            throw new IllegalArgumentException("Schedule not found with id: " + id);
        }
        
        // Set ID to ensure we're updating the existing record
        schedule.setId(id);
        
        // Validate the schedule
        if (!validateSchedule(schedule)) {
            throw new IllegalArgumentException("Invalid schedule details");
        }
        
        // Check if the bus is available for the scheduled time (if the bus is being changed)
        if (!existingSchedule.getBus().getId().equals(schedule.getBus().getId())) {
            Bus bus = schedule.getBus();
            if ("Maintenance".equals(bus.getStatus()) || "Retired".equals(bus.getStatus())) {
                throw new IllegalStateException("Bus is not available for scheduling: " + bus.getStatus());
            }
            
            // Check if the bus is already scheduled for the given time range
            List<Schedule> busSchedules = scheduleRepository.findByBusId(bus.getId());
            for (Schedule busSchedule : busSchedules) {
                if (!busSchedule.getId().equals(id) && isTimeOverlap(schedule.getDepartureTime(), schedule.getArrivalTime(), 
                                        busSchedule.getDepartureTime(), busSchedule.getArrivalTime())) {
                    throw new IllegalStateException("Bus is already scheduled during this time period");
                }
            }
            
            // Initialize seat map for the new bus
            initializeSeatMap(schedule);
        } else {
            // Keep the existing seat map
            schedule.setSeatMap(existingSchedule.getSeatMap());
        }
        
        // Check if the driver is available (if the driver is being changed)
        if (!existingSchedule.getDriver().getId().equals(schedule.getDriver().getId())) {
            User driver = schedule.getDriver();
            List<Schedule> driverSchedules = scheduleRepository.findByDriverId(driver.getId());
            for (Schedule driverSchedule : driverSchedules) {
                if (!driverSchedule.getId().equals(id) && isTimeOverlap(schedule.getDepartureTime(), schedule.getArrivalTime(), 
                                            driverSchedule.getDepartureTime(), driverSchedule.getArrivalTime())) {
                    throw new IllegalStateException("Driver is already assigned to another schedule during this time period");
                }
            }
        }
        
        return scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public Schedule updateScheduleStatus(Long scheduleId, String status) {
        Schedule schedule = findScheduleById(scheduleId);
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule not found with id: " + scheduleId);
        }
        
        schedule.setStatus(status);
        return scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public Schedule updateBusLocation(Long scheduleId, Double latitude, Double longitude) {
        Schedule schedule = findScheduleById(scheduleId);
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule not found with id: " + scheduleId);
        }
        
        schedule.setCurrentLatitude(latitude);
        schedule.setCurrentLongitude(longitude);
        schedule.setLastUpdated(LocalDateTime.now());
        
        return scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        // Check if schedule exists
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new IllegalArgumentException("Schedule not found with id: " + scheduleId);
        }
        
        // Check if the schedule has any bookings before deleting
        Schedule schedule = findScheduleById(scheduleId);
        if (schedule.getBookings() != null && !schedule.getBookings().isEmpty()) {
            throw new IllegalStateException("Cannot delete schedule as it has associated bookings");
        }
        
        scheduleRepository.deleteById(scheduleId);
    }

    @Override
    public boolean validateSchedule(Schedule schedule) {
        // Check if route is valid
        if (schedule.getRoute() == null || schedule.getRoute().getId() == null) {
            return false;
        }
        
        // Check if bus is valid
        if (schedule.getBus() == null || schedule.getBus().getId() == null) {
            return false;
        }
        
        // Check if driver is valid
        if (schedule.getDriver() == null || schedule.getDriver().getId() == null) {
            return false;
        }
        
        // Check if departure time is valid
        if (schedule.getDepartureTime() == null) {
            return false;
        }
        
        // Check if arrival time is valid and after departure time
        if (schedule.getArrivalTime() == null || !schedule.getArrivalTime().isAfter(schedule.getDepartureTime())) {
            return false;
        }
        
        // Check if available seats is valid
        if (schedule.getAvailableSeats() != null && (schedule.getAvailableSeats() < 0 || 
                                                    schedule.getAvailableSeats() > schedule.getBus().getCapacity())) {
            return false;
        }
        
        // Check if status is valid
        String status = schedule.getStatus();
        if (status != null && (!status.equals("Scheduled") && !status.equals("Cancelled") && 
                              !status.equals("Delayed") && !status.equals("Completed"))) {
            return false;
        }
        
        return true;
    }

    @Override
    public List<Integer> getAvailableSeats(Long scheduleId) {
        Schedule schedule = findScheduleById(scheduleId);
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule not found with id: " + scheduleId);
        }
        
        try {
            // Parse seat map from JSON
            Map<String, String> seatMap = parseSeatMap(schedule.getSeatMap());
            
            // Get available seats
            List<Integer> availableSeats = new ArrayList<>();
            for (String seatNumStr : seatMap.keySet()) {
                if ("AVAILABLE".equals(seatMap.get(seatNumStr))) {
                    availableSeats.add(Integer.parseInt(seatNumStr));
                }
            }
            
            return availableSeats;
            
        } catch (Exception e) {
            throw new RuntimeException("Error parsing seat map: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isSeatAvailable(Long scheduleId, int seatNumber) {
        Schedule schedule = findScheduleById(scheduleId);
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule not found with id: " + scheduleId);
        }
        
        try {
            // Parse seat map from JSON
            Map<String, String> seatMap = parseSeatMap(schedule.getSeatMap());
            
            // Check if seat is available
            return "AVAILABLE".equals(seatMap.get(String.valueOf(seatNumber)));
            
        } catch (Exception e) {
            throw new RuntimeException("Error parsing seat map: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void updateSeatStatus(Long scheduleId, int seatNumber, String status) {
        Schedule schedule = findScheduleById(scheduleId);
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule not found with id: " + scheduleId);
        }
        
        try {
            // Parse seat map from JSON
            Map<String, String> seatMap = parseSeatMap(schedule.getSeatMap());
            
            // Update seat status
            String seatNumStr = String.valueOf(seatNumber);
            if (!seatMap.containsKey(seatNumStr)) {
                throw new IllegalArgumentException("Invalid seat number: " + seatNumber);
            }
            
            String oldStatus = seatMap.get(seatNumStr);
            seatMap.put(seatNumStr, status);
            
            // Update seat map in schedule
            schedule.setSeatMap(objectMapper.writeValueAsString(seatMap));
            
            // Update available seats count
            if ("AVAILABLE".equals(oldStatus) && !"AVAILABLE".equals(status)) {
                schedule.setAvailableSeats(schedule.getAvailableSeats() - 1);
            } else if (!"AVAILABLE".equals(oldStatus) && "AVAILABLE".equals(status)) {
                schedule.setAvailableSeats(schedule.getAvailableSeats() + 1);
            }
            
            scheduleRepository.save(schedule);
            
        } catch (Exception e) {
            throw new RuntimeException("Error updating seat status: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void reportIssue(Long scheduleId, String issueType, String description) {
        Schedule schedule = findScheduleById(scheduleId);
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule not found with id: " + scheduleId);
        }
        
        // Create a maintenance record for the issue
        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
        maintenanceRecord.setBus(schedule.getBus());
        maintenanceRecord.setMaintenanceType(issueType);
        maintenanceRecord.setDescription(description);
        maintenanceRecord.setMaintenanceDate(LocalDate.now());
        maintenanceRecord.setStatus("Scheduled");
        
        maintenanceService.saveMaintenanceRecord(maintenanceRecord);
        
        // Update the schedule status if it's a serious issue
        if ("Emergency".equals(issueType)) {
            schedule.setStatus("Delayed");
            schedule.setRemarks("Emergency maintenance required: " + description);
            scheduleRepository.save(schedule);
            
            // Update bus status
            Bus bus = schedule.getBus();
            bus.setStatus("Maintenance");
            busRepository.save(bus);
        }
    }

    @Override
    public Map<String, Double> getCurrentBusLocation(Long scheduleId) {
        Schedule schedule = findScheduleById(scheduleId);
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule not found with id: " + scheduleId);
        }
        
        Map<String, Double> location = new HashMap<>();
        
        if (schedule.getCurrentLatitude() != null && schedule.getCurrentLongitude() != null) {
            location.put("latitude", schedule.getCurrentLatitude());
            location.put("longitude", schedule.getCurrentLongitude());
        } else {
            // If no location is available, return the source location as default
            // In a real system, this would be the actual coordinates of the source city
            location.put("latitude", 28.7041); // Default latitude (Delhi)
            location.put("longitude", 77.1025); // Default longitude (Delhi)
        }
        
        return location;
    }

    @Override
    public Map<Long, Double> getScheduleOccupancyRates(LocalDateTime startDate, LocalDateTime endDate) {
        // For this implementation, we'll calculate in memory since we've updated the model
        // In a real implementation, we would modify the repository query
        List<Schedule> schedules = scheduleRepository.findAll();
        Map<Long, Double> result = new HashMap<>();
        
        for (Schedule schedule : schedules) {
            // Calculate occupancy rate: (capacity - availableSeats) / capacity
            int capacity = schedule.getBus().getCapacity();
            int availableSeats = schedule.getAvailableSeats();
            double occupancyRate = capacity > 0 ? (double)(capacity - availableSeats) / capacity : 0.0;
            result.put(schedule.getId(), occupancyRate);
        }
        
        return result;
    }
    
    /**
     * Checks if two time periods overlap.
     */
    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }
    
    /**
     * Initializes the seat map for a schedule based on the bus capacity.
     */
    private void initializeSeatMap(Schedule schedule) {
        try {
            Bus bus = schedule.getBus();
            int capacity = bus.getCapacity();
            
            // Create a map with seat numbers as keys and "AVAILABLE" as values
            Map<String, String> seatMap = new HashMap<>();
            for (int i = 1; i <= capacity; i++) {
                seatMap.put(String.valueOf(i), "AVAILABLE");
            }
            
            // Convert map to JSON string
            schedule.setSeatMap(objectMapper.writeValueAsString(seatMap));
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error initializing seat map: " + e.getMessage(), e);
        }
    }
    
    /**
     * Parses the seat map JSON string to a Map.
     */
    private Map<String, String> parseSeatMap(String seatMapJson) throws Exception {
        if (seatMapJson == null || seatMapJson.isEmpty()) {
            return new HashMap<>();
        }
        
        return objectMapper.readValue(seatMapJson, Map.class);
    }
}
