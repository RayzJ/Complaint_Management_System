# Backend API Endpoints for Notification System

## Required API Endpoints

### 1. Get All Users (for notification sending)
```
GET /api/users
Authorization: Bearer {token}
Response: [
  {
    "id": 1,
    "username": "admin1",
    "fullName": "Admin User",
    "role": "ADMIN"
  },
  {
    "id": 2,
    "username": "support1", 
    "fullName": "Support Agent",
    "role": "SUPPORT_AGENT"
  }
]
```

### 2. Send Notification
```
POST /api/notifications/send
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "userId": 2,
  "message": "Your ticket has been updated",
  "senderId": 1
}

Response: 
{
  "id": 123,
  "message": "Notification sent successfully"
}
```

### 3. Get User Notifications
```
GET /api/notifications/user/{userId}
Authorization: Bearer {token}
Response: [
  {
    "id": 1,
    "message": "Your ticket #123 has been assigned to Agent John",
    "isRead": false,
    "createdAt": "2024-01-15T10:30:00Z",
    "senderId": 2
  }
]
```

### 4. Mark Notification as Read
```
POST /api/notifications/{notificationId}/read
Authorization: Bearer {token}
Response: 
{
  "message": "Notification marked as read"
}
```

### 5. Get Unread Count
```
GET /api/notifications/user/{userId}/unread-count
Authorization: Bearer {token}
Response: 5
```

## Database Schema Suggestions

### Notification Table
```sql
CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    sender_id BIGINT,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (sender_id) REFERENCES users(id)
);
```

## Spring Boot Controller Example

```java
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationRequest request) {
        // Implementation to send notification
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        // Implementation to get user notifications
        return ResponseEntity.ok(notifications);
    }
    
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId) {
        // Implementation to mark as read
        return ResponseEntity.ok().build();
    }
}
```