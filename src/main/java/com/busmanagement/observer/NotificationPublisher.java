package com.busmanagement.observer;

import com.busmanagement.model.Notification;
import com.busmanagement.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern implementation for notification publishing.
 * This class acts as the subject in the Observer pattern, notifying subscribers
 * when new notifications are created.
 */
@Component
public class NotificationPublisher {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    private List<NotificationSubscriber> subscribers = new ArrayList<>();
    
    /**
     * Registers a subscriber to receive notifications.
     * 
     * @param subscriber The subscriber to register
     */
    public void subscribe(NotificationSubscriber subscriber) {
        subscribers.add(subscriber);
    }
    
    /**
     * Unregisters a subscriber from receiving notifications.
     * 
     * @param subscriber The subscriber to unregister
     */
    public void unsubscribe(NotificationSubscriber subscriber) {
        subscribers.remove(subscriber);
    }
    
    /**
     * Publishes a notification to all subscribers and saves it to the database.
     * 
     * @param notification The notification to publish
     * @return The saved notification
     */
    public Notification publishNotification(Notification notification) {
        // Save notification to database
        Notification savedNotification = notificationRepository.save(notification);
        
        // Notify all subscribers
        for (NotificationSubscriber subscriber : subscribers) {
            subscriber.update(savedNotification);
        }
        
        return savedNotification;
    }
    
    /**
     * Publishes a notification to a specific user.
     * 
     * @param notification The notification to publish
     * @param userId The ID of the user to receive the notification
     * @return The saved notification
     */
    public Notification publishNotificationToUser(Notification notification, Long userId) {
        // Save notification to database
        Notification savedNotification = notificationRepository.save(notification);
        
        // Notify only subscribers for this specific user
        for (NotificationSubscriber subscriber : subscribers) {
            if (subscriber.getUserId().equals(userId)) {
                subscriber.update(savedNotification);
            }
        }
        
        return savedNotification;
    }
    
    /**
     * Publishes a notification to users with a specific role.
     * 
     * @param notification The notification to publish
     * @param roleName The name of the role to receive the notification
     * @return The saved notification
     */
    public Notification publishNotificationToRole(Notification notification, String roleName) {
        // Save notification to database
        Notification savedNotification = notificationRepository.save(notification);
        
        // Notify only subscribers with the specific role
        for (NotificationSubscriber subscriber : subscribers) {
            if (subscriber.getRoleName().equals(roleName)) {
                subscriber.update(savedNotification);
            }
        }
        
        return savedNotification;
    }
}
