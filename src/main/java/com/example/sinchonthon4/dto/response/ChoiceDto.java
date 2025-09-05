package com.example.sinchonthon4.dto.response;

import com.example.sinchonthon4.entity.QuizChoice;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceDto {
    private Long choiceId;
    private String content;
    private Boolean isAnswer;

    public static ChoiceDto of(QuizChoice choice) {
        return ChoiceDto.builder()
                .choiceId(choice.getId())
                .content(choice.getContent())
                .isAnswer(choice.isAnswer())
                .build();
    }
}