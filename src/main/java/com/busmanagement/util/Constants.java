package com.busmanagement.util;

import java.util.Arrays;
import java.util.List;

/**
 * Constants used throughout the Bus Management System.
 */
public class Constants {
    
    // User Roles
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_PASSENGER = "ROLE_PASSENGER";
    public static final String ROLE_DRIVER = "ROLE_DRIVER";
    public static final String ROLE_MAINTENANCE = "ROLE_MAINTENANCE";
    
    // Booking Status
    public static final String BOOKING_STATUS_PENDING_PAYMENT = "PENDING_PAYMENT";
    public static final String BOOKING_STATUS_PAID = "PAID";
    public static final String BOOKING_STATUS_CANCELLED = "CANCELLED";
    public static final String BOOKING_STATUS_COMPLETED = "COMPLETED";
    
    // Schedule Status
    public static final String SCHEDULE_STATUS_SCHEDULED = "Scheduled";
    public static final String SCHEDULE_STATUS_CANCELLED = "Cancelled";
    public static final String SCHEDULE_STATUS_DELAYED = "Delayed";
    public static final String SCHEDULE_STATUS_COMPLETED = "Completed";
    
    // Bus Status
    public static final String BUS_STATUS_ACTIVE = "Active";
    public static final String BUS_STATUS_MAINTENANCE = "Maintenance";
    public static final String BUS_STATUS_RETIRED = "Retired";
    
    // Bus Types
    public static final String BUS_TYPE_AC = "AC";
    public static final String BUS_TYPE_NON_AC = "Non-AC";
    public static final String BUS_TYPE_SLEEPER = "Sleeper";
    public static final String BUS_TYPE_SEATER = "Seater";
    public static final String BUS_TYPE_SEMI_SLEEPER = "Semi-Sleeper";
    public static final String BUS_TYPE_VOLVO = "Volvo";
    public static final List<String> BUS_TYPES = Arrays.asList(
        BUS_TYPE_AC, BUS_TYPE_NON_AC, BUS_TYPE_SLEEPER, 
        BUS_TYPE_SEATER, BUS_TYPE_SEMI_SLEEPER, BUS_TYPE_VOLVO
    );
    
    // Maintenance Types
    public static final String MAINTENANCE_TYPE_REGULAR = "Regular";
    public static final String MAINTENANCE_TYPE_REPAIR = "Repair";
    public static final String MAINTENANCE_TYPE_EMERGENCY = "Emergency";
    public static final List<String> MAINTENANCE_TYPES = Arrays.asList(
        MAINTENANCE_TYPE_REGULAR, MAINTENANCE_TYPE_REPAIR, MAINTENANCE_TYPE_EMERGENCY
    );
    
    // Maintenance Status
    public static final String MAINTENANCE_STATUS_SCHEDULED = "Scheduled";
    public static final String MAINTENANCE_STATUS_IN_PROGRESS = "In Progress";
    public static final String MAINTENANCE_STATUS_COMPLETED = "Completed";
    
    // Payment Methods
    public static final String PAYMENT_METHOD_CREDIT_CARD = "Credit Card";
    public static final String PAYMENT_METHOD_DEBIT_CARD = "Debit Card";
    public static final String PAYMENT_METHOD_UPI = "UPI";
    public static final String PAYMENT_METHOD_NET_BANKING = "Net Banking";
    public static final List<String> PAYMENT_METHODS = Arrays.asList(
        PAYMENT_METHOD_CREDIT_CARD, PAYMENT_METHOD_DEBIT_CARD, 
        PAYMENT_METHOD_UPI, PAYMENT_METHOD_NET_BANKING
    );
    
    // Payment Status
    public static final String PAYMENT_STATUS_PENDING = "Pending";
    public static final String PAYMENT_STATUS_COMPLETED = "Completed";
    public static final String PAYMENT_STATUS_FAILED = "Failed";
    public static final String PAYMENT_STATUS_REFUNDED = "Refunded";
    
    // Work Order Priority
    public static final String WORK_ORDER_PRIORITY_HIGH = "High";
    public static final String WORK_ORDER_PRIORITY_MEDIUM = "Medium";
    public static final String WORK_ORDER_PRIORITY_LOW = "Low";
    
    // Work Order Status
    public static final String WORK_ORDER_STATUS_PENDING = "Pending";
    public static final String WORK_ORDER_STATUS_IN_PROGRESS = "In Progress";
    public static final String WORK_ORDER_STATUS_COMPLETED = "Completed";
    public static final String WORK_ORDER_STATUS_CANCELLED = "Cancelled";
    
    // Seat Status
    public static final String SEAT_STATUS_AVAILABLE = "AVAILABLE";
    public static final String SEAT_STATUS_BOOKED = "BOOKED";
    public static final String SEAT_STATUS_RESERVED = "RESERVED";
    public static final String SEAT_STATUS_BLOCKED = "BLOCKED";
    
    // Common Indian Cities
    public static final List<String> MAJOR_INDIAN_CITIES = Arrays.asList(
        "Delhi", "Mumbai", "Bangalore", "Hyderabad", "Chennai", 
        "Kolkata", "Ahmedabad", "Pune", "Jaipur", "Lucknow", 
        "Kanpur", "Nagpur", "Visakhapatnam", "Indore", "Thane", 
        "Bhopal", "Patna", "Vadodara", "Ghaziabad", "Ludhiana"
    );
    
    // Indian States
    public static final List<String> INDIAN_STATES = Arrays.asList(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", 
        "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", 
        "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", 
        "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", 
        "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", 
        "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"
    );
    
    // Currency Symbol
    public static final String RUPEE_SYMBOL = "₹";
}
