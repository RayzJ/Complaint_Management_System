package com.example.complaintmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    // GET /api/notifications/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserNotifications(@PathVariable Long userId) {
        // Mock data - replace with actual database query
        List<Map<String, Object>> notifications = new ArrayList<>();
        
        Map<String, Object> notification1 = new HashMap<>();
        notification1.put("id", 1);
        notification1.put("message", "Your ticket #123 has been assigned to Support Agent");
        notification1.put("isRead", false);
        notification1.put("createdAt", new Date().toString());
        notification1.put("senderId", 2);
        
        Map<String, Object> notification2 = new HashMap<>();
        notification2.put("id", 2);
        notification2.put("message", "Ticket #124 status updated to In Progress");
        notification2.put("isRead", true);
        notification2.put("createdAt", new Date(System.currentTimeMillis() - 3600000).toString());
        notification2.put("senderId", 1);
        
        notifications.add(notification1);
        notifications.add(notification2);
        
        return ResponseEntity.ok(notifications);
    }

    // POST /api/notifications/{notificationId}/read
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable Long notificationId) {
        // Mock implementation - replace with actual database update
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification marked as read");
        return ResponseEntity.ok(response);
    }

    // POST /api/notifications/send
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendNotification(@RequestBody Map<String, Object> request) {
        // Mock implementation - replace with actual notification creation
        Long userId = Long.valueOf(request.get("userId").toString());
        String message = request.get("message").toString();
        Long senderId = Long.valueOf(request.get("senderId").toString());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification sent successfully");
        return ResponseEntity.ok(response);
    }

    // GET /api/notifications/user/{userId}/unread-count
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Integer> getUnreadCount(@PathVariable Long userId) {
        // Mock implementation - replace with actual count query
        return ResponseEntity.ok(2);
    }
}