package com.hansung.logrove.mission.repository;

import com.hansung.logrove.mission.entity.MissionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionStateRepository extends JpaRepository<MissionState, MissionState.MissionStateId> {

    Optional<MissionState> findByUserIdAndMissionId(Long userId, Long missionId);

    List<MissionState> findByUserId(Long userId);

    @Query("SELECT ms FROM MissionState ms JOIN FETCH ms.mission WHERE ms.user.id = :userId")
    List<MissionState> findByUserIdWithMission(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO mission_state (mission_id, user_id, state) VALUES (:missionId, :userId, 'COMPLETED') ON DUPLICATE KEY UPDATE state = 'COMPLETED'", nativeQuery = true)
    void upsertCompleted(@Param("missionId") Long missionId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT IGNORE INTO mission_state (mission_id, user_id, state) VALUES (:missionId, :userId, 'INCOMPLETE')", nativeQuery = true)
    void insertIncompleteIfAbsent(@Param("missionId") Long missionId, @Param("userId") Long userId);
}