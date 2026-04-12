package com.hansung.logrove.mission.service;

import com.hansung.logrove.gemini.dto.GeminiEvaluationResponse;
import com.hansung.logrove.gemini.service.ImageEvaluationService;
import com.hansung.logrove.mission.dto.MissionImageDetailResponse;
import com.hansung.logrove.mission.dto.MissionResultResponse;
import com.hansung.logrove.mission.entity.*;
import com.hansung.logrove.mission.repository.MissionImageRepository;
import com.hansung.logrove.mission.repository.MissionImageResultRepository;
import com.hansung.logrove.mission.repository.MissionStateRepository;
import com.hansung.logrove.storage.dto.ImageUploadResult;
import com.hansung.logrove.storage.service.ImageStorageService;
import com.hansung.logrove.user.entity.User;
import com.hansung.logrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final ImageEvaluationService imageEvaluationService;
    private final ImageStorageService imageStorageService;

    /**
     * [시나리오 5-1: 사진 학습 상세 조회]
     * 경로: GET /api/learning/{mission_id}/photo
     */
    public MissionImageDetailResponse getPhotoMissionDetail(Long missionId) {
        MissionImage missionImage = missionImageRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("사진 미션 정보를 찾을 수 없습니다. ID: " + missionId));
        return MissionImageDetailResponse.from(missionImage);
    }

    /**
     * [시나리오 5-2: 사진 제출 및 Gemini AI 분석]
     * 경로: POST /api/learning/{mission_id}/photo/submit
     */
    @Transactional
    public MissionResultResponse analyzeImage(Long userId, Long missionId, MultipartFile file) {

        // 1. 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        MissionImage missionImage = missionImageRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션 정보를 찾을 수 없습니다."));

        System.out.println("1. entity load success");

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("제출된 사진 파일이 없습니다.");
        }

        // 2. Gemini AI 채점 먼저 (파일 읽기)
        GeminiEvaluationResponse evaluation = imageEvaluationService.evaluate(
                file,
                missionImage.getTheme(),
                missionImage.getContent(),
                missionImage.getGuide(),
                missionImage.getPassScore()
        );

        System.out.println("2. gemini evaluate success");
        System.out.println("score = " + evaluation.getScore());
        System.out.println("passed = " + evaluation.isPassed());

        // 3. 채점 후 파일 저장 (transferTo 호출)
        ImageUploadResult uploadResult = imageStorageService.storeSubmissionImage(userId, file);
        System.out.println("3. image upload success");
        System.out.println("url = " + uploadResult.getUrl());

        MissionResultStatus status = evaluation.isPassed()
                ? MissionResultStatus.SUCCESS : MissionResultStatus.FAIL;

        // 4. 분석 결과 DB 저장
        MissionImageResult resultRecord = MissionImageResult.builder()
                .score(evaluation.getScore())
                .result(status)
                .user(user)
                .mission(missionImage.getMission())
                .resultUrl(uploadResult.getUrl())
                .build();

        missionImageResultRepository.save(resultRecord);
        System.out.println("4. result save success");

        // 5. 미션 성공 시 유저 상태 COMPLETED로 변경
        if (status == MissionResultStatus.SUCCESS) {
            updateUserMissionStatus(userId, missionId);
            System.out.println("5. mission state update success");
        }

        // 6. 결과 반환
        return MissionResultResponse.builder()
                .score(evaluation.getScore())
                .feedback(evaluation.getFeedback())
                .isSuccess(evaluation.isPassed())
                .build();
    }

    private void updateUserMissionStatus(Long userId, Long missionId) {
        missionStateRepository.findByUserIdAndMissionId(userId, missionId)
                .ifPresent(state -> state.updateStatus(MissionStatus.COMPLETED));
    }
}