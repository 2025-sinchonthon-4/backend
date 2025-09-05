package com.example.sinchonthon4.dto.response;

import com.example.sinchonthon4.entity.QuizChoice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizChoiceDto {
    private Long choiceId;
    private String content;

    public static QuizChoiceDto from(QuizChoice quizChoice) {
        return QuizChoiceDto.builder()
                .choiceId(quizChoice.getId())
                .content(quizChoice.getContent())
                .build();
    }
}