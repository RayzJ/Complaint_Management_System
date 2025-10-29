package com.example.controller;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.dao.RoleRepository;
import com.example.dao.UserRepository;
import com.example.dto.UserDTO;
import com.example.mapper.UserMapper;
import com.example.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @jakarta.validation.Valid UserDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username_exists"));
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "password_required"));
        }
        Role role = roleRepository.findByName("customer").orElseGet(() -> {
            Role r = new Role();
            r.setName("customer");
            return roleRepository.save(r);
        });
        User u = UserMapper.toEntity(dto);
        u.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        u.setRole(role);
        u = userRepository.save(u);

        return ResponseEntity.ok(Map.of("message", "registered", "username", u.getUsername()));
    }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
    String username = body.get("username");
    String password = body.get("password");
    var opt = userRepository.findByUsername(username);
    if (opt.isEmpty())
        return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
    User u = opt.get();
    if (!passwordEncoder.matches(password, u.getPasswordHash()))
        return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));

    // Get user roles, full name, and userId
    List<String> roles = u.getRole() != null ? List.of(u.getRole().getName()) : List.of();
    String fullName = u.getFullName();
    Integer userId = u.getId();  // Extract the user ID

    // Generate the token with username, fullName, roles, and userId
    String token = jwtUtil.generateToken(u.getUsername(), fullName, roles, userId);

    // Return token, username, fullName, roles, and userId
    return ResponseEntity.ok(Map.of("token", token, "username", u.getUsername(), "fullName", fullName, "roles", roles, "userId", userId));
}

}
