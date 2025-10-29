package com.example.mapper;

import com.example.entity.Attachment;
import com.example.dto.AttachmentDTO;

public class AttachmentMapper {
    public static AttachmentDTO toDto(Attachment e){
        if(e==null) return null;
        AttachmentDTO d = new AttachmentDTO();
        d.setId(e.getId());
        d.setTicketId(e.getTicket()!=null?e.getTicket().getId():null);
        d.setUploadedBy(e.getUploadedBy()!=null?e.getUploadedBy().getId():null);
        d.setFilePath(e.getFilePath());
        d.setFilename(e.getFilename());
        d.setContentType(e.getContentType());
        d.setSizeBytes(e.getSizeBytes());
        d.setCreatedAt(e.getCreatedAt());
        return d;
    }
    public static Attachment toEntity(AttachmentDTO d){
        if(d==null) return null;
        Attachment a = new Attachment();
        a.setId(d.getId());
        a.setFilePath(d.getFilePath());
        a.setFilename(d.getFilename());
        a.setContentType(d.getContentType());
        a.setSizeBytes(d.getSizeBytes());
        return a;
    }
}
