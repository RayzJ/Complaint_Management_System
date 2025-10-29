package com.example.mapper;

import com.example.entity.Assignment;
import com.example.dto.AssignmentDTO;

public class AssignmentMapper {
    public static AssignmentDTO toDto(Assignment e){
        if(e==null) return null;
        AssignmentDTO d = new AssignmentDTO();
        d.setId(e.getId());
        d.setTicketId(e.getTicket()!=null?e.getTicket().getId():null);
        d.setAssignedTo(e.getAssignedTo()!=null?e.getAssignedTo().getId():null);
        d.setAssignedBy(e.getAssignedBy()!=null?e.getAssignedBy().getId():null);
        d.setAssignedAt(e.getAssignedAt());
        d.setNote(e.getNote());
        return d;
    }
    public static Assignment toEntity(AssignmentDTO d){
        if(d==null) return null;
        Assignment a = new Assignment();
        a.setId(d.getId());
        a.setNote(d.getNote());
        return a;
    }
}
