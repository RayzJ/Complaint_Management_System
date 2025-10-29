package com.example.mapper;

import com.example.entity.Notification;
import com.example.dto.NotificationDTO;

public class NotificationMapper {
    public static NotificationDTO toDto(Notification e){
        if(e==null) return null;
        NotificationDTO d = new NotificationDTO();
        d.setId(e.getId());
        d.setUserId(e.getUser()!=null?e.getUser().getId():null);
        d.setTicketId(e.getTicket()!=null?e.getTicket().getId():null);
        d.setTitle(e.getTitle());
        d.setBody(e.getBody());
        d.setIsRead(e.getIsRead());
        d.setCreatedAt(e.getCreatedAt());
        return d;
    }
    public static Notification toEntity(NotificationDTO d){
        if(d==null) return null;
        Notification n = new Notification();
        n.setId(d.getId());
        n.setTitle(d.getTitle());
        n.setBody(d.getBody());
        n.setIsRead(d.getIsRead()!=null?d.getIsRead():false);
        return n;
    }
}
