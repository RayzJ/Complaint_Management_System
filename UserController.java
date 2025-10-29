package com.example.complaintmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    // GET /api/users - Get all users for notification sending
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        // Mock data - replace with actual database query
        List<Map<String, Object>> users = new ArrayList<>();
        
        Map<String, Object> user1 = new HashMap<>();
        user1.put("id", 1);
        user1.put("fullName", "Admin User");
        user1.put("username", "admin1");
        user1.put("role", "ADMIN");
        
        Map<String, Object> user2 = new HashMap<>();
        user2.put("id", 2);
        user2.put("fullName", "Support Agent 1");
        user2.put("username", "support1");
        user2.put("role", "SUPPORT_AGENT");
        
        Map<String, Object> user3 = new HashMap<>();
        user3.put("id", 3);
        user3.put("fullName", "Customer User");
        user3.put("username", "customer1");
        user3.put("role", "CUSTOMER");
        
        users.add(user1);
        users.add(user2);
        users.add(user3);
        
        return ResponseEntity.ok(users);
    }

    // GET /api/users/support-agents - Get only support agents for ticket assignment
    @GetMapping("/support-agents")
    public ResponseEntity<List<Map<String, Object>>> getSupportAgents() {
        // Mock data - replace with actual database query filtering by role
        List<Map<String, Object>> agents = new ArrayList<>();
        
        Map<String, Object> agent1 = new HashMap<>();
        agent1.put("id", 2);
        agent1.put("fullName", "Support Agent 1");
        agent1.put("username", "support1");
        agent1.put("role", "SUPPORT_AGENT");
        
        Map<String, Object> agent2 = new HashMap<>();
        agent2.put("id", 4);
        agent2.put("fullName", "Support Agent 2");
        agent2.put("username", "support2");
        agent2.put("role", "SUPPORT_AGENT");
        
        agents.add(agent1);
        agents.add(agent2);
        
        return ResponseEntity.ok(agents);
    }
}