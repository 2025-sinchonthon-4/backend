package com.example.sinchonthon4.dto.response;

import com.example.sinchonthon4.entity.QuizChoice;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public static class ChoiceDto {
    private Long choiceId;
    private String content;

    public static ChoiceDto fromEntity(QuizChoice choice) {
        return ChoiceDto.builder()
                .choiceId(choice.getId())
                .content(choice.getContent())
                .build();
    }
}