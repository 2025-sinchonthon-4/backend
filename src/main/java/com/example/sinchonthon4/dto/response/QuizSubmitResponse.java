package com.example.sinchonthon4.dto.response;

import com.example.sinchonthon4.entity.Quiz;
import com.example.sinchonthon4.entity.QuizChoice;
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

        String correctAnswer = quiz.getChoices().stream()
                .filter(QuizChoice::isAnswer)
                .findFirst()
                .map(QuizChoice::getContent)
                .orElse("정답 없음");

            return QuizSubmitResponse.builder()
                    .quizId(quiz.getId())
                    .userId(user.getUserId())
                    .reply(reply)
                    .answer(correctAnswer) // 위에서 찾은 정답을 사용
                    .build();
    }
}
