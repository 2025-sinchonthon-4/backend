package com.example.sinchonthon4.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponseDto {
    private String nickname;
    private int level;
    private int exp;
    private int totalAttempts;
    private int correctAttempts;
    private double correctRate;
}