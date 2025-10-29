package com.example.service;

import com.example.entity.Comment;
import com.example.entity.Ticket;
import com.example.entity.User;
import com.example.dao.CommentRepository;
import com.example.dao.TicketRepository;
import com.example.dao.UserRepository;
import com.example.dto.CommentDTO;
import com.example.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.BadRequestException;

@Service
@Transactional
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    public java.util.List<CommentDTO> getByTicket(Integer ticketId) {
        return commentRepository.findByTicketId(ticketId).stream().map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    public CommentDTO create(Integer ticketId, Integer authorId, CommentDTO dto) {
        logger.info("Add comment to ticket {}", ticketId);

        // Validate inputs
        if (ticketId == null)
            throw new BadRequestException("Ticket ID is required");
        if (authorId == null)
            throw new BadRequestException("Author ID is required");
        if (dto.getBody() == null || dto.getBody().isBlank())
            throw new BadRequestException("Comment body is required");

        // Retrieve ticket and author
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketId));
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + authorId));

        // Convert DTO to entity
        Comment c = CommentMapper.toEntity(dto);

        // Set ticket and author
        c.setTicket(ticket);
        c.setAuthor(author);

        // Set createdAt timestamp explicitly
        c.setCreatedAt(LocalDateTime.now());

        // Save comment and return DTO
        c = commentRepository.save(c);
        return CommentMapper.toDto(c);
    }

    public void delete(Integer id) {
        if (!commentRepository.existsById(id))
            throw new ResourceNotFoundException("Comment not found: " + id);
        commentRepository.deleteById(id);
    }
}
