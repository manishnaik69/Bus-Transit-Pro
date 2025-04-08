package com.busmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for generating reports.
 */
public interface ReportService {
    
    /**
     * Gets the revenue report for a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Map of dates to revenue amounts
     */
    Map<LocalDate, Double> getRevenueReport(LocalDate startDate, LocalDate endDate);
    
    /**
     * Gets the total revenue for a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Total revenue
     */
    double getTotalRevenue(LocalDate startDate, LocalDate endDate);
    
    /**
     * Gets the daily revenue.
     * 
     * @return Today's revenue
     */
    double getDailyRevenue();
    
    /**
     * Gets the weekly revenue.
     * 
     * @return This week's revenue
     */
    double getWeeklyRevenue();
    
    /**
     * Gets the monthly revenue.
     * 
     * @return This month's revenue
     */
    double getMonthlyRevenue();
    
    /**
     * Gets the occupancy report for a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Map of schedule IDs to occupancy rates
     */
    Map<Long, Double> getOccupancyReport(LocalDate startDate, LocalDate endDate);
    
    /**
     * Gets the average occupancy for a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Average occupancy
     */
    double getAverageOccupancy(LocalDate startDate, LocalDate endDate);
    
    /**
     * Gets the most popular routes.
     * 
     * @return Map of route IDs to booking counts
     */
    Map<Long, Long> getPopularRoutes();
    
    /**
     * Gets the most popular routes for a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Map of route IDs to booking counts
     */
    Map<Long, Long> getPopularRoutes(LocalDate startDate, LocalDate endDate);
    
    /**
     * Gets the maintenance report for a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Map of bus IDs to maintenance counts
     */
    Map<Long, Integer> getMaintenanceReport(LocalDate startDate, LocalDate endDate);
    
    /**
     * Gets the bus with the most maintenance issues for a specific date range.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return Bus ID and issue count
     */
    Map.Entry<Long, Integer> getBusWithMostIssues(LocalDate startDate, LocalDate endDate);
    
    /**
     * Gets the occupancy rates for all buses.
     * 
     * @return Map of bus IDs to occupancy rates
     */
    Map<Long, Double> getBusOccupancyRates();
    
    /**
     * Gets the most recent bookings.
     * 
     * @param limit Maximum number of bookings to return
     * @return List of recent bookings
     */
    List<?> getRecentBookings(int limit);
    
    /**
     * Counts the total number of bookings.
     * 
     * @return Total number of bookings
     */
    long countTotalBookings();
}
