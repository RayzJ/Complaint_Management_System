package com.example.mapper;

import com.example.entity.Comment;
import com.example.dto.CommentDTO;

public class CommentMapper {
    public static CommentDTO toDto(Comment e){
        if(e==null) return null;
        CommentDTO d = new CommentDTO();
        d.setId(e.getId());
        d.setTicketId(e.getTicket()!=null?e.getTicket().getId():null);
        d.setAuthorId(e.getAuthor()!=null?e.getAuthor().getId():null);
        d.setBody(e.getBody());
        d.setIsInternal(e.getIsInternal());
        d.setCreatedAt(e.getCreatedAt());
        d.setEditedAt(e.getEditedAt());
        return d;
    }
    public static Comment toEntity(CommentDTO d){
        if(d==null) return null;
        Comment c = new Comment();
        c.setId(d.getId());
        c.setBody(d.getBody());
        c.setIsInternal(d.getIsInternal()!=null?d.getIsInternal():false);
        return c;
    }
}
