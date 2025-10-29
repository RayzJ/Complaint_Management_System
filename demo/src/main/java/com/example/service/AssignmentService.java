package com.example.service;

import com.example.entity.Assignment;
import com.example.dao.AssignmentRepository;
import com.example.dto.AssignmentDTO;
import com.example.mapper.AssignmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.BadRequestException;

@Service
@Transactional
public class AssignmentService {
    private static final Logger logger = LoggerFactory.getLogger(AssignmentService.class);

    @Autowired private AssignmentRepository assignmentRepository;

    public java.util.List<AssignmentDTO> getByTicket(Integer ticketId){
        return assignmentRepository.findByTicketId(ticketId).stream().map(AssignmentMapper::toDto).collect(Collectors.toList());
    }

    public AssignmentDTO create(AssignmentDTO dto){
        logger.info("Create assignment for ticket {}", dto.getTicketId());
        if (dto.getTicketId() == null) throw new BadRequestException("Ticket ID is required");
        if (dto.getAssignedBy() == null) throw new BadRequestException("Assigned by user ID is required");
        if (dto.getAssignedTo() == null) throw new BadRequestException("Assigned to user ID is required");
        Assignment a = AssignmentMapper.toEntity(dto);
        a = assignmentRepository.save(a);
        return AssignmentMapper.toDto(a);
    }

    public void delete(Integer id){
        if (!assignmentRepository.existsById(id)) throw new ResourceNotFoundException("Assignment not found: " + id);
        assignmentRepository.deleteById(id);
    }
}
