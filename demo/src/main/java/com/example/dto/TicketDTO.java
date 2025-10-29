
package com.example.dto;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class TicketDTO {
    private Integer id;
    private String reference;
    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "description is required")
    private String description;
    private String status;
    private String priority;
    private Integer customerId;
    private Integer assigneeId;
    private LocalDateTime slaDueAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;

    public TicketDTO() {}

    public Integer getId(){return id;} public void setId(Integer id){this.id=id;}
    public String getReference(){return reference;} public void setReference(String r){this.reference=r;}
    public String getTitle(){return title;} public void setTitle(String t){this.title=t;}
    public String getDescription(){return description;} public void setDescription(String d){this.description=d;}
    public String getStatus(){return status;} public void setStatus(String s){this.status=s;}
    public String getPriority(){return priority;} public void setPriority(String p){this.priority=p;}
    public Integer getCustomerId(){return customerId;} public void setCustomerId(Integer c){this.customerId=c;}
    public Integer getAssigneeId(){return assigneeId;} public void setAssigneeId(Integer a){this.assigneeId=a;}
    public java.time.LocalDateTime getSlaDueAt(){return slaDueAt;} public void setSlaDueAt(java.time.LocalDateTime l){this.slaDueAt=l;}
    public java.time.LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(java.time.LocalDateTime c){this.createdAt=c;}
    public java.time.LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(java.time.LocalDateTime u){this.updatedAt=u;}
    public java.time.LocalDateTime getResolvedAt(){return resolvedAt;} public void setResolvedAt(java.time.LocalDateTime r){this.resolvedAt=r;}
}
