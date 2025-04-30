package com.dailycodeworks.dream_shop.data;



import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodeworks.dream_shop.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String role);
}