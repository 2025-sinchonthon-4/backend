package com.example.sinchonthon4.dto.response;

import com.example.sinchonthon4.entity.QuizLog;
import com.example.sinchonthon4.entity.QuizType;

import java.time.LocalDateTime;

public record QuizLogResponse(
        String title,
        QuizType type,
        String reply,
        boolean isCorrect,
        LocalDateTime solvedAt
) {
    public static QuizLogResponse of(QuizLog q) {
        return new QuizLogResponse(
                q.getQuiz().getTitle(),
                q.getQuiz().getType(),
                q.getReply(),
                q.getIsCorrect(),
                q.getCreatedAt()
        );
    }
}
