package com.example.sinchonthon4.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class QuizLogDto {
    private Long quizId;
    private String quizQuestion;
    private String myReply;
    private boolean isCorrect;
    private LocalDateTime solvedAt;
}