package com.example.service;

import com.example.entity.Attachment;
import com.example.entity.Ticket;
import com.example.entity.User;
import com.example.dao.AttachmentRepository;
import com.example.dao.TicketRepository;
import com.example.dao.UserRepository;
import com.example.dto.AttachmentDTO;
import com.example.mapper.AttachmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.BadRequestException;

@Service
@Transactional
public class AttachmentService {
    private static final Logger logger = LoggerFactory.getLogger(AttachmentService.class);

    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    public java.util.List<AttachmentDTO> getByTicket(Integer ticketId) {
        return attachmentRepository.findByTicketId(ticketId).stream().map(AttachmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public AttachmentDTO create(Integer ticketId, Integer uploadedById, AttachmentDTO dto) {
        logger.info("Add attachment to ticket {}", ticketId);

        // Validate input parameters
        if (ticketId == null)
            throw new BadRequestException("Ticket ID is required");
        if (uploadedById == null)
            throw new BadRequestException("Uploader ID is required");
        if (dto.getFilename() == null || dto.getFilename().isBlank())
            throw new BadRequestException("File name is required");
        if (dto.getFilePath() == null || dto.getFilePath().isBlank())
            throw new BadRequestException("File path is required");
        if (dto.getContentType() == null || dto.getContentType().isBlank())
            throw new BadRequestException("Content type is required");

        // Fetch the Ticket and User from the database
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketId));
        User uploader = userRepository.findById(uploadedById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + uploadedById));

        // Map the DTO to the entity
        Attachment attachment = AttachmentMapper.toEntity(dto);

        // Ensure created_at is set explicitly
        attachment.setCreatedAt(java.time.LocalDateTime.now()); // Set created_at to current time if it's not already
                                                                // set

        // Set other necessary fields
        attachment.setTicket(ticket);
        attachment.setUploadedBy(uploader);

        // Save the attachment
        attachment = attachmentRepository.save(attachment);

        // Return the DTO
        return AttachmentMapper.toDto(attachment);
    }

    public void delete(Integer id) {
        if (!attachmentRepository.existsById(id))
            throw new ResourceNotFoundException("Attachment not found: " + id);
        attachmentRepository.deleteById(id);
    }
}
