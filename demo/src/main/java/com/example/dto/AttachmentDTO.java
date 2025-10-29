
package com.example.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
public class AttachmentDTO {
    private Integer id;
    @NotNull(message = "ticketId is required")
    private Integer ticketId;
    private Integer uploadedBy;

    @NotBlank(message = "filePath is required")
    private String filePath;

    @NotBlank(message = "filename is required")
    private String filename;
    private String contentType;
    private Long sizeBytes;
    private LocalDateTime createdAt;
    public AttachmentDTO() {}
    public Integer getId(){return id;} public void setId(Integer i){this.id=i;}
    public Integer getTicketId(){return ticketId;} public void setTicketId(Integer t){this.ticketId=t;}
    public Integer getUploadedBy(){return uploadedBy;} public void setUploadedBy(Integer u){this.uploadedBy=u;}
    public String getFilePath(){return filePath;} public void setFilePath(String f){this.filePath=f;}
    public String getFilename(){return filename;} public void setFilename(String f){this.filename=f;}
    public String getContentType(){return contentType;} public void setContentType(String c){this.contentType=c;}
    public Long getSizeBytes(){return sizeBytes;} public void setSizeBytes(Long s){this.sizeBytes=s;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime d){this.createdAt=d;}
}
