package com.example.questapp.repos;

import com.example.questapp.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query(value = "SELECT * FROM refresh_token WHERE user_id = :userId", nativeQuery = true)
    RefreshToken findByUserId(Long userId);
}
