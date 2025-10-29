
package com.example.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
public class CommentDTO {
    private Integer id;
    @NotNull(message = "ticketId is required")
    private Integer ticketId;

    @NotNull(message = "authorId is required")
    private Integer authorId;

    @NotBlank(message = "body is required")
    private String body;
    private Boolean isInternal;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;

    public CommentDTO() {}
    public Integer getId(){return id;} public void setId(Integer i){this.id=i;}
    public Integer getTicketId(){return ticketId;} public void setTicketId(Integer t){this.ticketId=t;}
    public Integer getAuthorId(){return authorId;} public void setAuthorId(Integer a){this.authorId=a;}
    public String getBody(){return body;} public void setBody(String b){this.body=b;}
    public Boolean getIsInternal(){return isInternal;} public void setIsInternal(Boolean v){this.isInternal=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime d){this.createdAt=d;}
    public LocalDateTime getEditedAt(){return editedAt;} public void setEditedAt(LocalDateTime d){this.editedAt=d;}
}
