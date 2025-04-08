package com.busmanagement.repository;

import com.busmanagement.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Notification entities.
 * Provides methods to interact with the notifications table in the database.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * Finds all notifications for a specific user.
     * 
     * @param userId ID of the user
     * @return List of notifications
     */
    List<Notification> findByUserId(Long userId);
    
    /**
     * Finds all unread notifications for a specific user.
     * 
     * @param userId ID of the user
     * @return List of unread notifications
     */
    List<Notification> findByUserIdAndIsReadFalse(Long userId);
    
    /**
     * Finds all notifications for a specific user, ordered by creation date.
     * 
     * @param userId ID of the user
     * @return List of notifications
     */
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    List<Notification> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    /**
     * Counts the number of unread notifications for a specific user.
     * 
     * @param userId ID of the user
     * @return Number of unread notifications
     */
    long countByUserIdAndIsReadFalse(Long userId);
    
    /**
     * Finds all notifications with a specific title.
     * 
     * @param title Title of the notifications
     * @return List of notifications
     */
    List<Notification> findByTitle(String title);
    
    /**
     * Finds all notifications for users with a specific role.
     * 
     * @param roleName Name of the role
     * @return List of notifications
     */
    @Query("SELECT n FROM Notification n JOIN n.user u JOIN u.role r WHERE r.name = :roleName")
    List<Notification> findByUserRole(@Param("roleName") String roleName);
}
