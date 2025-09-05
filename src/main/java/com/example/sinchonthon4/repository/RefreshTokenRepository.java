package com.example.sinchonthon4.repository;

import com.example.sinchonthon4.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserEmail(String userEmail);
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserEmail(String userEmail);
}
