package com.busmanagement.service;

import com.busmanagement.model.Bus;
import com.busmanagement.model.Route;
import com.busmanagement.model.Schedule;
import com.busmanagement.repository.InMemoryScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final InMemoryScheduleRepository scheduleRepository;
    private final BusService busService;
    private final RouteService routeService;

    @Autowired
    public ScheduleService(InMemoryScheduleRepository scheduleRepository,
            BusService busService,
            RouteService routeService) {
        this.scheduleRepository = scheduleRepository;
        this.busService = busService;
        this.routeService = routeService;

        // Add some sample data if empty
        if (scheduleRepository.findAll().isEmpty()) {
            createSampleSchedules();
        }
    }

    public List<Schedule> findSchedulesByRouteAndDate(Long sourceId, Long destinationId, LocalDate travelDate) {
        // Get the start and end of the day
        LocalDateTime startOfDay = LocalDateTime.of(travelDate, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(travelDate, LocalTime.MAX);

        return scheduleRepository.findAll().stream()
                .filter(schedule -> {
                    // Skip any schedules with null routes
                    if (schedule == null || schedule.getRoute() == null ||
                            schedule.getRoute().getSource() == null ||
                            schedule.getRoute().getDestination() == null) {
                        return false;
                    }

                    Route route = schedule.getRoute();
                    return route.getSource().getId().equals(sourceId) &&
                            route.getDestination().getId().equals(destinationId) &&
                            !schedule.getDepartureTime().isBefore(startOfDay) &&
                            !schedule.getDepartureTime().isAfter(endOfDay);
                })
                .collect(Collectors.toList());
    }

    private void createSampleSchedules() {
        // Get some buses and routes
        List<Bus> activeBuses = busService.getActiveBuses();
        List<Route> routes = routeService.getAllRoutes();

        if (!activeBuses.isEmpty() && !routes.isEmpty()) {
            // Create a sample schedule for tomorrow
            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0);

            Schedule schedule = new Schedule();
            schedule.setBus(activeBuses.get(0));
            schedule.setRoute(routes.get(0));
            schedule.setDepartureTime(tomorrow);
            schedule.setArrivalTime(tomorrow.plusHours(routes.get(0).getDuration() / 60));
            schedule.setDriverName("John Driver");
            schedule.setAvailableSeats(activeBuses.get(0).getCapacity());
            schedule.setStatus("Scheduled");

            scheduleRepository.save(schedule);
        }
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public Schedule saveSchedule(Schedule schedule) throws IllegalStateException {
        validateSchedule(schedule);
        return scheduleRepository.save(schedule);
    }

    private void validateSchedule(Schedule schedule) {
        // 1. Check if bus exists and is active
        Bus bus = schedule.getBus();
        if (bus == null || bus.getId() == null) {
            throw new IllegalStateException("Bus must be selected for schedule");
        }

        // Get fresh bus data
        bus = busService.getBusById(bus.getId())
                .orElseThrow(() -> new IllegalStateException("Selected bus does not exist"));

        // 2. Check bus status
        if (bus.getStatus() == Bus.BusStatus.MAINTENANCE) {
            throw new IllegalStateException("Cannot schedule a bus that is under maintenance");
        }
        if (bus.getStatus() == Bus.BusStatus.RETIRED) {
            throw new IllegalStateException("Cannot schedule a retired bus");
        }

        // 3. Check for scheduling conflicts
        if (schedule.getId() == null) { // Only for new schedules
            boolean busAlreadyScheduled = scheduleRepository.isBusScheduledBetween(
                    bus.getId(), schedule.getDepartureTime(), schedule.getArrivalTime());

            if (busAlreadyScheduled) {
                throw new IllegalStateException("Bus is already scheduled during this time period");
            }
        }

        // 4. Check route exists
        Route route = schedule.getRoute();
        if (route == null || route.getId() == null) {
            throw new IllegalStateException("Route must be selected for schedule");
        }

        routeService.getRouteById(route.getId())
                .orElseThrow(() -> new IllegalStateException("Selected route does not exist"));

        // 5. Validate times
        if (schedule.getDepartureTime() == null || schedule.getArrivalTime() == null) {
            throw new IllegalStateException("Departure and arrival times must be specified");
        }

        if (schedule.getDepartureTime().isAfter(schedule.getArrivalTime())) {
            throw new IllegalStateException("Departure time must be before arrival time");
        }

        if (schedule.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot schedule a bus in the past");
        }
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    public List<Schedule> getSchedulesByRouteId(Long routeId) {
        return scheduleRepository.findByRouteId(routeId);
    }

    public List<Schedule> getSchedulesByBusId(Long busId) {
        return scheduleRepository.findByBusId(busId);
    }

    public List<Bus> getAvailableBusesForTimeSlot(LocalDateTime start, LocalDateTime end, Long excludeScheduleId) {
        // Get all active buses
        List<Bus> activeBuses = busService.getActiveBuses();

        // Filter out buses that are already scheduled during this time period
        return activeBuses.stream()
                .filter(bus -> {
                    // Check if this bus is available during the specified time period
                    return !isAlreadyScheduled(bus.getId(), start, end, excludeScheduleId);
                })
                .collect(Collectors.toList());
    }

    /**
     * Check if a bus is already scheduled during the specified time period
     */
    private boolean isAlreadyScheduled(Long busId, LocalDateTime start, LocalDateTime end, Long excludeScheduleId) {
        List<Schedule> busSchedules = scheduleRepository.findByBusId(busId);

        return busSchedules.stream()
                .filter(schedule -> !schedule.getId().equals(excludeScheduleId)) // Exclude current schedule being
                                                                                 // edited
                .filter(schedule -> "Scheduled".equals(schedule.getStatus())
                        || "In-Progress".equals(schedule.getStatus()))
                .anyMatch(schedule -> (start.isBefore(schedule.getArrivalTime())
                        && end.isAfter(schedule.getDepartureTime())));
    }
}