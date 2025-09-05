package com.example.sinchonthon4.service;

import com.example.sinchonthon4.config.TokenProvider;
import com.example.sinchonthon4.entity.CustomOAuth2User;
import com.example.sinchonthon4.entity.User;
import com.example.sinchonthon4.repository.CookieAuthorizationRequestRepository;
import com.example.sinchonthon4.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

    private final String DEFAULT_REDIRECT_URI = "http://localhost:3000/oauth-redirect";

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

        Optional<String> redirectUri = CookieUtil.getCookie(request, "redirect_uri")
                .map(Cookie::getValue);

        // redirectUri 파라미터가 있다면 그 값을, 없다면 기본값을 사용합니다.
        String targetUrl = redirectUri.orElse(DEFAULT_REDIRECT_URI);

        // ✅ 3. 가져온 URI에 토큰과 상태를 담아 최종 리다이렉트 주소를 만듭니다.
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken)
                .queryParam("needsOnboarding", needsOnboarding)
                .build().toUriString();

        // ✅ 4. 인증 관련 임시 쿠키/세션을 정리합니다.
        clearAuthenticationAttributes(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);

        log.info("JWT 발급 완료: {}, 온보딩 필요 여부: {}", accessToken, needsOnboarding);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        // ✅ removeAuthorizationRequest 대신 removeAuthorizationRequestCookies를 호출해야 합니다.
        if (authorizationRequestRepository instanceof CookieAuthorizationRequestRepository) {
            ((CookieAuthorizationRequestRepository) authorizationRequestRepository).removeAuthorizationRequestCookies(request, response);
        }
    }
}git