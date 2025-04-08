package com.busmanagement.service;

import com.busmanagement.model.Notification;

import java.util.List;

/**
 * Service interface for managing notification operations.
 */
public interface NotificationService {
    
    /**
     * Finds a notification by its ID.
     * 
     * @param id ID of the notification
     * @return The notification, or null if not found
     */
    Notification findNotificationById(Long id);
    
    /**
     * Finds all notifications for a specific user.
     * 
     * @param userId ID of the user
     * @return List of notifications
     */
    List<Notification> findAllNotificationsByUserId(Long userId);
    
    /**
     * Finds all unread notifications for a specific user.
     * 
     * @param userId ID of the user
     * @return List of unread notifications
     */
    List<Notification> findUnreadNotificationsByUserId(Long userId);
    
    /**
     * Creates and saves a new notification.
     * 
     * @param notification The notification to save
     * @return The saved notification
     */
    Notification createNotification(Notification notification);
    
    /**
     * Marks a notification as read.
     * 
     * @param notificationId ID of the notification
     * @return The updated notification
     */
    Notification markNotificationAsRead(Long notificationId);
    
    /**
     * Marks all notifications for a specific user as read.
     * 
     * @param userId ID of the user
     */
    void markAllNotificationsAsRead(Long userId);
    
    /**
     * Deletes a notification.
     * 
     * @param notificationId ID of the notification to delete
     */
    void deleteNotification(Long notificationId);
    
    /**
     * Counts the number of unread notifications for a specific user.
     * 
     * @param userId ID of the user
     * @return Number of unread notifications
     */
    long countUnreadNotifications(Long userId);
    
    /**
     * Sends a notification to a specific user.
     * 
     * @param userId ID of the user
     * @param title Title of the notification
     * @param message Content of the notification
     * @return The created notification
     */
    Notification sendNotificationToUser(Long userId, String title, String message);
    
    /**
     * Sends a notification to all users with a specific role.
     * 
     * @param roleName Name of the role
     * @param title Title of the notification
     * @param message Content of the notification
     * @return List of created notifications
     */
    List<Notification> sendNotificationToRole(String roleName, String title, String message);
}
