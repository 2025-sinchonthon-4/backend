package com.example.sinchonthon4.config;

import com.example.sinchonthon4.config.JwtProperties;
import com.example.sinchonthon4.entity.CustomOAuth2User; // 🚩 CustomOAuth2User import 추가
import com.example.sinchonthon4.entity.UserInfo;
import com.example.sinchonthon4.service.UserInfoService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // 🚩 UserDetails import 추가
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String TOKEN_TYPE_KEY = "type";

    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private Key key;

    @Autowired
    private UserInfoService userInfoService;

    // JwtProperties를 주입받는 생성자는 그대로 유지
    public TokenProvider(JwtProperties jwtProperties) {
        this.secret = jwtProperties.getSecret();
        this.accessTokenValidityInMilliseconds = jwtProperties.getAccessTokenValidityInSeconds() * 1000;
        this.refreshTokenValidityInMilliseconds = jwtProperties.getRefreshTokenValidityInSeconds() * 1000;

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 🚩 createAccessToken 메소드 수정
    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accessTokenValidityInMilliseconds);

        // 🚩 OAuth2 로그인 유저와 일반 로그인 유저를 구분하여 이메일을 추출
        String email;
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOAuth2User) {
            // OAuth2 로그인 시
            email = ((CustomOAuth2User) principal).getUser().getEmail();
        } else if (principal instanceof UserDetails) {
            // 일반 로그인 시 (UserDetails를 구현한 객체)
            email = ((UserDetails) principal).getUsername();
        } else {
            // 기타 경우
            email = principal.toString();
        }

        return Jwts.builder()
                .setSubject(email) // ⬅️ 여기에 이메일을 Subject로 설정
                .claim(AUTHORITIES_KEY, authorities)
                .claim(TOKEN_TYPE_KEY, "access")
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    // 🚩 Refresh Token도 동일한 방식으로 이메일을 사용하도록 수정 (일관성 유지)
    public String createRefreshToken(Authentication authentication) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);

        String email;
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOAuth2User) {
            email = ((CustomOAuth2User) principal).getUser().getEmail();
        } else if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        return Jwts.builder()
                .setSubject(email) // ⬅️ 여기에 이메일을 Subject로 설정
                .claim(TOKEN_TYPE_KEY, "refresh")
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    // getAuthentication 메소드는 수정할 필요 없음 (이미 Subject를 사용하고 있음)
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities;
        if (claims.containsKey(AUTHORITIES_KEY) && claims.get(AUTHORITIES_KEY) != null) {
            authorities =
                    Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
        } else {
            authorities = new ArrayList<>();
        }

        // 이제 claims.getSubject()는 이메일을 반환하므로, loadUserByUsername과 완벽하게 일치합니다.
        UserInfo userInfo = (UserInfo) userInfoService.loadUserByUsername(claims.getSubject());
        userInfo.setAuthorities(authorities);

        return new UsernamePasswordAuthenticationToken(userInfo, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String getUserEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getTokenType(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(TOKEN_TYPE_KEY, String.class);
    }

    public Long getExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().getTime();
    }
}

