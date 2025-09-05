package com.example.sinchonthon4.service;

import com.example.sinchonthon4.dto.request.QuizCountRequest;
import com.example.sinchonthon4.dto.response.QuizResponse;
import com.example.sinchonthon4.entity.Quiz;
import com.example.sinchonthon4.repository.QuizLogRepository;
import com.example.sinchonthon4.repository.QuizRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizLogRepository quizLogRepository;

    //퀴즈 출제
    @Transactional
    public List<QuizResponse> createQuizByCategory(QuizCountRequest req) {

    }

}
