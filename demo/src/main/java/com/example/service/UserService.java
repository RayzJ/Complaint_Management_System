package com.example.service;

import com.example.entity.User;
import com.example.entity.Role;
import com.example.dao.RoleRepository;
import com.example.dao.UserRepository;
import com.example.dto.UserDTO;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.BadRequestException;

@Service
@Transactional
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    public List<UserDTO> getAll(){
        logger.info("Fetching all users");
        return userRepository.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    public UserDTO getById(Integer id){
        logger.info("Fetching user id={}", id);
        Optional<User> ou = userRepository.findById(id);
        if(ou.isEmpty()) throw new ResourceNotFoundException("User not found: " + id);
        return UserMapper.toDto(ou.get());
    }

    public UserDTO create(UserDTO dto){
        logger.info("Creating user {}", dto.getUsername());
        if(dto.getUsername() == null || dto.getUsername().isBlank()) throw new BadRequestException("Username is required");
        User u = UserMapper.toEntity(dto);
        if(dto.getRoleId()!=null){
            Role r = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + dto.getRoleId()));
            u.setRole(r);
        }
        u = userRepository.save(u);
        return UserMapper.toDto(u);
    }

    public UserDTO update(Integer id, UserDTO dto){
        logger.info("Updating user id={}", id);
        Optional<User> ou = userRepository.findById(id);
        if(ou.isEmpty()) throw new ResourceNotFoundException("User not found: " + id);
        User u = ou.get();
        u.setFullName(dto.getFullName());
        u.setEmail(dto.getEmail());
        u.setUsername(dto.getUsername());
        if(dto.getRoleId()!=null){
            Role r = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + dto.getRoleId()));
            u.setRole(r);
        }
        u = userRepository.save(u);
        return UserMapper.toDto(u);
    }

    public void delete(Integer id){
        logger.info("Deleting user id={}", id);
        if (!userRepository.existsById(id)) throw new ResourceNotFoundException("User not found: " + id);
        userRepository.deleteById(id);
    }

    public UserDTO findByUsername(String username){
        logger.info("Find user by username={}", username);
        return userRepository.findByUsername(username)
            .map(UserMapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}
