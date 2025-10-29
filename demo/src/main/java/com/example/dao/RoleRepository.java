package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
