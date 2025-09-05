package com.example.sinchonthon4.dto.request;

import com.example.sinchonthon4.entity.QuizLog;

public record QuizLogCreateRequest(
        Long quizId,
        String reply
) {
    public QuizLog toEntity() {
        return QuizLog.builder()
                .reply(reply)
                .build();
    }
}
