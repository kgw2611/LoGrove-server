package com.hansung.logrove.auth.controller;

import com.hansung.logrove.auth.dto.LoginRequest;
import com.hansung.logrove.auth.dto.LoginResponse;
import com.hansung.logrove.auth.service.AuthService;
import com.hansung.logrove.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인
     * POST /api/auth/login
     * 성공 시 JWT 토큰 반환
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    /**
     * 로그아웃
     * POST /api/auth/logout
     * stateless JWT 방식 — 서버는 별도 처리 없이 클라이언트가 토큰을 폐기
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.ok(null);
    }
}