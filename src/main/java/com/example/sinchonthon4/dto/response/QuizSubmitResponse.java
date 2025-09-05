package com.example.sinchonthon4.dto.response;

import com.example.sinchonthon4.entity.Quiz;
import com.example.sinchonthon4.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizSubmitResponse {
    private Long quizId;
    private Long userId;
    private boolean isCorrect;
    private String reply;
    private String answer;

    public static QuizSubmitResponse of(Quiz quiz, User user,String reply) {
            return QuizSubmitResponse.builder()
                    .quizId(quiz.getId())
                    .userId(user.getUserId())
                    .reply(reply)
                    .answer(quiz.getAnswer())
                    .build();
    }
}
