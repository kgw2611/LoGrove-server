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

    /**
     * [단계별 학습 리스트 조회]
     */
    public List<MissionResponse> getStairMissionList(Long userId) {
        List<Mission> missions = missionRepository.findAll();

        return missions.stream()
                .filter(mission -> mission.getMissionStair() != null)
                .map(mission -> {
                    String state = missionStateRepository.findByUserIdAndMissionId(userId, mission.getId())
                            .map(s -> s.getState().name())
                            .orElse("INCOMPLETE");

                    return new MissionResponse(
                            mission.getId(),
                            mission.getQuestion(),
                            mission.getMissionStair().getStep() + "단계",
                            mission.getMissionStair().getLevel().ordinal(),
                            state,
                            mission.getMissionStair().getType() // MULTIPLE_CHOICE or SHORT_ANSWER
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

                    return new MissionResponse(
                            mission.getId(),
                            mission.getMissionImage().getTheme(),
                            mission.getMissionImage().getContent(),
                            0,
                            state,
                            null // 사진 미션은 StairType 없음
                    );
                })
                .collect(Collectors.toList());
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