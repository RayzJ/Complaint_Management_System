package com.example.service;

import com.example.entity.Role;
import com.example.dao.RoleRepository;
import com.example.dto.RoleDTO;
import com.example.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleRepository roleRepository;

    public List<RoleDTO> getAll(){
        logger.info("Fetching all roles");
        return roleRepository.findAll().stream().map(RoleMapper::toDto).collect(Collectors.toList());
    }

    public RoleDTO getById(Integer id){
        logger.info("Fetching role id={}", id);
        return roleRepository.findById(id).map(RoleMapper::toDto).orElse(null);
    }

    public RoleDTO create(RoleDTO dto){
        logger.info("Creating role {}", dto.getName());
        Role r = RoleMapper.toEntity(new RoleDTO(dto.getId(), dto.getName()));
        r = roleRepository.save(r);
        return RoleMapper.toDto(r);
    }

    public RoleDTO update(Integer id, RoleDTO dto){
        logger.info("Updating role id={}", id);
        return roleRepository.findById(id).map(r -> {
            r.setName(dto.getName());
            roleRepository.save(r);
            return RoleMapper.toDto(r);
        }).orElse(null);
    }

    public void delete(Integer id){
        logger.info("Deleting role id={}", id);
        roleRepository.deleteById(id);
    }
}
