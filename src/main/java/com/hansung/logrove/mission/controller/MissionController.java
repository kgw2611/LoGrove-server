package com.hansung.logrove.mission.controller;

import com.hansung.logrove.global.jwt.JwtUtil;
import com.hansung.logrove.global.response.ApiResponse;
import com.hansung.logrove.mission.dto.*;
import com.hansung.logrove.mission.service.MissionImageService;
import com.hansung.logrove.mission.service.MissionService;
import com.hansung.logrove.mission.service.MissionStairService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;
    private final MissionStairService missionStairService;
    private final MissionImageService missionImageService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "단계별 학습 메인 리스트 조회")
    // GET /api/learning/stair
    @GetMapping("/stair")
    public ApiResponse<List<MissionResponse>> getStairMissions(
            @RequestHeader("Authorization") String token) {
        Long userId = extractUserIdFromHeader(token);
        return ApiResponse.ok(missionService.getStairMissionList(userId));
    }

    @Operation(summary = "단계별 학습 객관식 화면")
    // GET /api/learning/stair/{mission_id}/multiple
    @GetMapping("/stair/{mission_id}/multiple")
    public ApiResponse<MissionStairDetailResponse> getMultipleQuiz(
            @PathVariable("mission_id") Long missionId) {
        return ApiResponse.ok(missionStairService.getStairContent(missionId));
    }

    @Operation(summary = "단계별 학습 객관식 제출")
    // POST /api/learning/stair/{mission_id}/multiple/submit
    @PostMapping("/stair/{mission_id}/multiple/submit")
    public ApiResponse<Boolean> submitMultiple(
            @PathVariable("mission_id") Long missionId,
            @RequestHeader("Authorization") String token,
            @RequestBody MissionSubmitRequest request) {
        Long userId = extractUserIdFromHeader(token);
        return ApiResponse.ok(missionStairService.submitAnswer(userId, missionId, request));
    }

    @Operation(summary = "단계별 학습 단답식 화면")
    // GET /api/learning/stair/{mission_id}/short
    @GetMapping("/stair/{mission_id}/short")
    public ApiResponse<MissionStairDetailResponse> getShortQuiz(
            @PathVariable("mission_id") Long missionId) {
        return ApiResponse.ok(missionStairService.getStairContent(missionId));
    }

    @Operation(summary = "단계별 학습 단답식 제출")
    // POST /api/learning/stair/{mission_id}/short/submit
    @PostMapping("/stair/{mission_id}/short/submit")
    public ApiResponse<Boolean> submitShort(
            @PathVariable("mission_id") Long missionId,
            @RequestHeader("Authorization") String token,
            @RequestBody MissionSubmitRequest request) {
        Long userId = extractUserIdFromHeader(token);
        return ApiResponse.ok(missionStairService.submitAnswer(userId, missionId, request));
    }

    @Operation(summary = "사진 제출형 학습 리스트 조회")
    // GET /api/learning/photo
    @GetMapping("/photo")
    public ApiResponse<List<MissionResponse>> getPhotoMissions(
            @RequestHeader("Authorization") String token) {
        Long userId = extractUserIdFromHeader(token);
        return ApiResponse.ok(missionService.getPhotoMissionList(userId));
    }

    @Operation(summary = "사진 학습 상세 화면")
    // GET /api/learning/{mission_id}/photo
    @GetMapping("/{mission_id}/photo")
    public ApiResponse<MissionImageDetailResponse> getPhotoMissionDetail(
            @PathVariable("mission_id") Long missionId) {
        return ApiResponse.ok(missionImageService.getPhotoMissionDetail(missionId));
    }

    @Operation(summary = "사진 학습 제출")
    // POST /api/learning/{mission_id}/photo/submit
    @PostMapping(value = "/{mission_id}/photo/submit", consumes = "multipart/form-data")
    public ApiResponse<MissionResultResponse> submitPhoto(
            @PathVariable("mission_id") Long missionId,
            @RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file) {
        Long userId = extractUserIdFromHeader(token);
        return ApiResponse.ok(missionImageService.analyzeImage(userId, missionId, file));
    }

    private Long extractUserIdFromHeader(String authorizationHeader) {
        String accessToken = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : authorizationHeader;
        return jwtUtil.extractUserId(accessToken);
    }
}