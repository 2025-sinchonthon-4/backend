package com.example.sinchonthon4.service;

import com.example.sinchonthon4.dto.request.OnboardingRequestDto;
import com.example.sinchonthon4.entity.User;
import com.example.sinchonthon4.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
public class OnboardingService {
    private final UserRepository userRepository;

    public OnboardingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveOnboardingInfo(User principalUser, OnboardingRequestDto request) {
        User user = userRepository.findByUserId(principalUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + principalUser.getUserId()));

        user.updateCategory(new HashSet<>(request.getCategory()));

        userRepository.save(user);
    }
}
