package com.example.sinchonthon4.controller;

import com.example.sinchonthon4.dto.request.RequestLogin;
import com.example.sinchonthon4.dto.request.RequestReissueToken;
import com.example.sinchonthon4.dto.request.RequestSignup;
import com.example.sinchonthon4.dto.response.ResponseLogin;
import com.example.sinchonthon4.dto.response.ResponseReissueToken;
import com.example.sinchonthon4.entity.UserInfo;
import com.example.sinchonthon4.service.TokenReissueService;
import com.example.sinchonthon4.service.UserLoginService;
import com.example.sinchonthon4.service.UserSignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserSignupService userSignupService;
    private final UserLoginService userLoginService;
    private final TokenReissueService tokenReissueService;

    @PostMapping("/signup1")
    public ResponseEntity<ResponseLogin> signup(@RequestBody @Valid RequestSignup request) {
        userSignupService.register(request);
        ResponseLogin response = userLoginService.authenticate(request.getEmail(), request.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLogin> login(@RequestBody @Valid RequestLogin request) {
        ResponseLogin response = userLoginService.authenticate(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResponseReissueToken> reissueToken(@RequestBody @Valid RequestReissueToken request) {
        ResponseReissueToken response = tokenReissueService.reissue(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserInfo userInfo) {
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증된 사용자 정보가 없습니다.");
        }

        String userEmail = userInfo.getEmail();

        tokenReissueService.deleteRefreshToken(userEmail);

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
