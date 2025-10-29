package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<Map<String, Object>> users = new ArrayList<>();
        users.add(Map.of("id", 1, "fullName", "Admin User", "role", "ADMIN", "username", "admin1"));
        users.add(Map.of("id", 2, "fullName", "John Smith", "role", "SUPPORT_AGENT", "username", "support1"));
        users.add(Map.of("id", 3, "fullName", "Jane Doe", "role", "CUSTOMER", "username", "customer1"));
        users.add(Map.of("id", 4, "fullName", "Mike Wilson", "role", "SUPPORT_AGENT", "username", "support2"));
        users.add(Map.of("id", 5, "fullName", "Sarah Johnson", "role", "CUSTOMER", "username", "customer2"));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/support-agents")
    public ResponseEntity<List<Map<String, Object>>> getSupportAgents() {
        List<Map<String, Object>> agents = new ArrayList<>();
        agents.add(Map.of("id", 2, "fullName", "John Smith", "username", "support1"));
        agents.add(Map.of("id", 4, "fullName", "Mike Wilson", "username", "support2"));
        return ResponseEntity.ok(agents);
    }
}