package com.hansung.logrove.mission.repository;

import com.hansung.logrove.mission.entity.MissionStairContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionStairContentRepository extends JpaRepository<MissionStairContent, Long> {
}