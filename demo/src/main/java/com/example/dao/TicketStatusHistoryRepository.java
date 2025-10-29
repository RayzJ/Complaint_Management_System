package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.TicketStatusHistory;

import java.util.List;

public interface TicketStatusHistoryRepository extends JpaRepository<TicketStatusHistory, Integer> {
    List<TicketStatusHistory> findByTicketId(Integer ticketId);
    List<TicketStatusHistory> findByChangedById(Integer changedById);
}
