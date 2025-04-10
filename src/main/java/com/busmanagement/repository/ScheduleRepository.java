package com.busmanagement.repository;

import com.busmanagement.model.Bus;
import com.busmanagement.model.Route;
import com.busmanagement.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * Repository for Schedule entity
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    /**
     * Find a schedule by its code
     * @param scheduleCode The schedule code
     * @return The schedule if found
     */
    Schedule findByScheduleCode(String scheduleCode);
    
    /**
     * Find schedules by route
     * @param route The route
     * @return List of schedules for the given route
     */
    List<Schedule> findByRoute(Route route);
    
    /**
     * Find schedules by route ID
     * @param routeId The route ID
     * @return List of schedules for the route with the given ID
     */
    List<Schedule> findByRouteId(Long routeId);
    
    /**
     * Find schedules by bus
     * @param bus The bus
     * @return List of schedules for the given bus
     */
    List<Schedule> findByBus(Bus bus);
    
    /**
     * Find schedules by bus ID
     * @param busId The bus ID
     * @return List of schedules for the bus with the given ID
     */
    List<Schedule> findByBusId(Long busId);
    
    /**
     * Find schedules by departure date
     * @param departureDate The departure date
     * @return List of schedules for the given departure date
     */
    List<Schedule> findByDepartureDate(LocalDate departureDate);
    
    /**
     * Find schedules by active status
     * @param isActive The active status
     * @return List of active/inactive schedules
     */
    List<Schedule> findByIsActive(boolean isActive);
    
    /**
     * Find schedules by departure date and active status
     * @param departureDate The departure date
     * @param isActive The active status
     * @return List of active/inactive schedules for the given departure date
     */
    List<Schedule> findByDepartureDateAndIsActive(LocalDate departureDate, boolean isActive);
    
    /**
     * Find schedules by available seats greater than the given value
     * @param minSeats The minimum number of available seats
     * @return List of schedules with available seats > the given value
     */
    List<Schedule> findByAvailableSeatsGreaterThanEqual(int minSeats);
    
    /**
     * Find schedules for a specific departure date and route
     * @param departureDate The departure date
     * @param routeId The route ID
     * @return List of schedules for the given departure date and route
     */
    List<Schedule> findByDepartureDateAndRouteId(LocalDate departureDate, Long routeId);
    
    /**
     * Find schedules between departure date range
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return List of schedules between the given dates
     */
    List<Schedule> findByDepartureDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find schedules between departure time range for a specific date
     * @param departureDate The departure date
     * @param startTime The start time (inclusive)
     * @param endTime The end time (inclusive)
     * @return List of schedules between the given times on the given date
     */
    List<Schedule> findByDepartureDateAndDepartureTimeBetween(LocalDate departureDate, LocalTime startTime, LocalTime endTime);
    
    /**
     * Find schedules by operating days
     * @param dayOfWeek The day of week
     * @return List of schedules operating on the given day
     */
    @Query("SELECT s FROM Schedule s JOIN s.operatingDays d WHERE d = :dayOfWeek")
    List<Schedule> findByOperatingDay(@Param("dayOfWeek") DayOfWeek dayOfWeek);
    
    /**
     * Find schedules for a specific route and date range
     * @param routeId The route ID
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return List of schedules for the given route and date range
     */
    List<Schedule> findByRouteIdAndDepartureDateBetween(Long routeId, LocalDate startDate, LocalDate endDate);
}