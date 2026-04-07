package com.hansung.logrove.mission.service;

import com.hansung.logrove.mission.dto.MissionStairDetailResponse;
import com.hansung.logrove.mission.dto.MissionSubmitRequest;
import com.hansung.logrove.mission.entity.*;
import com.hansung.logrove.mission.repository.MissionStairContentRepository;
import com.hansung.logrove.mission.repository.MissionStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionStairService {

    private final MissionStairContentRepository contentRepository;
    private final MissionStateRepository missionStateRepository;

    /**
     * 상세 퀴즈 데이터 조회 (DTO 변환)
     */
    public MissionStairDetailResponse getStairContent(Long missionId) {
        MissionStairContent content = contentRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션 상세 정보가 없습니다. ID: " + missionId));

        return MissionStairDetailResponse.builder()
                .missionId(content.getId())
                .question(content.getMissionStair().getMission().getQuestion())
                .type(content.getMissionStair().getType())
                .radio1(content.getRadio1())
                .radio2(content.getRadio2())
                .radio3(content.getRadio3())
                .radio4(content.getRadio4())
                .commentary(content.getCommentary())
                .imageUrl(content.getImageUrl())
                .build();
    }

    /**
     * 정답 제출 및 채점 로직
     */
    @Transactional
    public boolean submitAnswer(Long userId, Long missionId, MissionSubmitRequest request) {
        MissionStairContent content = contentRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션 상세 정보가 없습니다. ID: " + missionId));

        // DB 정답과 유저 제출 답안 비교 (공백 제거 및 대소문자 무시)
        boolean isCorrect = content.getAnswer().trim().equalsIgnoreCase(request.getSubmittedAnswer().trim());

        // 정답이면 미션 상태를 COMPLETED로 변경
        if (isCorrect) {
            updateMissionStatus(userId, missionId);
        }

        return isCorrect;
    }

    private void updateMissionStatus(Long userId, Long missionId) {
        missionStateRepository.findByUserIdAndMissionId(userId, missionId)
                .ifPresent(state -> state.updateStatus(MissionStatus.COMPLETED));
    }
}