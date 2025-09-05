package com.example.sinchonthon4.controller;

import com.example.sinchonthon4.dto.response.MyPageResponseDto;
import com.example.sinchonthon4.dto.response.QuizLogDto;
import com.example.sinchonthon4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 내 정보 조회 (마이페이지)
     */
    @GetMapping("/me")
    public ResponseEntity<MyPageResponseDto> getMyPageInfo(
            // @AuthenticationPrincipal UserDetailsImpl userDetails // TODO: Spring Security 설정 후 주석 해제
    ) {
        // Long memberId = userDetails.getMember().getId(); // TODO: 실제 로그인된 유저 ID 가져오기
        Long memberId = 1L; // HACK: 테스트를 위한 임시 하드코딩 ID

        MyPageResponseDto myPageInfo = userService.getMyPageInfo(memberId);
        return ResponseEntity.ok(myPageInfo);
    }

    /**
     * 내 퀴즈 풀이 기록 조회
     */
    @GetMapping("/me/logs")
    public ResponseEntity<List<QuizLogDto>> getQuizHistory(
            // @AuthenticationPrincipal UserDetailsImpl userDetails // TODO: Spring Security 설정 후 주석 해제
    ) {
        // Long memberId = userDetails.getMember().getId(); // TODO: 실제 로그인된 유저 ID 가져오기
        Long memberId = 1L; // HACK: 테스트를 위한 임시 하드코딩 ID

        List<QuizLogDto> quizHistory = userService.getQuizHistory(memberId);
        return ResponseEntity.ok(quizHistory);
    }
}