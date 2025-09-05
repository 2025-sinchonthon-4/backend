package com.example.sinchonthon4.controller;

import com.example.sinchonthon4.dto.request.QuizLogCreateRequest;
import com.example.sinchonthon4.dto.response.QuizLogResponse;
import com.example.sinchonthon4.entity.UserInfo;
import com.example.sinchonthon4.service.QuizLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/quizLogs")
public class QuizLogController {
    private final QuizLogService quizLogService;

    @PostMapping
    public ResponseEntity<QuizLogResponse> createQuizLog(@AuthenticationPrincipal UserInfo user, @RequestBody QuizLogCreateRequest req){
        QuizLogResponse res = quizLogService.create(user.getUser().getUserId(), req);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/history")
    public ResponseEntity<List<QuizLogResponse>> getQuizLogHistory(@AuthenticationPrincipal UserInfo user){
        List<QuizLogResponse> res = quizLogService.getQuizLogs(user.getUser().getUserId());
        return ResponseEntity.ok(res);
    }


}
