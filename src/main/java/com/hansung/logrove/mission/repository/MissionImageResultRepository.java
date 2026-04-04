package com.hansung.logrove.mission.repository;

import com.hansung.logrove.mission.entity.MissionImageResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionImageResultRepository extends JpaRepository<MissionImageResult, Long> {

    List<MissionImageResult> findByUserId(Long userId);
}