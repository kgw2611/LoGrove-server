package com.hansung.logrove.mission.service;

import com.hansung.logrove.mission.dto.MissionStairDetailResponse;
import com.hansung.logrove.mission.dto.MissionSubmitRequest;
import com.hansung.logrove.mission.entity.*;
import com.hansung.logrove.mission.repository.MissionStairContentRepository;
import com.hansung.logrove.mission.repository.MissionStateRepository;
import com.hansung.logrove.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionStairService {

    private final MissionStairContentRepository contentRepository;
    private final MissionStateRepository missionStateRepository;
    private final UserService userService;

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

        try {
            if (isCorrect) {
                boolean alreadyCompleted = missionStateRepository
                        .findByUserIdAndMissionId(userId, missionId)
                        .map(s -> s.getState() == com.hansung.logrove.mission.entity.MissionStatus.COMPLETED)
                        .orElse(false);

                missionStateRepository.upsertCompleted(missionId, userId);
                unlockNextMission(missionId, userId);

                if (!alreadyCompleted) {
                    try {
                        int point = content.getMissionStair().getMission().getPoint();
                        userService.addExp(userId, point);
                    } catch (Exception e) {
                        log.error("경험치 지급 실패 missionId={} userId={}", missionId, userId, e);
                    }
                }
            } else {
                missionStateRepository.insertIncompleteIfAbsent(missionId, userId);
            }
        } catch (Exception e) {
            log.error("mission_state 저장 실패 missionId={} userId={}", missionId, userId, e);
        }

        return isCorrect;
    }

    // 카테고리 마지막 미션: 9, 18, 27, 36 → 다음 언락 없음
    private static final java.util.Set<Long> CATEGORY_LAST = java.util.Set.of(9L, 18L, 27L, 36L);

    private void unlockNextMission(Long missionId, Long userId) {
        if (CATEGORY_LAST.contains(missionId)) return;
        try {
            missionStateRepository.insertIncompleteIfAbsent(missionId + 1, userId);
        } catch (Exception e) {
            log.error("다음 미션 언락 실패 nextMissionId={} userId={}", missionId + 1, userId, e);
        }
    }
}