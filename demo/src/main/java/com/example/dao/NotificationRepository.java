package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserId(Integer userId);
    List<Notification> findByTicketId(Integer ticketId);
    List<Notification> findByIsRead(Boolean isRead);
}
