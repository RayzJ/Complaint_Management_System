package com.example.controller;

import com.example.dto.NotificationDTO;
import com.example.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Get notifications for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> listByUser(@PathVariable Integer userId){
        return ResponseEntity.ok(notificationService.getByUser(userId));
    }

    // Create notification for a user
    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createForUser(@PathVariable Integer userId, @RequestBody NotificationDTO dto){
        return ResponseEntity.ok(notificationService.createForUser(userId, dto));
    }

    // Mark notification as read
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markRead(@PathVariable Integer id){
        notificationService.markRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }
}
