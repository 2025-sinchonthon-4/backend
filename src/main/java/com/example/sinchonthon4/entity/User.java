package com.example.sinchonthon4.entity;

import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private Integer level = 1; // 기본 레벨 1

    @Column(nullable = false)
    private Integer exp = 0; // 기본 경험치 0

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @ElementCollection(targetClass = Category.class)
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    @Builder.Default // ⬅빌더가 이 기본값을 사용하도록 추가
    private Set<Category> category = new HashSet<>();

    @Column(nullable = false)
    private Integer todaySolvedCount = 0; // 오늘 푼 문제 수

    @Column(nullable = false)
    private Integer continuousStudyDays = 0; // 연속 학습 일수

    private LocalDateTime lastStudyDate; // 마지막으로 학습한 날짜

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }


    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column
    private Set<Category> interests = new HashSet<>();

    // 레벨, 경험치 업데이트를 위한 편의 메서드 (추후 구현)
    public void addExp(int amount) {
        this.exp += amount;
        // 레벨업 체크 로직 추가
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateCategory(Set<Category> category){
        this.category.clear();
        this.category.addAll(category);
    }

    public boolean isCategorySet() {
        return this.category != null && !this.category.isEmpty();
    }

    public void solvedProblem() {
        LocalDateTime today = LocalDateTime.now();

        if (lastStudyDate == null || !lastStudyDate.toLocalDate().equals(today.toLocalDate())) {
            // 새로운 날이면 초기화
            todaySolvedCount = 0;

            // 마지막 학습일이 어제면 연속 학습 유지, 아니면 초기화
            if (lastStudyDate != null &&
                    lastStudyDate.toLocalDate().plusDays(1).equals(today.toLocalDate())) {
                continuousStudyDays++;
            } else {
                continuousStudyDays = 1;
            }
        }

        todaySolvedCount++;
        lastStudyDate = today;
    }
}
