
package com.example.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
public class TicketStatusHistoryDTO {
    private Integer id;
    @NotNull(message = "ticketId is required")
    private Integer ticketId;

    @NotBlank(message = "oldStatus is required")
    private String oldStatus;

    @NotBlank(message = "newStatus is required")
    private String newStatus;

    @NotNull(message = "changedBy is required")
    private Integer changedBy;
    private String comment;
    private LocalDateTime changedAt;
    public TicketStatusHistoryDTO() {}
    public Integer getId(){return id;} public void setId(Integer i){this.id=i;}
    public Integer getTicketId(){return ticketId;} public void setTicketId(Integer t){this.ticketId=t;}
    public String getOldStatus(){return oldStatus;} public void setOldStatus(String s){this.oldStatus=s;}
    public String getNewStatus(){return newStatus;} public void setNewStatus(String s){this.newStatus=s;}
    public Integer getChangedBy(){return changedBy;} public void setChangedBy(Integer c){this.changedBy=c;}
    public String getComment(){return comment;} public void setComment(String c){this.comment=c;}
    public LocalDateTime getChangedAt(){return changedAt;} public void setChangedAt(LocalDateTime d){this.changedAt=d;}
}
