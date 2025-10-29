package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Attachment;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    List<Attachment> findByTicketId(Integer ticketId);
    List<Attachment> findByUploadedById(Integer uploadedById);
}
