package com.example.sinchonthon4.service;

import com.example.sinchonthon4.dto.response.QuizResponse;
import com.example.sinchonthon4.entity.*;
import com.example.sinchonthon4.dto.request.QuizSubmitRequestDto;
import com.example.sinchonthon4.dto.response.QuizResponseDto;
import com.example.sinchonthon4.dto.response.QuizSubmitResponseDto;
import com.example.sinchonthon4.repository.QuizLogRepository;
import com.example.sinchonthon4.repository.QuizRepository;
import com.example.sinchonthon4.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserService userService; // 유저 성장 로직 처리를 위해 주입
    private final QuizLogRepository quizLogRepository;
    private final UserRepository userRepository;

    /**
     * 랜덤 퀴즈 조회
     */
    public List<QuizResponseDto> getRandomQuiz(Integer count) {
        List<Quiz> randList = quizRepository.findRandomQuizzes(count);
        if (randList == null) {
            throw new IllegalArgumentException("표시할 퀴즈가 없습니다.");
        }
        return randList.stream()
                .map(quiz -> QuizResponseDto.from(quiz))
                .toList(); // DTO 변환
    }

    /**
     * 정답 제출 및 결과 처리
     */
    @Transactional
    public QuizSubmitResponseDto submitAnswer(Long memberId, Long quizId, QuizSubmitRequestDto requestDto) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));
        User user = userRepository.findByUserId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));
        // 1. 정답 확인
        boolean isCorrect = checkAnswer(quiz, requestDto.getReply());

        // 2. 유저 성장 로직 처리 (UserService에 위임)
//        userService.processQuizResult(memberId, quizId, requestDto.getReply(), isCorrect);
//        QuizLog log = QuizLog.builder()
//                .reply(requestDto.getReply())
//                .isCorrect(isCorrect)
//                .user(user)
//                .quiz(quiz)
//                .build();
//        quizLogRepository.save(log);

        // 3. 최종 결과 DTO 생성 및 반환
        return new QuizSubmitResponseDto(isCorrect, requestDto.getReply(), quiz.getChoices().stream()
                .filter(QuizChoice::isAnswer)
                .findFirst()
                .map(QuizChoice::getContent)
                .orElse(""), quiz.getExplanation());
    }

    /**
     * 카테고리별 퀴즈 출제
     */
    @Transactional
    public List<QuizResponseDto> createQuizByCategory(Long userId, Integer count) {
        // TODO: 카테고리별로 퀴즈를 생성하는 로직 구현
        User user = userRepository.findByUserId(userId).orElseThrow(
                ()-> new EntityNotFoundException("존재하지 않는 유저입니다.")
        );
        List<Category> catList = user.getCategory().stream().toList();
        List<Quiz> quizList = quizRepository.findByCategoryIn(catList);
        Collections.shuffle(quizList);
        return quizList.subList(0, count).stream()
                .map(quiz-> {return QuizResponseDto.from(quiz);})
                .toList();
    }
    @Transactional
    public QuizResponse getQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(()->new EntityNotFoundException("존재하지 않는 퀴즈입니다."));
        return QuizResponse.fromEntity(quiz);
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