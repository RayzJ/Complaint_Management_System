package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "body", columnDefinition = "TEXT")//Allows long message content.
    private String body;

    @Column(name = "is_internal", nullable = false)
    private Boolean isInternal;

    @Column(name = "created_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")//Automatically set when the comment is created.
    private LocalDateTime createdAt;

    @Column(name = "edited_at", columnDefinition = "TIMESTAMP NULL DEFAULT NULL")//Set when the comment is edited.
    private LocalDateTime editedAt;
}
