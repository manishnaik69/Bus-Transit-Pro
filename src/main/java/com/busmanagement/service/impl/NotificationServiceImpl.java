package com.busmanagement.service.impl;

import com.busmanagement.model.Notification;
import com.busmanagement.model.User;
import com.busmanagement.observer.NotificationPublisher;
import com.busmanagement.repository.NotificationRepository;
import com.busmanagement.repository.UserRepository;
import com.busmanagement.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the NotificationService interface.
 * Provides business logic for notification operations.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NotificationPublisher notificationPublisher;

    @Override
    public Notification findNotificationById(Long id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        return notification.orElse(null);
    }

    @Override
    public List<Notification> findAllNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Notification> findUnreadNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public Notification createNotification(Notification notification) {
        // Validate the notification
        if (!isValidNotification(notification)) {
            throw new IllegalArgumentException("Invalid notification details");
        }
        
        // Set creation time if not set
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }
        
        // Set read status to false by default
        notification.setRead(false);
        
        // Save the notification and publish it to subscribers
        return notificationPublisher.publishNotification(notification);
    }

    @Override
    @Transactional
    public Notification markNotificationAsRead(Long notificationId) {
        Notification notification = findNotificationById(notificationId);
        if (notification == null) {
            throw new IllegalArgumentException("Notification not found with id: " + notificationId);
        }
        
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllNotificationsAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
        }
        
        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new IllegalArgumentException("Notification not found with id: " + notificationId);
        }
        
        notificationRepository.deleteById(notificationId);
    }

    @Override
    public long countUnreadNotifications(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public Notification sendNotificationToUser(Long userId, String title, String message) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        User user = userOptional.get();
        
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        
        return notificationPublisher.publishNotification(notification);
    }

    @Override
    @Transactional
    public List<Notification> sendNotificationToRole(String roleName, String title, String message) {
        List<User> users = userRepository.findByRoleName(roleName);
        List<Notification> sentNotifications = new ArrayList<>();
        
        for (User user : users) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setRead(false);
            
            sentNotifications.add(notificationPublisher.publishNotification(notification));
        }
        
        return sentNotifications;
    }
    
    /**
     * Validates a notification entity.
     * 
     * @param notification The notification to validate
     * @return true if the notification is valid, false otherwise
     */
    private boolean isValidNotification(Notification notification) {
        // Check if user is valid
        if (notification.getUser() == null || notification.getUser().getId() == null) {
            return false;
        }
        
        // Check if title is valid
        if (notification.getTitle() == null || notification.getTitle().trim().isEmpty()) {
            return false;
        }
        
        // Check if message is valid
        if (notification.getMessage() == null || notification.getMessage().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
}
