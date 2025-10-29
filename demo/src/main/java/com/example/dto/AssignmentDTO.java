
package com.example.dto;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
public class AssignmentDTO {
    private Integer id;
    @NotNull(message = "ticketId is required")
    private Integer ticketId;

    @NotNull(message = "assignedTo is required")
    private Integer assignedTo;

    @NotNull(message = "assignedBy is required")
    private Integer assignedBy;
    private LocalDateTime assignedAt;
    private String note;

    public AssignmentDTO() {}
    public Integer getId(){return id;} public void setId(Integer i){this.id=i;}
    public Integer getTicketId(){return ticketId;} public void setTicketId(Integer t){this.ticketId=t;}
    public Integer getAssignedTo(){return assignedTo;} public void setAssignedTo(Integer a){this.assignedTo=a;}
    public Integer getAssignedBy(){return assignedBy;} public void setAssignedBy(Integer a){this.assignedBy=a;}
    public LocalDateTime getAssignedAt(){return assignedAt;} public void setAssignedAt(LocalDateTime d){this.assignedAt=d;}
    public String getNote(){return note;} public void setNote(String n){this.note=n;}
}
