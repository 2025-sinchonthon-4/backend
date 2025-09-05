package com.example.sinchonthon4.service;

import com.example.sinchonthon4.config.TokenProvider;
import com.example.sinchonthon4.entity.CustomOAuth2User;
import com.example.sinchonthon4.entity.User;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        log.info("OAuth2 로그인 성공, JWT 발급 및 온보딩 상태 확인 시작");

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        User user = customOAuth2User.getUser();

        boolean needsOnboarding = !user.isCategorySet();

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