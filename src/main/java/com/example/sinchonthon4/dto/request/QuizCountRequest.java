package com.example.sinchonthon4.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizCountRequest {
    private int limit;  // 몇 문제를 불러올지
}
