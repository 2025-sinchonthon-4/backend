package com.example.sinchonthon4.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 생성자를 이용해 간단히 만듦
public class QuizSubmitResponseDto {
    private boolean isCorrect;
    private String reply;
    private String answer;
    private String explanation;
}