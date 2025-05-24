package com.dailycodeworks.dream_shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodeworks.dream_shop.entity.RefreshToken;
import com.dailycodeworks.dream_shop.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    int deleteByUser(User user);
}
