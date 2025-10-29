package com.example.controller;

import com.example.dto.CommentDTO;
import com.example.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/comments")
public class CommentController {

    @Autowired private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDTO>> list(@PathVariable Integer ticketId){
        return ResponseEntity.ok(commentService.getByTicket(ticketId));
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable Integer ticketId, @RequestParam Integer authorId, @RequestBody @jakarta.validation.Valid CommentDTO dto){
        return ResponseEntity.ok(commentService.create(ticketId, authorId, dto));
    }
}
