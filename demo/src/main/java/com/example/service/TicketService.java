package com.example.service;

import com.example.entity.Ticket;
import com.example.entity.User;
import com.example.entity.Assignment;
import com.example.entity.TicketStatusHistory;
import com.example.entity.Notification;
import com.example.dao.AssignmentRepository;
import com.example.dao.NotificationRepository;
import com.example.dao.TicketRepository;
import com.example.dao.TicketStatusHistoryRepository;
import com.example.dao.UserRepository;
import com.example.dto.TicketDTO;
import com.example.mapper.TicketMapper;
import com.example.mapper.AssignmentMapper;
import com.example.mapper.TicketStatusHistoryMapper;
import com.example.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.BadRequestException;
import com.example.exception.DuplicateResourceException;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private TicketStatusHistoryRepository tshRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    // Fetch tickets based on the role (Admin, Support, User)
    public List<TicketDTO> getTicketsByRole(String role, Integer userId) {
        // Normalize the role to uppercase
        role = role.toUpperCase();
        logger.info("Fetching tickets for role: {} with userId: {}", role, userId);

        List<TicketDTO> tickets = new ArrayList<>();

        // Admin should see all tickets
        if ("ADMIN".equals(role)) {
            tickets = ticketRepository.findAll().stream().map(TicketMapper::toDto).collect(Collectors.toList());
            logger.info("Admin fetched {} tickets", tickets.size());
        }
        // Support should see only assigned tickets
        else if ("SUPPORT AGENT".equals(role)) {
            tickets = ticketRepository.findByAssigneeId(userId).stream().map(TicketMapper::toDto)
                    .collect(Collectors.toList());
            logger.info("Support fetched {} tickets for assignee: {}", tickets.size(), userId);
        }
        // Customer should see only their own tickets
        else if ("CUSTOMER".equals(role)) {
            tickets = ticketRepository.findByCustomerId(userId).stream().map(TicketMapper::toDto)
                    .collect(Collectors.toList());
            logger.info("Customer fetched {} tickets for customer: {}", tickets.size(), userId);
        } else {
            logger.error("Unknown role: {}", role);
            throw new IllegalArgumentException("Unknown role: " + role); // For invalid role
        }

        return tickets;
    }

    // Helper method to get customerId from role
    private Integer getCustomerIdFromRole(String role) {
        // You can replace this with actual logic to retrieve the customerId from the
        // logged-in user,
        // typically from JWT or session data
        if ("CUSTOMER".equals(role)) {
            return 5; // Example: return the logged-in customer's ID
        }
        return null;
    }

    public List<TicketDTO> getAll() {
        return ticketRepository.findAll().stream().map(TicketMapper::toDto).collect(Collectors.toList());
    }

    public TicketDTO getById(Integer id) {
        logger.info("Get ticket id={}", id);
        Optional<Ticket> ot = ticketRepository.findById(id);
        if (ot.isEmpty())
            throw new ResourceNotFoundException("Ticket not found: " + id);
        return TicketMapper.toDto(ot.get());
    }

    public TicketDTO create(TicketDTO dto) {
        logger.info("Creating ticket {}", dto.getTitle());
        if (dto.getTitle() == null || dto.getTitle().isBlank())
            throw new BadRequestException("Title is required");
        Ticket t = TicketMapper.toEntity(dto);
        if (dto.getCustomerId() != null) {
            User c = userRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + dto.getCustomerId()));
            t.setCustomer(c);
        }
        t.setReference("TCK-" + System.currentTimeMillis());
        t = ticketRepository.save(t);
        return TicketMapper.toDto(t);
    }

    public TicketDTO update(Integer id, TicketDTO dto) {
        logger.info("Updating ticket id={}", id);
        Optional<Ticket> ot = ticketRepository.findById(id);
        if (ot.isEmpty())
            throw new ResourceNotFoundException("Ticket not found: " + id);
        Ticket t = ot.get();

        // Update fields
        t.setTitle(dto.getTitle());
        t.setDescription(dto.getDescription());
        t.setPriority(dto.getPriority());
        t.setStatus(dto.getStatus());
        t.setAssignee(userRepository.findById(dto.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found")));
        t.setUpdatedAt(LocalDateTime.now()); // Set updatedAt field
        t.setSlaDueAt(dto.getSlaDueAt()); // Manually set SLA if provided
        t.setResolvedAt(dto.getResolvedAt()); // Manually set resolvedAt if provided

        // Save updated ticket
        t = ticketRepository.save(t);
        return TicketMapper.toDto(t);
    }

    public void delete(Integer id) {
        if (!ticketRepository.existsById(id))
            throw new ResourceNotFoundException("Ticket not found: " + id);
        ticketRepository.deleteById(id);
    }

    public TicketDTO assignTicket(Integer ticketId, Integer agentId, Integer assignedById, String note) {
        logger.info("Assign ticket {} to agent {}", ticketId, agentId);
        Optional<Ticket> ot = ticketRepository.findById(ticketId);
        if (ot.isEmpty())
            throw new ResourceNotFoundException("Ticket not found: " + ticketId);
        Optional<User> au = userRepository.findById(agentId);
        if (au.isEmpty())
            throw new ResourceNotFoundException("Agent not found: " + agentId);
        Optional<User> by = userRepository.findById(assignedById);
        if (by.isEmpty())
            throw new ResourceNotFoundException("User not found: " + assignedById);
        Ticket t = ot.get();
        User agent = au.get();

        Assignment a = new Assignment();
        a.setTicket(t);
        a.setAssignedTo(agent);
        a.setAssignedBy(by.get());
        a.setNote(note);
        a.setAssignedAt(LocalDateTime.now());
        assignmentRepository.save(a);

        t.setAssignee(agent);
        t.setStatus("assigned");
        ticketRepository.save(t);

        TicketStatusHistory history = new TicketStatusHistory();
        history.setTicket(t);
        history.setOldStatus("open");
        history.setNewStatus("assigned");
        history.setChangedBy(by.get());
        history.setChangedAt(LocalDateTime.now());
        tshRepository.save(history);

        Notification n = new Notification();
        n.setUser(t.getCustomer());
        n.setTicket(t);
        n.setTitle("Ticket Assigned");
        n.setBody("Your ticket has been assigned to support agent.");
        n.setCreatedAt(LocalDateTime.now());
        n.setIsRead(false);
        notificationRepository.save(n);

        return TicketMapper.toDto(t);
    }

    public TicketDTO changeStatus(Integer ticketId, String newStatus, Integer changedById, String comment) {
        logger.info("Change status ticket {} -> {}", ticketId, newStatus);
        Optional<Ticket> ot = ticketRepository.findById(ticketId);
        if (ot.isEmpty())
            throw new ResourceNotFoundException("Ticket not found: " + ticketId);
        Optional<User> by = userRepository.findById(changedById);
        if (by.isEmpty())
            throw new ResourceNotFoundException("User not found: " + changedById);
        Ticket t = ot.get();
        String old = t.getStatus();
        t.setStatus(newStatus);
        if ("resolved".equalsIgnoreCase(newStatus))
            t.setResolvedAt(java.time.LocalDateTime.now());
        ticketRepository.save(t);

        TicketStatusHistory h = new TicketStatusHistory();
        h.setTicket(t);
        h.setOldStatus(old);
        h.setNewStatus(newStatus);
        h.setChangedBy(by.get());
        h.setComment(comment);
        h.setChangedAt(LocalDateTime.now());
        tshRepository.save(h);

        // Notification part
        Notification notification = new Notification();
        notification.setBody("Ticket status has been updated: " + comment);
        notification.setTicket(t);
        notification.setTitle("Ticket Status Updated");

        // Set the user who should receive the notification (i.e., the customer)
        User customer = t.getCustomer();
        notification.setUser(customer);

        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);
        notificationRepository.save(notification);

        return TicketMapper.toDto(t);
    }

}
