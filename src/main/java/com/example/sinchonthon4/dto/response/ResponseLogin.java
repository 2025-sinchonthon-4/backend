package com.example.sinchonthon4.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseLogin {
    private String accessToken;
    private String refreshToken;
    private String nickname;
}
