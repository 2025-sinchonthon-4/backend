package com.example.sinchonthon4.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class QuizLogDto {
    private Long quizId;
    private String quizQuestion;
    private String myReply;
    private String correctAnswer;
    private String explanation;
    private boolean isCorrect;
    private LocalDate solvedAt;
}