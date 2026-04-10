package com.hansung.logrove.user.controller;

import com.hansung.logrove.comment.dto.CommentResponse;
import com.hansung.logrove.comment.service.CommentService;
import com.hansung.logrove.global.jwt.JwtUtil;
import com.hansung.logrove.global.response.ApiResponse;
import com.hansung.logrove.post.dto.PostListResponse;
import com.hansung.logrove.user.dto.SignUpRequest;
import com.hansung.logrove.user.dto.UserResponse;
import com.hansung.logrove.user.dto.UserUpdateRequest;
import com.hansung.logrove.user.service.UserService;
import com.hansung.logrove.mission.dto.MissionListResponse;
import com.hansung.logrove.mission.dto.PhotoListResponse;
import com.hansung.logrove.mission.service.MissionService;
import com.hansung.logrove.user.dto.GameProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 API")
public class UserController {

    private final UserService userService;
    private final CommentService commentService;
    private final JwtUtil jwtUtil;
    private final MissionService missionService;

    @Operation(summary = "회원가입")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(userService.register(request)));
    }

    @Operation(summary = "마이페이지 조회")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(userService.getMyInfo(userId)));
    }

    @Operation(summary = "회원 정보 수정")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyInfo(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UserUpdateRequest request) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(userService.updateMyInfo(userId, request)));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "작성 게시글 목록")
    @GetMapping("/me/myposts")
    public ResponseEntity<ApiResponse<List<PostListResponse>>> getMyPosts(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(userService.getMyPosts(userId)));
    }

    @Operation(summary = "작성 댓글 목록")
    @GetMapping("/me/mycomments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getMyComments(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(commentService.getMyComments(userId)));
    }

    @Operation(summary = "완료 미션 목록")
    @GetMapping("/me/missionlist")
    public ResponseEntity<ApiResponse<List<MissionListResponse>>> getMissionList(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(missionService.getMissionList(userId)));
    }

    @Operation(summary = "사진미션 제출 목록")
    @GetMapping("/me/photolist")
    public ResponseEntity<ApiResponse<List<PhotoListResponse>>> getPhotoList(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(missionService.getPhotoList(userId)));
    }

    @Operation(summary = "게임 이력 프로필")
    @GetMapping("/me/game-profile")
    public ResponseEntity<ApiResponse<GameProfileResponse>> getGameProfile(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(userService.getGameProfile(userId)));
    }
}