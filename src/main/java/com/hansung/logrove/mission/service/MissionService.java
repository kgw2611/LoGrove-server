package com.hansung.logrove.mission.service;

import com.hansung.logrove.mission.dto.MissionResponse;
import com.hansung.logrove.mission.entity.Mission;
import com.hansung.logrove.mission.repository.MissionRepository;
import com.hansung.logrove.mission.repository.MissionStateRepository;
import com.hansung.logrove.mission.dto.MissionListResponse;
import com.hansung.logrove.mission.dto.PhotoListResponse;
import com.hansung.logrove.mission.repository.MissionImageResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final MissionStateRepository missionStateRepository;
    private final MissionImageResultRepository missionImageResultRepository;

    // 각 카테고리의 첫 번째 미션 ID (가입 즉시 접근 가능)
    private static final java.util.Set<Long> STARTING_MISSIONS = java.util.Set.of(1L, 10L, 19L, 28L);

    /**
     * [단계별 학습 리스트 조회]
     */
    public List<MissionResponse> getStairMissionList(Long userId) {
        List<Mission> missions = missionRepository.findAll();

        // N+1 방지: 유저의 모든 상태를 1번 쿼리로 로드
        java.util.Map<Long, String> stateMap = missionStateRepository.findByUserIdWithMission(userId).stream()
                .collect(Collectors.toMap(
                        ms -> ms.getMission().getId(),
                        ms -> ms.getState().name()
                ));

        return missions.stream()
                .filter(mission -> mission.getMissionStair() != null)
                .map(mission -> {
                    String state = stateMap.containsKey(mission.getId())
                            ? stateMap.get(mission.getId())
                            : (STARTING_MISSIONS.contains(mission.getId()) ? "INCOMPLETE" : "LOCKED");

                    return new MissionResponse(
                            mission.getId(),
                            mission.getQuestion(),
                            mission.getMissionStair().getStep() + "단계",
                            mission.getMissionStair().getLevel().ordinal(),
                            state,
                            mission.getMissionStair().getType(),
                            null,
                            mission.getPoint()
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * [사진 제출형 학습 리스트 조회]
     */
    public List<MissionResponse> getPhotoMissionList(Long userId) {
        List<Mission> missions = missionRepository.findAll();

        return missions.stream()
                .filter(mission -> mission.getMissionImage() != null)
                .map(mission -> {
                    String state = missionStateRepository.findByUserIdAndMissionId(userId, mission.getId())
                            .map(s -> s.getState().name())
                            .orElse("INCOMPLETE");
                    String theme = mission.getMissionImage().getTheme();

                    return new MissionResponse(
                            mission.getId(),
                            mission.getQuestion(),
                            mission.getMissionImage().getContent(),
                            photoLevel(mission.getPoint()),
                            state,
                            null,
                            MissionResponse.resolveSampleUrl(theme, mission.getMissionImage().getSampleUrl()),
                            mission.getPoint()
                    );
                })
                .collect(Collectors.toList());
    }

    private static int photoLevel(int point) {
        if (point >= 500) return 2;
        if (point >= 300) return 1;
        return 0;
    }

    // 완료 미션 목록 조회 - /api/users/me/missionlist
    public List<MissionListResponse> getMissionList(Long userId) {
        return missionStateRepository.findByUserId(userId).stream()
                .map(MissionListResponse::from)
                .collect(Collectors.toList());
    }

    // 사진미션 제출 목록 조회 - /api/users/me/photolist
    public List<PhotoListResponse> getPhotoList(Long userId) {
        return missionImageResultRepository.findByUserId(userId).stream()
                .map(PhotoListResponse::from)
                .collect(Collectors.toList());
    }
}
