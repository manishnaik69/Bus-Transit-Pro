package com.busmanagement.service;

import com.busmanagement.model.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing schedule operations.
 */
public interface ScheduleService {
    
    /**
     * Finds a schedule by its ID.
     * 
     * @param id ID of the schedule
     * @return The schedule, or null if not found
     */
    Schedule findScheduleById(Long id);
    
    /**
     * Finds all schedules in the system.
     * 
     * @return List of all schedules
     */
    List<Schedule> findAllSchedules();
    
    /**
     * Finds all schedules for a specific route.
     * 
     * @param routeId ID of the route
     * @return List of schedules
     */
    List<Schedule> findSchedulesByRoute(Long routeId);
    
    /**
     * Finds all schedules for a specific bus.
     * 
     * @param busId ID of the bus
     * @return List of schedules
     */
    List<Schedule> findSchedulesByBus(Long busId);
    
    /**
     * Finds all schedules for a specific driver.
     * 
     * @param driverId ID of the driver
     * @return List of schedules
     */
    List<Schedule> findSchedulesByDriverId(Long driverId);
    
    /**
     * Finds all schedules for a specific driver.
     * 
     * @param driverId ID of the driver
     * @return List of schedules
     */
    List<Schedule> findAllSchedulesByDriverId(Long driverId);
    
    /**
     * Finds all schedules with a specific status.
     * 
     * @param status Status of the schedules
     * @return List of schedules
     */
    List<Schedule> findSchedulesByStatus(String status);
    
    /**
     * Finds all schedules after a specific date/time.
     * 
     * @param dateTime Minimum date/time
     * @return List of schedules
     */
    List<Schedule> findSchedulesAfterDateTime(LocalDateTime dateTime);
    
    /**
     * Finds all schedules for a specific route and date.
     * 
     * @param routeId ID of the route
     * @param date Date of departure
     * @return List of schedules
     */
    List<Schedule> findSchedulesByRouteAndDate(Long routeId, LocalDate date);
    
    /**
     * Finds all schedules between specific source and destination cities on a specific date.
     * 
     * @param sourceId ID of the source city
     * @param destinationId ID of the destination city
     * @param date Date of departure
     * @return List of schedules
     */
    List<Schedule> findSchedulesByRouteAndDate(Long sourceId, Long destinationId, LocalDate date);
    
    /**
     * Finds the current schedule for a specific driver.
     * 
     * @param driverId ID of the driver
     * @return Current schedule, or null if not found
     */
    Schedule findCurrentScheduleForDriver(Long driverId);
    
    /**
     * Saves a new schedule.
     * 
     * @param schedule The schedule to save
     * @return The saved schedule
     */
    Schedule saveSchedule(Schedule schedule);
    
    /**
     * Updates an existing schedule.
     * 
     * @param id ID of the schedule to update
     * @param schedule The updated schedule data
     * @return The updated schedule
     */
    Schedule updateSchedule(Long id, Schedule schedule);
    
    /**
     * Updates the status of a schedule.
     * 
     * @param scheduleId ID of the schedule
     * @param status New status
     * @return The updated schedule
     */
    Schedule updateScheduleStatus(Long scheduleId, String status);
    
    /**
     * Updates the location of a bus for a specific schedule.
     * 
     * @param scheduleId ID of the schedule
     * @param latitude Current latitude
     * @param longitude Current longitude
     * @return The updated schedule
     */
    Schedule updateBusLocation(Long scheduleId, Double latitude, Double longitude);
    
    /**
     * Deletes a schedule.
     * 
     * @param scheduleId ID of the schedule to delete
     */
    void deleteSchedule(Long scheduleId);
    
    /**
     * Validates a schedule to ensure it meets all business rules.
     * 
     * @param schedule The schedule to validate
     * @return true if the schedule is valid, false otherwise
     */
    boolean validateSchedule(Schedule schedule);
    
    /**
     * Gets the available seats for a specific schedule.
     * 
     * @param scheduleId ID of the schedule
     * @return List of available seat numbers
     */
    List<Integer> getAvailableSeats(Long scheduleId);
    
    /**
     * Checks if a specific seat is available for a specific schedule.
     * 
     * @param scheduleId ID of the schedule
     * @param seatNumber Seat number to check
     * @return true if the seat is available, false otherwise
     */
    boolean isSeatAvailable(Long scheduleId, int seatNumber);
    
    /**
     * Updates the status of a seat for a specific schedule.
     * 
     * @param scheduleId ID of the schedule
     * @param seatNumber Seat number to update
     * @param status New status (e.g., "AVAILABLE", "BOOKED")
     */
    void updateSeatStatus(Long scheduleId, int seatNumber, String status);
    
    /**
     * Reports an issue for a specific schedule.
     * 
     * @param scheduleId ID of the schedule
     * @param issueType Type of issue
     * @param description Description of the issue
     */
    void reportIssue(Long scheduleId, String issueType, String description);
    
    /**
     * Gets the current location of a bus for a specific schedule.
     * 
     * @param scheduleId ID of the schedule
     * @return Map containing latitude and longitude
     */
    Map<String, Double> getCurrentBusLocation(Long scheduleId);
    
    /**
     * Gets the occupancy rate for each schedule within a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Map of schedule IDs to occupancy rates
     */
    Map<Long, Double> getScheduleOccupancyRates(LocalDateTime startDate, LocalDateTime endDate);
}
