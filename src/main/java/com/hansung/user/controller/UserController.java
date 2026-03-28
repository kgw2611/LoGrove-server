package com.hansung.user.controller;

import com.hansung.global.jwt.JwtUtil;
import com.hansung.global.response.ApiResponse;
import com.hansung.global.response.SuccessCode;
import com.hansung.user.dto.UserResponse;
import com.hansung.user.dto.UserUpdateRequest;
import com.hansung.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 API")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // JWT 토큰에서 userId 추출 후 마이페이지 조회
    @Operation(summary = "마이페이지 조회")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(userService.getMyInfo(userId)));
    }

    // 닉네임, 이메일 수정
    @Operation(summary = "회원 정보 수정")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyInfo(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UserUpdateRequest request) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(userService.updateMyInfo(userId, request)));
    }

    // 회원 탈퇴 - 성공 시 데이터 없이 응답
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}