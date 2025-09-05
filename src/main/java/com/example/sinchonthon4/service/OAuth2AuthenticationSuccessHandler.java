package com.example.sinchonthon4.service;

import com.example.sinchonthon4.config.TokenProvider;
import com.example.sinchonthon4.entity.CustomOAuth2User;
import com.example.sinchonthon4.entity.User;
import com.example.sinchonthon4.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        log.info("OAuth2 로그인 성공, JWT 발급 및 온보딩 상태 확인 시작");

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User detachedUser = customOAuth2User.getUser(); // 이 객체는 분리(detached) 상태입니다.

        // 2. 현재 트랜잭션 내에서 ID를 기준으로 새로운 User 엔티티를 조회합니다.
        // 이 'managedUser'는 현재 DB 세션에 연결된 '영속성(managed)' 상태의 객체입니다.
        User managedUser = userRepository.findById(detachedUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + detachedUser.getUserId()));

        // 3. '영속성' 상태의 객체로 카테고리 설정 여부를 확인합니다.
        // 이제 LazyInitializationException이 발생하지 않습니다.
        boolean needsOnboarding = !managedUser.isCategorySet();

        String accessToken = tokenProvider.createAccessToken(authentication);

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("accessToken", accessToken);
        resp.put("needsOnboarding", needsOnboarding);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(resp));
        response.getWriter().flush();

        log.info("JWT 발급 완료: {}, 온보딩 필요 여부: {}", accessToken, needsOnboarding);
    }
}