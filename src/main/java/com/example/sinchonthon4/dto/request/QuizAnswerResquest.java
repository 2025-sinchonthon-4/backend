package com.example.sinchonthon4.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizAnswerResquest {
    private Long userId;
    private String answer;
}
