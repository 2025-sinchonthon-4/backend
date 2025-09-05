package com.example.sinchonthon4.dto.request;

import com.example.sinchonthon4.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OnboardingRequestDto {
    private List<Category> category;
}
