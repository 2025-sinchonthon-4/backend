package com.example.sinchonthon4.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class UserResponseDto {
    //닉넴,프사,동네,점수,계좌
    private String name;
    private String nickname;
    private String profileImage;
    private String address;
    private String phoneNumber;
}
