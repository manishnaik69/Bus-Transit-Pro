package com.busmanagement.observer;

import com.busmanagement.model.Notification;

/**
 * Observer Pattern interface for notification subscribers.
 * Classes implementing this interface can receive notifications from the NotificationPublisher.
 */
public interface NotificationSubscriber {
    
    /**
     * Called when a new notification is published.
     * 
     * @param notification The notification that was published
     */
    void update(Notification notification);
    
    /**
     * Gets the ID of the user associated with this subscriber.
     * 
     * @return The user ID
     */
    Long getUserId();
    
    /**
     * Gets the role name of the user associated with this subscriber.
     * 
     * @return The role name
     */
    String getRoleName();
}
