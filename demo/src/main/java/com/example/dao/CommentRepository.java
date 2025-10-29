package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByTicketId(Integer ticketId);
    List<Comment> findByAuthorId(Integer authorId);
}
