package com.example.sinchonthon4.service;

import com.example.sinchonthon4.dto.request.RequestSignup;
import com.example.sinchonthon4.entity.Role;
import com.example.sinchonthon4.entity.User;
import com.example.sinchonthon4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserSignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RequestSignup join) {

        if (userRepository.findByEmail(join.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(join.getPassword());

        User user = User.builder()
                .email(join.getEmail())
                .password(encodedPassword)
                .name(join.getName())
                .nickname(join.getNickname())
                .phone(join.getPhone())
                .bank(join.getBank())
                .accountNumber(
                        StringUtils.hasText(join.getAccountNumber())
                                ? join.getAccountNumber()
                                : null
                )
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }
}

