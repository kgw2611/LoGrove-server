package com.hansung.logrove.mission.repository;

import com.hansung.logrove.mission.entity.MissionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionStateRepository extends JpaRepository<MissionState, MissionState.MissionStateId> {

    Optional<MissionState> findByUserIdAndMissionId(Long userId, Long missionId);

    List<MissionState> findByUserId(Long userId);
}