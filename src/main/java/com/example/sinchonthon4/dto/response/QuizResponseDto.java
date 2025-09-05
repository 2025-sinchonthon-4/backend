package com.example.sinchonthon4.dto.response;

import com.example.sinchonthon4.entity.Quiz;
import com.example.sinchonthon4.entity.QuizType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class QuizResponseDto {
    private Long quizId;
    private QuizType type;
    private String question;
    private String hint;
    private String imgUrl;
    private List<QuizChoiceDto> choices;

    public static QuizResponseDto from(Quiz quiz) {
        List<QuizChoiceDto> choiceDtos = quiz.getChoices().stream()
                .map(QuizChoiceDto::from)
                .collect(Collectors.toList());

        return QuizResponseDto.builder()
                .quizId(quiz.getId())
                .type(quiz.getType())
                .question(quiz.getQuestion())
                .hint(quiz.getHint())
                .choices(choiceDtos)
                .imgUrl(quiz.getImgUrl())
                .build();
    }
}