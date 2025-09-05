package com.example.sinchonthon4.controller;

import com.example.sinchonthon4.dto.response.QuizResponse;
import com.example.sinchonthon4.dto.response.QuizResponseDto;
import com.example.sinchonthon4.entity.UserInfo;
import com.example.sinchonthon4.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/quizzes")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }
    @PostMapping
    public ResponseEntity<QuizResponse> createQuiz(@RequestBody QuizCreateRequest req) {
        QuizResponse res = quizService.createQuiz(req);
        return ResponseEntity.ok(res);

    }

    @GetMapping
    public ResponseEntity<List<QuizResponse>> getQuiz(@AuthenticationPrincipal UserInfo user, @RequestParam Integer count) {
        List<QuizResponse> res = quizService.createQuizByCategory(user.getUser().getUserId(), count);
        return ResponseEntity.ok(res);

    }
}
