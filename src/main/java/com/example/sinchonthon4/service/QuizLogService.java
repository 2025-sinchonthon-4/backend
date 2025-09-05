package com.example.sinchonthon4.service;

import com.example.sinchonthon4.dto.request.QuizLogCreateRequest;
import com.example.sinchonthon4.dto.response.QuizLogResponse;
import com.example.sinchonthon4.entity.Quiz;
import com.example.sinchonthon4.entity.QuizLog;
import com.example.sinchonthon4.entity.User;
import com.example.sinchonthon4.repository.QuizLogRepository;
import com.example.sinchonthon4.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizLogService {
    UserRepository userRepository;
    QuizRepository quizRepository;
    QuizLogRepository quizLogRepository;

    @Transactional
    public QuizLogResponse create(Long userId, QuizLogCreateRequest req){
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("User with id " + userId + " not found")
        );
        Quiz quiz = quizRepository.findById(req.quizId()).orElseThrow(
                ()-> new EntityNotFoundException("Quiz with id " + req.quizId() + " not found")
        );

        QuizLog quizLog = req.toEntity();
        quizLog.setUser(user);
        quizLog.setQuiz(quiz);
        quizLogRepository.save(quizLog);
        return QuizLogResponse.of(quizLog);
    }
}
