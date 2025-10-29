package com.example.controller;

import com.example.dto.TicketDTO;
import com.example.service.TicketService;

import jakarta.servlet.http.HttpServletRequest;

import com.example.security.JwtUtil;  // Import JwtUtil for JWT parsing
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private JwtUtil jwtUtil;  // Inject JwtUtil for extracting claims from the JWT token

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TicketDTO dto) {
        TicketDTO created = ticketService.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> list(@RequestParam String role, HttpServletRequest request) {
        if (role == null || role.isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Handle missing role gracefully
        }

        // Extract the JWT token from the request
        String token = request.getHeader("Authorization").substring(7);  // Remove "Bearer " prefix

        // Extract the user ID and role from the JWT token
        Integer userId = jwtUtil.extractUserId(token);  
        String userRole = jwtUtil.extractRole(token).toLowerCase();  // Normalize the role to lowercase

        System.out.println("Received role parameter: " + role);  // Log the received role parameter
        System.out.println("Fetched role from token: " + userRole); // Log the role extracted from the token

        // Ensure the role from the request matches the role in the token (case-insensitive)
       if (!role.toLowerCase().equals(userRole.toLowerCase())) {
            // Instead of returning a string, return a response with an empty list and 403 status code
            return ResponseEntity.status(403).body(List.of());  // Return empty list for role mismatch
        }

        // Pass both role and userId to the service method
        List<TicketDTO> tickets = ticketService.getTicketsByRole(role, userId);

        // Log the number of tickets fetched
        System.out.println("Fetched " + tickets.size() + " tickets for role: " + role);

        return ResponseEntity.ok(tickets); // Return the list of tickets
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        return ResponseEntity.ok(ticketService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody TicketDTO dto) {
        TicketDTO updatedTicket = ticketService.update(id, dto);
        return ResponseEntity.ok(updatedTicket);
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<?> assign(@PathVariable Integer id, @RequestParam Integer agentId,
            @RequestParam Integer assignedById) {
        TicketDTO t = ticketService.assignTicket(id, agentId, assignedById, null);
        if (t == null)
            return ResponseEntity.badRequest().body("assign_failed");
        return ResponseEntity.ok(t);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable Integer id, @RequestParam String status,
            @RequestParam Integer changedById) {
        TicketDTO t = ticketService.changeStatus(id, status, changedById, null);
        if (t == null)
            return ResponseEntity.badRequest().body("status_change_failed");
        return ResponseEntity.ok(t);
    }
}
