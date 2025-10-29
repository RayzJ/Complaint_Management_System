package com.example.mapper;

import com.example.entity.TicketStatusHistory;
import com.example.dto.TicketStatusHistoryDTO;

public class TicketStatusHistoryMapper {
    public static TicketStatusHistoryDTO toDto(TicketStatusHistory e){
        if(e==null) return null;
        TicketStatusHistoryDTO d = new TicketStatusHistoryDTO();
        d.setId(e.getId());
        d.setTicketId(e.getTicket()!=null?e.getTicket().getId():null);
        d.setOldStatus(e.getOldStatus());
        d.setNewStatus(e.getNewStatus());
        d.setChangedBy(e.getChangedBy()!=null?e.getChangedBy().getId():null);
        d.setComment(e.getComment());
        d.setChangedAt(e.getChangedAt());
        return d;
    }
    public static TicketStatusHistory toEntity(TicketStatusHistoryDTO d){
        if(d==null) return null;
        TicketStatusHistory t = new TicketStatusHistory();
        t.setId(d.getId());
        t.setOldStatus(d.getOldStatus());
        t.setNewStatus(d.getNewStatus());
        t.setComment(d.getComment());
        return t;
    }
}
