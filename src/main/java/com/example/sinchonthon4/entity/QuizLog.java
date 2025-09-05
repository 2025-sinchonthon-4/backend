package com.example.sinchonthon4.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuizLog extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Quiz quiz;

    @Column(nullable = false)
    private String reply;
    // TF(OX 문제): O, X로 저장
    // SHORT: 들어온 답변 그대로 저장
    // MULTIPLE_CHOICE: 선택된 객관식 선택지 (quizChoice)의 id를 저장

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
