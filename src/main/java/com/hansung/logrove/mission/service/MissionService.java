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

    // 설계도에 명시된 Repository들을 주입받습니다.
    private final MissionRepository missionRepository;
    private final MissionStateRepository missionStateRepository;
    private final MissionImageResultRepository missionImageResultRepository;

    /**
     * [시나리오 2: 단계별 학습 리스트 조회]
     * MISSIONS_STAIR(성장형 미션) 정보를 기반으로 리스트를 생성합니다.
     */
    public List<MissionResponse> getStairMissionList(Long userId) {
        // 1. DB에서 모든 미션을 가져옵니다. (실제 운영 시에는 '성장형' 타입만 필터링합니다.)
        List<Mission> missions = missionRepository.findAll();

        // 2. 각 미션 엔티티를 MissionResponse DTO로 변환합니다.
        return missions.stream()
                .filter(mission -> mission.getMissionStair() != null) // stair 미션만
                .map(mission -> {
                    // ERD의 MISSION_STATE 테이블에서 유저의 완료 여부를 확인합니다.
                    // 데이터가 없으면 아직 미완료(INCOMPLETE) 상태로 정의합니다.
                    String state = missionStateRepository.findByUserIdAndMissionId(userId, mission.getId())
                            .map(s -> s.getState().name())
                            .orElse("INCOMPLETE");

                    // DTO 생성 시 각 테이블의 컬럼을 매핑합니다.
                    return new MissionResponse(
                            mission.getId(),                  // MISSIONS.mission_id
                            mission.getQuestion(),                    // MISSIONS.question (미션 제목)
                            mission.getMissionStair().getStep() + "단계", // MISSIONS_STAIR.step (설명)
                            mission.getMissionStair().getLevel().ordinal(), // MISSIONS_STAIR.level (난이도)
                            state                                     // 보라색/초록색 결정을 위한 상태값
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * [시나리오 2: 사진 제출형 학습 리스트 조회]
     * MISSIONS_IMAGE(사진 미션) 정보를 기반으로 리스트를 생성합니다.
     */
    public List<MissionResponse> getPhotoMissionList(Long userId) {
        // 1. 모든 미션을 가져옵니다. (실제 운영 시에는 '사진형' 타입만 필터링합니다.)
        List<Mission> missions = missionRepository.findAll();

        return missions.stream()
                .filter(mission -> mission.getMissionImage() != null) // photo 미션만
                .map(mission -> {
                    // 유저별 진행 상태 조회 (단계별 학습과 동일한 로직 공유)
                    String state = missionStateRepository.findByUserIdAndMissionId(userId, mission.getId())
                            .map(s -> s.getState().name())
                            .orElse("INCOMPLETE");

                    // 사진 미션의 특성에 맞는 컬럼들을 매핑합니다.
                    return new MissionResponse(
                            mission.getId(),                  // MISSIONS.mission_id
                            mission.getMissionImage().getTheme(),     // MISSIONS_IMAGE.theme (미션 주제)
                            mission.getMissionImage().getContent(),   // MISSIONS_IMAGE.content (미션 내용)
                            0,                                        // 사진 미션은 레벨 구분이 없으므로 0 세팅
                            state                                     // 상태값 전달
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