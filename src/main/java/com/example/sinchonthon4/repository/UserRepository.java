package com.example.sinchonthon4.repository;

import com.example.sinchonthon4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long > {
    Optional<User> findBySocialIdAndSocialType(String socialId, Enum socialType);

    boolean existsBySocialIdAndSocialType(String socialId, Enum socialType);

    // 소셜 로그인 제공자와 제공자 ID로 이미 가입된 회원인지 찾는 메서드
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(Long userID);
}
