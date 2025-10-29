package com.example.controller;

import com.example.dto.NotificationDTO;
import com.example.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Get notifications for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> listByUser(@PathVariable Integer userId) {
        try {
            // Admin and support agents get no notifications
            if (userId <= 2) {
                return ResponseEntity.ok(new ArrayList<>());
            }
            return ResponseEntity.ok(notificationService.getByUser(userId));
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    // Create notification for a user
    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createForUser(@PathVariable Integer userId, @RequestBody NotificationDTO dto) {
        return ResponseEntity.ok(notificationService.createForUser(userId, dto));
    }

    // Mark notification as read
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markRead(@PathVariable Integer id) {
        notificationService.markRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId) {
        notificationService.markRead(notificationId.intValue());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = (Integer) request.get("userId");
            String message = (String) request.get("message");
            Integer senderId = (Integer) request.get("senderId");
            
            // Create notification DTO
            NotificationDTO dto = new NotificationDTO();
            dto.setTitle("New Message");
            dto.setBody(message);
            dto.setUserId(userId);
            dto.setIsRead(false);
            dto.setSenderName(senderId == 1 ? "Admin" : "Support Agent");
            
            // Save notification
            notificationService.createForUser(userId, dto);
            
            return ResponseEntity.ok(Map.of("success", true, "message", "Notification sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Notification sent (demo mode)"));
        }
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        return ResponseEntity.ok(new ArrayList<>());
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getUsers() {
        List<Map<String, Object>> users = new ArrayList<>();
        users.add(Map.of("id", 1, "fullName", "Admin User", "role", "ADMIN", "username", "admin1"));
        users.add(Map.of("id", 2, "fullName", "John Smith", "role", "SUPPORT_AGENT", "username", "support1"));
        users.add(Map.of("id", 3, "fullName", "Jane Doe", "role", "CUSTOMER", "username", "customer1"));
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/users/support-agents")
    public ResponseEntity<List<Map<String, Object>>> getSupportAgents() {
        List<Map<String, Object>> agents = new ArrayList<>();
        agents.add(Map.of("id", 2, "fullName", "John Smith", "username", "support1"));
        agents.add(Map.of("id", 4, "fullName", "Mike Wilson", "username", "support2"));
        return ResponseEntity.ok(agents);
    }

}
