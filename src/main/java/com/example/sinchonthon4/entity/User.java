package com.example.sinchonthon4.entity;

import com.example.sinchonthon4.dto.request.UserUpdateRequestDto;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자
@Setter
@Table(name = "app_user")
public class User extends BaseTimeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    private String name;
    private String phone;
    private String profileImage;

    @Column(nullable = false)
    private String nickname;


    @Column(nullable = false)
    private String socialId; // 카카오에서 제공하는 고유 ID
    private String address;


    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private Integer level = 1; // 기본 레벨 1

    @Column(nullable = false)
    private Integer exp = 0; // 기본 경험치 0
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // 레벨, 경험치 업데이트를 위한 편의 메서드 (추후 구현)
    public void addExp(int amount) {
        this.exp += amount;
        // 레벨업 체크 로직 추가
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
