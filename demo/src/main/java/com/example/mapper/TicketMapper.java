package com.example.mapper;

import com.example.entity.Ticket;
import com.example.dto.TicketDTO;

public class TicketMapper {
    public static TicketDTO toDto(Ticket e){
        if(e==null) return null;
        TicketDTO d = new TicketDTO();
        d.setId(e.getId());
        d.setReference(e.getReference());
        d.setTitle(e.getTitle());
        d.setDescription(e.getDescription());
        d.setStatus(e.getStatus());
        d.setPriority(e.getPriority());
        d.setCustomerId(e.getCustomer()!=null?e.getCustomer().getId():null);
        d.setAssigneeId(e.getAssignee()!=null?e.getAssignee().getId():null);
        d.setSlaDueAt(e.getSlaDueAt());
        d.setCreatedAt(e.getCreatedAt());
        d.setUpdatedAt(e.getUpdatedAt());
        d.setResolvedAt(e.getResolvedAt());
        return d;
    }
    public static Ticket toEntity(TicketDTO d){
        if(d==null) return null;
        Ticket t = new Ticket();
        t.setId(d.getId());
        t.setReference(d.getReference());
        t.setTitle(d.getTitle());
        t.setDescription(d.getDescription());
        t.setStatus(d.getStatus()!=null?d.getStatus():"open");
        t.setPriority(d.getPriority()!=null?d.getPriority():"medium");
        t.setSlaDueAt(d.getSlaDueAt());
        t.setCreatedAt(d.getCreatedAt());
        t.setUpdatedAt(d.getUpdatedAt());
        t.setResolvedAt(d.getResolvedAt());
        return t;
    }
}
