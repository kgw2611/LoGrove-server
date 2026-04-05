package com.hansung.logrove.auth.controller;

import com.hansung.logrove.auth.dto.LoginRequest;
import com.hansung.logrove.auth.dto.LoginResponse;
import com.hansung.logrove.auth.service.AuthService;
import com.hansung.logrove.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.ok(null);
    }
}