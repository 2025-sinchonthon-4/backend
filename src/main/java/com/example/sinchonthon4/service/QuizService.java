package com.example.sinchonthon4.service;

import com.example.sinchonthon4.entity.Quiz;
import com.example.sinchonthon4.entity.QuizChoice;
import com.example.sinchonthon4.entity.QuizType;
import com.example.sinchonthon4.dto.request.QuizSubmitRequestDto;
import com.example.sinchonthon4.dto.response.QuizResponseDto;
import com.example.sinchonthon4.dto.response.QuizSubmitResponseDto;
import com.example.sinchonthon4.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserService userService; // 유저 성장 로직 처리를 위해 주입

    /**
     * 랜덤 퀴즈 조회
     */
    public QuizResponseDto getRandomQuiz() {
        Quiz quiz = quizRepository.findRandomQuiz();
        if (quiz == null) {
            throw new IllegalArgumentException("표시할 퀴즈가 없습니다.");
        }
        return QuizResponseDto.from(quiz); // DTO 변환
    }

    /**
     * 정답 제출 및 결과 처리
     */
    @Transactional
    public QuizSubmitResponseDto submitAnswer(Long memberId, Long quizId, QuizSubmitRequestDto requestDto) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));

        // 1. 정답 확인
        boolean isCorrect = checkAnswer(quiz, requestDto.getReply());

        // 2. 유저 성장 로직 처리 (MemberService에 위임)
        userService.processQuizResult(memberId, quizId, requestDto.getReply(), isCorrect);

        // 3. 최종 결과 DTO 생성 및 반환
        return new QuizSubmitResponseDto(isCorrect, quiz.getExplanation());
    }

    // 정답 체크 로직
    private boolean checkAnswer(Quiz quiz, String clientReply) {
        if (quiz.getType() == QuizType.MULTIPLE_CHOICE) {
            // 객관식: 사용자가 보낸 choiceId와 정답 choiceId를 비교
            Long replyChoiceId = Long.parseLong(clientReply);

            return quiz.getChoices().stream()
                    .filter(QuizChoice::isAnswer)
                    .anyMatch(choice -> choice.getId().equals(replyChoiceId));
        } else {
            // 주관식/OX: 정답 content와 사용자가 보낸 텍스트를 비교 (대소문자 무시)
            String correctAnswer = quiz.getChoices().stream()
                    .filter(QuizChoice::isAnswer)
                    .findFirst()
                    .map(QuizChoice::getContent)
                    .orElse(""); // 정답이 없는 경우를 대비
            return correctAnswer.equalsIgnoreCase(clientReply);
        }
    }
}