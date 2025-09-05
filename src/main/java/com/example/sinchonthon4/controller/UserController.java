package com.example.sinchonthon4.controller;

import com.example.sinchonthon4.dto.response.MyPageResponseDto;
import com.example.sinchonthon4.dto.response.QuizLogDto;
import com.example.sinchonthon4.entity.User;
import com.example.sinchonthon4.entity.UserInfo;
import com.example.sinchonthon4.repository.UserRepository;
import com.example.sinchonthon4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * 내 정보 조회 (마이페이지)
     */
    @GetMapping("/me")
    public ResponseEntity<MyPageResponseDto> getMyPageInfo(
            @AuthenticationPrincipal UserInfo user // ✅ 파라미터에서 직접 주입
    ) {
        Long memberId = user.getUserId(); // 로그인된 유저 ID 사용
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

    @GetMapping("/study-info")
    public ResponseEntity<?> getStudyInfo(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return ResponseEntity.ok(Map.of(
                "todaySolvedCount", user.getTodaySolvedCount(),
                "continuousStudyDays", user.getContinuousStudyDays()
        ));
    }

    @PostMapping("/solve")
    public ResponseEntity<?> solvedProblem(@PathVariable Long id) {
        userService.recordSolvedProblem(id);
        return ResponseEntity.ok("Solved recorded!");
    }
}