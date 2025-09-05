package com.example.sinchonthon4.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    private String profileImageUrl;

    @Column(nullable = false)
    private String provider; // 예: "KAKAO"

    @Column(nullable = false)
    private String providerId; // 카카오에서 제공하는 고유 ID

    @Column(nullable = false)
    private Integer level = 1; // 기본 레벨 1

    @Column(nullable = false)
    private Integer exp = 0; // 기본 경험치 0

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // 레벨, 경험치 업데이트를 위한 편의 메서드 (추후 구현)
    public void addExp(int amount) {
        this.exp += amount;
        // 레벨업 체크 로직 추가
    }
}