package com.example.sinchonthon4.dto.response;

import com.example.sinchonthon4.entity.Quiz;
import com.example.sinchonthon4.entity.QuizType;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizAnswerResponse {

    private Long quizId;
    private QuizType type;
    private String title;
    private String correctAnswer;
    private String explanation;
    private List<ChoiceDto> choices;

    public static QuizAnswerResponse of(Quiz quiz) {
        List<ChoiceDto> choiceDtos = quiz.getChoices() != null
                ? quiz.getChoices().stream()
                .map(ChoiceDto::of)
                .collect(Collectors.toList())
                : List.of();

        return QuizAnswerResponse.builder()
                .quizId(quiz.getId())
                .type(quiz.getType())
                .title(quiz.getTitle())
                .correctAnswer(quiz.getAnswer())
                .explanation(quiz.getExplanation())
                .choices(choiceDtos)
                .build();
    }
}
