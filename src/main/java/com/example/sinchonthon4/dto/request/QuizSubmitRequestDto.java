package com.example.sinchonthon4.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuizSubmitRequestDto {
    private String reply; // 주관식 답안 또는 객관식 choiceId를 문자열로 받음
}