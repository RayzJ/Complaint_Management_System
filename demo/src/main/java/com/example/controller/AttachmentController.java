package com.example.controller;

import com.example.dto.AttachmentDTO;
import com.example.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/attachments")
public class AttachmentController {

    @Autowired private AttachmentService attachmentService;

    @GetMapping
    public ResponseEntity<List<AttachmentDTO>> list(@PathVariable Integer ticketId){
        return ResponseEntity.ok(attachmentService.getByTicket(ticketId));
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable Integer ticketId, @RequestParam Integer uploadedById, @RequestBody @jakarta.validation.Valid AttachmentDTO dto){
        return ResponseEntity.ok(attachmentService.create(ticketId, uploadedById, dto));
    }
}
