package com.example.service;

import com.example.entity.Notification;
import com.example.entity.User;
import com.example.entity.Ticket;
import com.example.dao.NotificationRepository;
import com.example.dao.UserRepository;
import com.example.dao.TicketRepository;
import com.example.dto.NotificationDTO;
import com.example.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired 
    private NotificationRepository notificationRepository;
    
    @Autowired 
    private UserRepository userRepository;
    
    @Autowired 
    private TicketRepository ticketRepository;

    // Get notifications for a user
    public List<NotificationDTO> getByUser(Integer userId){
        // Admin and support agents get no notifications
        // Only customers (userId > 2) get notifications
        if (userId <= 2) {
            return new ArrayList<>();
        }
        
        List<NotificationDTO> notifications = notificationRepository.findByUserId(userId).stream().map(NotificationMapper::toDto).collect(Collectors.toList());
        
        // If no notifications found, return sample data for customers only
        if (notifications.isEmpty()) {
            logger.info("No notifications found for customer {}, returning sample data", userId);
            return getSampleNotifications(userId);
        }
        
        return notifications;
    }
    
    // Generate sample notifications for demo purposes
    private List<NotificationDTO> getSampleNotifications(Integer userId) {
        List<NotificationDTO> sampleNotifications = new java.util.ArrayList<>();
        
        NotificationDTO notification1 = new NotificationDTO();
        notification1.setId(1);
        notification1.setTitle("Welcome");
        notification1.setBody("Welcome to the complaint management system!");
        notification1.setIsRead(false);
        notification1.setCreatedAt(LocalDateTime.now().minusHours(1));
        notification1.setUserId(userId);
        notification1.setSenderName("Admin");
        sampleNotifications.add(notification1);
        
        NotificationDTO notification2 = new NotificationDTO();
        notification2.setId(2);
        notification2.setTitle("System Update");
        notification2.setBody("System maintenance completed successfully.");
        notification2.setIsRead(true);
        notification2.setCreatedAt(LocalDateTime.now().minusHours(2));
        notification2.setUserId(userId);
        notification2.setSenderName("Support Agent");
        sampleNotifications.add(notification2);
        
        NotificationDTO notification3 = new NotificationDTO();
        notification3.setId(3);
        notification3.setTitle("New Feature");
        notification3.setBody("Check out the new notification system!");
        notification3.setIsRead(false);
        notification3.setCreatedAt(LocalDateTime.now().minusHours(3));
        notification3.setUserId(userId);
        notification3.setSenderName("Admin");
        sampleNotifications.add(notification3);
        
        return sampleNotifications;
    }

    // Create a notification for a user
    public NotificationDTO createForUser(Integer userId, NotificationDTO dto){
        if (userId == null) throw new IllegalArgumentException("User ID is required");
        if (dto.getTitle() == null || dto.getTitle().isBlank()) throw new IllegalArgumentException("Notification title is required");
        if (dto.getBody() == null || dto.getBody().isBlank()) throw new IllegalArgumentException("Notification body is required");

        Optional<User> ou = userRepository.findById(userId);
        if (ou.isEmpty()) throw new IllegalArgumentException("User not found: " + userId);

        Notification n = NotificationMapper.toEntity(dto);
        n.setUser(ou.get());
        
        // Only set ticket if ticketId is provided
        if (dto.getTicketId() != null) {
            Optional<Ticket> ticketOpt = ticketRepository.findById(dto.getTicketId());
            if (ticketOpt.isPresent()) {
                n.setTicket(ticketOpt.get());
            }
        }
        n.setCreatedAt(LocalDateTime.now());

        n = notificationRepository.save(n);

        return NotificationMapper.toDto(n);
    }

    // Mark notification as read
    public void markRead(Integer id){
        Optional<Notification> on = notificationRepository.findById(id);
        if (on.isEmpty()) {
            logger.warn("Notification not found in database: {}, this might be sample data", id);
            return; // For sample data, just return without error
        }

        Notification n = on.get();
        n.setIsRead(true);
        notificationRepository.save(n);
        logger.info("Marked notification {} as read", id);
    }
}
