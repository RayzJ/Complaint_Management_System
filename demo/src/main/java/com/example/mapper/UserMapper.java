package com.example.mapper;

import com.example.entity.User;
import com.example.dto.UserDTO;

public class UserMapper {
    public static UserDTO toDto(User e){
        if(e==null) return null;
        UserDTO d = new UserDTO();
        d.setId(e.getId());
        d.setUsername(e.getUsername());
        d.setEmail(e.getEmail());
        d.setFullName(e.getFullName());
        d.setRoleId(e.getRole()!=null?e.getRole().getId():null);
        d.setIsActive(e.getIsActive());
        return d;
    }
    public static User toEntity(UserDTO d){
        if(d==null) return null;
        User u = new User();
        u.setId(d.getId());
        u.setUsername(d.getUsername());
        u.setEmail(d.getEmail());
        u.setFullName(d.getFullName());
        u.setIsActive(d.getIsActive()!=null?d.getIsActive():true);
        return u;
    }
}
