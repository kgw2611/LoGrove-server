package com.hansung.logrove.mission.service;

import com.hansung.logrove.mission.dto.MissionResultResponse;
import com.hansung.logrove.mission.entity.*;
import com.hansung.logrove.mission.repository.MissionImageRepository;
import com.hansung.logrove.mission.repository.MissionImageResultRepository;
import com.hansung.logrove.mission.repository.MissionStateRepository;
import com.hansung.logrove.user.entity.User;
import com.hansung.logrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * [설계도 명칭 준수] 사진 제출형 학습(MissionImage)의 비즈니스 로직 및 Gemini AI 분석을 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionImageService {

    private final MissionImageRepository missionImageRepository;
    private final MissionImageResultRepository missionImageResultRepository;
    private final MissionStateRepository missionStateRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}") // application.properties에 설정된 API 키를 가져옵니다.
    private String apiKey;

    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-pro-preview:generateContent?key=";

    /**
     * [시나리오 5-1: 사진 학습 상세 조회]
     * 경로: GET /api/learning/{mission_id}/photo
     * 역할: 미션 주제, 가이드, 예시 이미지 URL 등을 반환합니다.
     */
    public MissionImage getPhotoMissionDetail(Long missionId) {
        return missionImageRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("사진 미션 정보를 찾을 수 없습니다. ID: " + missionId));
    }

    /**
     * [시나리오 5-2: 사진 제출 및 Gemini AI 분석]
     * 경로: POST /api/learning/{mission_id}/photo/submit
     * 역할: 이미지를 Gemini API로 분석하고, 결과를 MissionImageResult 엔티티에 저장합니다.
     */
    @Transactional
    public MissionResultResponse analyzeImage(Long userId, Long missionId, MultipartFile file) {

        // 1. 엔티티 데이터 조회 (연관 관계 설정을 위해 필요)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        MissionImage missionImage = missionImageRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션 정보를 찾을 수 없습니다."));

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("제출된 사진 파일이 없습니다.");
        }

        try {
            // 2. Gemini API 전송을 위한 이미지 Base64 인코딩
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

            // 3. 호진님이 정의한 프롬프트 구성 (Scoring 및 JSON 출력 강제)
            String prompt = String.format(
                    "You are a strict photography judge. Evaluate the image concept: \"%s\"\n" +
                            "Scoring: 0-100. If score >= %d then PASS, else FAIL.\n" +
                            "Explain in Korean (1-2 sentences).\n" +
                            "Output JSON only: {\"concept\": \"%s\", \"score\": 0, \"reason\": \"...\", \"result\": \"PASS or FAIL\"}",
                    missionImage.getTheme(), missionImage.getPassScore(), missionImage.getTheme()
            );

            // 4. Gemini API 호출 (멀티모달 요청 구성 및 전송)
            Map<String, Object> aiResult = callGeminiApi(prompt, base64Image);

            int score = (int) aiResult.get("score");
            String reason = (String) aiResult.get("reason");
            String passOrFail = (String) aiResult.get("result"); // "PASS" 또는 "FAIL"

            MissionResultStatus status = "PASS".equalsIgnoreCase(passOrFail)
                    ? MissionResultStatus.SUCCESS : MissionResultStatus.FAIL;

            // 5. [설계도 반영] 분석 결과 DB 저장 (MissionImageResult 엔티티)
            // 실제 구현 시 resultUrl에는 S3 업로드 경로를 넣어야 합니다.
            MissionImageResult resultRecord = MissionImageResult.builder()
                    .score(score)
                    .result(status)
                    .user(user)
                    .mission(missionImage.getMission())
                    .resultUrl("https://s3.logrove.com/uploads/" + file.getOriginalFilename())
                    .build();

            missionImageResultRepository.save(resultRecord);

            // 6. 미션 성공 시 유저 상태를 COMPLETED(보라색)로 변경
            if (status == MissionResultStatus.SUCCESS) {
                updateUserMissionStatus(userId, missionId);
            }

            // 7. [설계도 반영] MissionResultResponse DTO 반환
            return MissionResultResponse.builder()
                    .feedback(reason)
                    .isSuccess(status == MissionResultStatus.SUCCESS)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("AI 분석 서비스 호출 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * Gemini API 실제 통신 로직 (JSON 페이로드 구성 및 전송)
     */
    private Map<String, Object> callGeminiApi(String prompt, String base64Image) {
        // Gemini API가 요구하는 복잡한 JSON 구조를 구성합니다.
        // { "contents": [{ "parts": [{ "text": "..." }, { "inline_data": { "mime_type": "image/jpeg", "data": "..." }}]}] }

        // 실제 운영 시에는 ObjectMapper 등을 활용해 더 정교하게 구성해야 합니다.
        // 현재는 테스트용 모킹 데이터로 로직 흐름만 유지합니다.
        return Map.of(
                "score", 85,
                "reason", "3분할 선의 교차점에 피사체가 잘 배치되어 안정감이 느껴집니다.",
                "result", "PASS"
        );
    }

    /**
     * 유저의 미션 상태 정보를 업데이트하는 내부 메서드
     */
    private void updateUserMissionStatus(Long userId, Long missionId) {
        missionStateRepository.findByUserIdAndMissionId(userId, missionId)
                .ifPresent(state -> state.updateStatus(MissionStatus.COMPLETED));
    }
}