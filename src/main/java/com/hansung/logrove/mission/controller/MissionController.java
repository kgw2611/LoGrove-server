package com.hansung.logrove.mission.controller;

import com.hansung.logrove.mission.dto.MissionResponse;
import com.hansung.logrove.mission.dto.MissionResultResponse;
import com.hansung.logrove.mission.dto.MissionStairDetailResponse;
import com.hansung.logrove.mission.dto.MissionSubmitRequest;
import com.hansung.logrove.mission.entity.MissionImage;
import com.hansung.logrove.mission.service.MissionImageService;
import com.hansung.logrove.mission.service.MissionService;
import com.hansung.logrove.mission.service.MissionStairService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;
    private final MissionStairService missionStairService;
    private final MissionImageService missionImageService; // 설계도 서비스 주입

    /**
     * [단계별 학습 메인 리스트 조회]
     * 경로: GET /api/learning/stair
     */
    @GetMapping("/stair")
    public ResponseEntity<List<MissionResponse>> getStairMissions() {
        // TODO: 추후 실제 로그인 유저 ID로 변경 (현재 테스트용 1L)
        return ResponseEntity.ok(missionService.getStairMissionList(1L));
    }

    /**
     * [명세서 반영 - 단계별 학습 객관식 화면]
     * 경로: GET /api/learning/stair/{mission_id}/multiple
     */
    @GetMapping("/stair/{mission_id}/multiple")
    public ResponseEntity<MissionStairDetailResponse> getMultipleQuiz(@PathVariable("mission_id") Long missionId) {
        return ResponseEntity.ok(missionStairService.getStairContent(missionId));
    }

    /**
     * [명세서 반영 - 단계별 학습 객관식 제출]
     * 경로: POST /api/learning/stair/{mission_id}/multiple/submit
     */
    @PostMapping("/stair/{mission_id}/multiple/submit")
    public ResponseEntity<Boolean> submitMultiple(@PathVariable("mission_id") Long missionId,
                                                  @RequestBody MissionSubmitRequest request) {
        // 명세서에 정의된 경로의 missionId를 우선 사용합니다.
        return ResponseEntity.ok(missionStairService.submitAnswer(1L, missionId, request));
    }

    /**
     * [명세서 반영 - 단계별 학습 단답식 화면]
     * 경로: GET /api/learning/stair/{mission_id}/short
     */
    @GetMapping("/stair/{mission_id}/short")
    public ResponseEntity<MissionStairDetailResponse> getShortQuiz(@PathVariable("mission_id") Long missionId) {
        return ResponseEntity.ok(missionStairService.getStairContent(missionId));
    }

    /**
     * [명세서 반영 - 단계별 학습 단답식 제출]
     * 경로: POST /api/learning/stair/{mission_id}/short/submit
     */
    @PostMapping("/stair/{mission_id}/short/submit")
    public ResponseEntity<Boolean> submitShort(@PathVariable("mission_id") Long missionId,
                                               @RequestBody MissionSubmitRequest request) {
        return ResponseEntity.ok(missionStairService.submitAnswer(1L, missionId, request));
    }

    /**
     * [사진 제출형 학습 진입 및 리스트 조회]
     * 경로: GET /api/learning/photo
     */
    @GetMapping("/photo")
    public ResponseEntity<List<MissionResponse>> getPhotoMissions() {
        return ResponseEntity.ok(missionService.getPhotoMissionList(1L));
    }

    /**
     * [명세서 반영 - 사진 학습 상세 화면]
     * 경로: GET /api/learning/{mission_id}/photo
     * 역할: 사진 제출형 학습(3분할 구도 등)의 가이드와 주제를 반환합니다.
     */
    @GetMapping("/{mission_id}/photo")
    public ResponseEntity<MissionImage> getPhotoMissionDetail(@PathVariable("mission_id") Long missionId) {
        // 설계도 서비스 호출
        MissionImage response = missionImageService.getPhotoMissionDetail(missionId);

        // 프론트엔드는 이 데이터를 받아 팝업창에 주제와 설명을 띄웁니다.
        return ResponseEntity.ok(response);
    }

    /**
     * [사진 학습 제출]
     * 명세서 경로: POST /api/learning/{mission_id}/photo/submit
     */
    @PostMapping(value = "/{mission_id}/photo/submit", consumes = "multipart/form-data")
    public ResponseEntity<MissionResultResponse> submitPhoto(
            @PathVariable("mission_id") Long missionId,
            @RequestParam("file") MultipartFile file) {

        // 설계도 서비스 호출하여 결과 DTO 반환
        MissionResultResponse response = missionImageService.analyzeImage(1L, missionId, file);

        return ResponseEntity.ok(response);
    }
}