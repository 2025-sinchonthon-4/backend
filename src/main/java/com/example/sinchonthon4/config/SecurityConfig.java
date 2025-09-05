package com.example.sinchonthon4.config;

import com.example.sinchonthon4.config.CustomJwtFilter;
import com.example.sinchonthon4.config.JwtAccessDeniedHandler;
import com.example.sinchonthon4.config.JwtAuthenticationEntryPoint;
import com.example.sinchonthon4.repository.CookieAuthorizationRequestRepository;
import com.example.sinchonthon4.service.KakaoOAuth2UserService;
import com.example.sinchonthon4.service.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Profile("!dev")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final KakaoOAuth2UserService kakaoOAuth2UserService;
    private final CustomJwtFilter customJwtFilter;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    // ✅ 1. 공개 경로용 SecurityFilterChain (JWT 필터 없음)
    @Bean
    @Order(1)
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                                .frameOptions(frameOptions -> frameOptions.disable()) // h2-console 등을 위해 disable하거나
                        // .frameOptions(frameOptions -> frameOptions.sameOrigin()) // 혹은 sameOrigin()으로 설정
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 이 필터 체인이 적용될 경로 지정
                .securityMatcher(
                        "/", "/home", "/login/**", "/oauth2/**", "/h2-console/**",
                        "/api/auth/login", "/static/**", "/favicon.ico", "/auth", "/Signup",
                        "/css/**", "/js/**", "/images/**", "/products/**", "/api/auth/signup1", "/quizLogs/**"
                        ,"/quiz/**"
                )
                .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                        // ✅ 인증 요청을 쿠키에 임시 저장하도록 설정
                        .authorizationEndpoint(auth -> auth
                                .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                        )
                        .userInfoEndpoint(userInfo -> userInfo.userService(kakaoOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                );

        return http.build();
    }

    // ✅ 2. 인증이 필요한 API용 SecurityFilterChain (JWT 필터 적용)
    @Bean
    @Order(2)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 이 필터 체인이 적용될 경로 지정
                .securityMatcher("/api/v1/**", "api/auth/signup2")
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/auth/signup2").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                // 이 체인에만 JWT 필터를 추가
                .addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class)
                // OAuth2 관련 설정은 필요에 따라 이 체인 또는 다른 체인에 구성할 수 있습니다.
                // 만약 /api/v1/** 경로 외에 OAuth2가 필요하다면 publicFilterChain에도 추가해야 할 수 있습니다.
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(kakaoOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:5173",
                "https://spacefarm.shop",
                "http://localhost:8080",
                "https://cenchi.vercel.app/"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        // WebSocket 관련 헤더 추가
        configuration.addExposedHeader("Sec-WebSocket-Accept");
        configuration.addExposedHeader("Sec-WebSocket-Extensions");
        configuration.addExposedHeader("Sec-WebSocket-Protocol");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
