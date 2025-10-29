package com.example.mapper;

import com.example.entity.Role;
import com.example.dto.RoleDTO;

public class RoleMapper {
    public static RoleDTO toDto(Role e){
        if(e==null) return null;
        return new RoleDTO(e.getId(), e.getName());
    }
    public static Role toEntity(RoleDTO d){
        if(d==null) return null;
        Role r = new Role();
        r.setId(d.getId());
        r.setName(d.getName());
        return r;
    }
}
