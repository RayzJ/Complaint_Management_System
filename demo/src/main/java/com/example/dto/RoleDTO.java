
package com.example.dto;
import jakarta.validation.constraints.NotBlank;

public class RoleDTO {
    private Integer id;

    @NotBlank(message = "role name is required")
    private String name;

    public RoleDTO() {}
    public RoleDTO(Integer id, String name) { this.id = id; this.name = name; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
