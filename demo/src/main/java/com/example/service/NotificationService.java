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
        return notificationRepository.findByUserId(userId).stream().map(NotificationMapper::toDto).collect(Collectors.toList());
    }

    // Create a notification for a user
    public NotificationDTO createForUser(Integer userId, NotificationDTO dto){
        if (userId == null) throw new IllegalArgumentException("User ID is required");
        if (dto.getTitle() == null || dto.getTitle().isBlank()) throw new IllegalArgumentException("Notification title is required");
        if (dto.getBody() == null || dto.getBody().isBlank()) throw new IllegalArgumentException("Notification body is required");

        Optional<User> ou = userRepository.findById(userId);
        if (ou.isEmpty()) throw new IllegalArgumentException("User not found: " + userId);

        Optional<Ticket> ticketOpt = ticketRepository.findById(dto.getTicketId());
        if (ticketOpt.isEmpty()) {
            throw new IllegalArgumentException("Ticket not found: " + dto.getTicketId());
        }

        Notification n = NotificationMapper.toEntity(dto);

        n.setUser(ou.get());
        n.setTicket(ticketOpt.get());
        n.setCreatedAt(LocalDateTime.now());

        n = notificationRepository.save(n);

        return NotificationMapper.toDto(n);
    }

    // Mark notification as read
    public void markRead(Integer id){
        Optional<Notification> on = notificationRepository.findById(id);
        if (on.isEmpty()) throw new IllegalArgumentException("Notification not found: " + id);

        Notification n = on.get();
        n.setIsRead(true);
        notificationRepository.save(n);
    }
}
