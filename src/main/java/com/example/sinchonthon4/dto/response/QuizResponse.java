package com.example.sinchonthon4.dto.response;

import com.example.sinchonthon4.entity.Quiz;
import com.example.sinchonthon4.entity.QuizType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponse {


        private Long quizId;
        private QuizType type;
        private String title;
        private String imgUrl;
        private String explanation;
        private String hint;
        private String categoryName;
        private List<ChoiceDto> choices;   // 객관식 / OX → 선택지, 주관식 → 빈 리스트


        //객관식인지 체크
        public static QuizResponse fromEntity(Quiz quiz) {
                List<ChoiceDto> choiceDtos = (quiz.getQuizChoices() != null)
                        ? quiz.getQuizChoices().stream()
                        .map(ChoiceDto::fromEntity)
                        .toList()
                        : List.of();

                return QuizResponse.builder()
                        .quizId(quiz.getId())
                        .type(quiz.getType())
                        .title(quiz.getTitle())
                        .imgUrl(quiz.getImgUrl())
                        .explanation(quiz.getExplanation())
                        .hint(quiz.getHint())
                        .categoryName(quiz.getCategory().getId())
                        .choices(choiceDtos)
                        .build();
        }
}
