package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByCustomerId(Integer customerId);
    List<Ticket> findByAssigneeId(Integer assigneeId); // This works if you want to find by assignee ID
    
    List<Ticket> findByStatus(String status);
    List<Ticket> findByPriority(String priority);

    // Fetch all tickets (Admin)
    List<Ticket> findAll();

    // Fetch tickets where assignee is not null (Support users' tickets)
    List<Ticket> findByAssigneeIsNotNull();

    // Fetch tickets by customer's username (for "USER" role)
    List<Ticket> findByCustomer_Username(String username);
}
