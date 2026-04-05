package com.hansung.logrove.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;       // JWT 액세스 토큰
    private String nickname;    // 로그인한 사용자 닉네임 (프론트 UI 표시용)
    private Long userId;        // 사용자 PK
}