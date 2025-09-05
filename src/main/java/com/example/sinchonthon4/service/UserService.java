package com.example.sinchonthon4.service;

import com.example.sinchonthon4.dto.response.MyPageResponseDto;
import com.example.sinchonthon4.dto.response.QuizLogDto;
import com.example.sinchonthon4.entity.QuizChoice;
import com.example.sinchonthon4.entity.User;
import com.example.sinchonthon4.entity.Quiz;
import com.example.sinchonthon4.entity.QuizLog;
import com.example.sinchonthon4.repository.UserRepository;
import com.example.sinchonthon4.repository.QuizLogRepository;
import com.example.sinchonthon4.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final QuizLogRepository quizLogRepository;
    private final QuizRepository quizRepository; // Quiz 정보가 필요하므로 주입

    /**
     * 마이페이지 정보 조회
     */
    public MyPageResponseDto getMyPageInfo(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<QuizLog> quizLogs = quizLogRepository.findAllByUser(user);

        int totalAttempts = quizLogs.size();
        long correctAttempts = quizLogs.stream().filter(QuizLog::getIsCorrect).count();

        // 0으로 나누는 것을 방지
        double correctRate = (totalAttempts == 0) ? 0 : ((double) correctAttempts / totalAttempts) * 100;

        return MyPageResponseDto.builder()
                .nickname(user.getNickname())
                .level(user.getLevel())
                .exp(user.getExp())
                .totalAttempts(totalAttempts)
                .correctAttempts((int) correctAttempts)
                .correctRate(correctRate)
                .build();
    }

    /**
     * 퀴즈 풀이 기록 조회
     */
    public List<QuizLogDto> getQuizHistory(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<QuizLog> quizLogs = quizLogRepository.findAllByUser(user);

        return quizLogs.stream()
                .map(log -> {
                    Quiz quiz = log.getQuiz();

                    // 퀴즈의 선택지들 중에서 정답을 찾아냄
                    String correctAnswer = quiz.getChoices().stream()
                            .filter(QuizChoice::isAnswer)
                            .findFirst()
                            .map(QuizChoice::getContent)
                            .orElse("정답 정보 없음");

                    return QuizLogDto.builder()
                            .quizId(quiz.getId())
                            .quizQuestion(quiz.getQuestion())
                            .myReply(log.getReply())
                            .correctAnswer(correctAnswer) // [추가]
                            .explanation(quiz.getExplanation()) // [추가]
                            .isCorrect(log.getIsCorrect())
                            .solvedAt(log.getCreatedAt().toLocalDate()) // [수정]
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 퀴즈 풀이 결과 처리 (경험치, 레벨업 등)
     */
    @Transactional // 데이터를 변경하므로 readOnly = false 적용
    public void processQuizResult(Long memberId, Long quizId, String reply, boolean isCorrect) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));

        // 1. 퀴즈 풀이 기록(Log) 저장
        QuizLog quizLog = new QuizLog(user, quiz, reply, isCorrect);
        quizLogRepository.save(quizLog);

        // 2. 정답일 경우, 경험치 부여 및 레벨업 체크
        if (isCorrect) {
            user.addExp(10); // Member Entity에 경험치 추가 및 레벨업 로직 구현
        }

        // member.addExp() 내에서 변경된 내용은 @Transactional에 의해 자동으로 DB에 반영됩니다.
    }
}