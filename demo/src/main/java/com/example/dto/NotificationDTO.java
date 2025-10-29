
package com.example.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
public class NotificationDTO {
    private Integer id;
    @NotNull(message = "userId is required")
    private Integer userId;
    private Integer ticketId;

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "body is required")
    private String body;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private String senderName;
    public NotificationDTO() {}
    public Integer getId(){return id;} public void setId(Integer i){this.id=i;}
    public Integer getUserId(){return userId;} public void setUserId(Integer u){this.userId=u;}
    public Integer getTicketId(){return ticketId;} public void setTicketId(Integer t){this.ticketId=t;}
    public String getTitle(){return title;} public void setTitle(String t){this.title=t;}
    public String getBody(){return body;} public void setBody(String b){this.body=b;}
    public Boolean getIsRead(){return isRead;} public void setIsRead(Boolean r){this.isRead=r;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime d){this.createdAt=d;}
    public String getSenderName(){return senderName;} public void setSenderName(String s){this.senderName=s;}
}
