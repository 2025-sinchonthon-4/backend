package com.example.sinchonthon4.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz; // BE 1 담당자가 만들 Quiz Entity

    private String reply; // 사용자가 제출한 답

    @Column(nullable = false)
    private Boolean isCorrect;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 생성자
    public QuizLog(User user, Quiz quiz, String reply, Boolean isCorrect) {
        this.user = user;
        this.quiz = quiz;
        this.reply = reply;
        this.isCorrect = isCorrect;
    }
}