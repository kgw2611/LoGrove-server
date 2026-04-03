package com.hansung.logrove.mission.repository;

import com.hansung.logrove.mission.entity.MissionImageResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionImageResultRepository extends JpaRepository<MissionImageResult, Long> {
}