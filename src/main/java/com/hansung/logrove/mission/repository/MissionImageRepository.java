package com.hansung.logrove.mission.repository;

import com.hansung.logrove.mission.entity.MissionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionImageRepository extends JpaRepository<MissionImage, Long> {
    // 기본 상속만으로 findById 사용 가능 (시나리오 5-1 상세 조회용)
}