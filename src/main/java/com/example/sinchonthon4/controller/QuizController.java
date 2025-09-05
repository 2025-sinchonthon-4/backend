package com.example.sinchonthon4.controller;

import com.example.sinchonthon4.dto.request.QuizSubmitRequestDto;
import com.example.sinchonthon4.dto.response.QuizLogResponse;
import com.example.sinchonthon4.dto.response.QuizResponse;
import com.example.sinchonthon4.dto.response.QuizResponseDto;
import com.example.sinchonthon4.dto.response.QuizSubmitResponseDto;
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
//    @PostMapping
//    public ResponseEntity<QuizResponse> createQuiz(@RequestBody QuizCreateRequest req) {
//        QuizResponse res = quizService.createQuiz(req);
//        return ResponseEntity.ok(res);
//
//    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<QuizSubmitResponseDto> submitAnswer(@AuthenticationPrincipal UserInfo user, @PathVariable Long id, @RequestBody QuizSubmitRequestDto req) {
        QuizSubmitResponseDto res = quizService.submitAnswer(user.getUser().getUserId(), id, req);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/random")
    public ResponseEntity<List<QuizResponseDto>> getRandomQuiz(@RequestParam("count") int count) {
        List<QuizResponseDto> res = quizService.getRandomQuiz(count);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<List<QuizResponseDto>> getQuiz(@AuthenticationPrincipal UserInfo user, @RequestParam("count") int count) {
        List<QuizResponseDto> res = quizService.createQuizByCategory(user.getUser().getUserId(), count);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResponse> getQuizById(@PathVariable Long id) {
        QuizResponse res = quizService.getQuiz(id);
        return ResponseEntity.ok(res);
    }
}
