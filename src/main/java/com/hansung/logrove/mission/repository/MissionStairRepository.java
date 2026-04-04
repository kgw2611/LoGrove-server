package com.hansung.logrove.mission.repository;

import com.hansung.logrove.mission.entity.MissionStair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * [설계도 명칭 준수] 단계별 학습(MissionStair)의 메타데이터를 관리하는 리포지토리입니다.
 */
@Repository
public interface MissionStairRepository extends JpaRepository<MissionStair, Long> {
    // 기본 상속(findById, save 등)만으로도 시나리오 1~4를 수행하는 데 충분합니다.
}