package com.example.service;

import com.example.entity.TicketStatusHistory;
import com.example.dao.TicketStatusHistoryRepository;
import com.example.dto.TicketStatusHistoryDTO;
import com.example.mapper.TicketStatusHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;

@Service
@Transactional
public class TicketStatusHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(TicketStatusHistoryService.class);

    @Autowired private TicketStatusHistoryRepository tshRepository;

    public java.util.List<TicketStatusHistoryDTO> getByTicket(Integer ticketId){
        return tshRepository.findByTicketId(ticketId).stream().map(TicketStatusHistoryMapper::toDto).collect(Collectors.toList());
    }

    public TicketStatusHistoryDTO create(TicketStatusHistoryDTO dto){
        if (dto.getTicketId() == null) throw new BadRequestException("Ticket ID is required");
        if (dto.getChangedBy() == null) throw new BadRequestException("Changed by user ID is required");
        if (dto.getNewStatus() == null || dto.getNewStatus().isBlank()) throw new BadRequestException("New status is required");
        if (dto.getOldStatus() == null || dto.getOldStatus().isBlank()) throw new BadRequestException("Old status is required");
        TicketStatusHistory t = TicketStatusHistoryMapper.toEntity(dto);
        t = tshRepository.save(t);
        return TicketStatusHistoryMapper.toDto(t);
    }
}
