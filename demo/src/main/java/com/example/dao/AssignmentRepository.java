package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Assignment;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    List<Assignment> findByTicketId(Integer ticketId);
    List<Assignment> findByAssignedToId(Integer assignedToId);
    List<Assignment> findByAssignedById(Integer assignedById);
}
