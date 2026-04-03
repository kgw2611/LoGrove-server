package com.hansung.logrove.mission.repository;

import com.hansung.logrove.mission.entity.MissionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MissionStateRepository extends JpaRepository<MissionState, MissionState.MissionStateId> {

    /**
     * [최종 수정된 메서드 명]
     * MissionState 엔티티 내부의 user 필드의 id 필드,
     * 그리고 mission 필드의 id 필드를 찾는다는 의미입니다.
     */
    Optional<MissionState> findByUserIdAndMissionId(Long userId, Long missionId);
}