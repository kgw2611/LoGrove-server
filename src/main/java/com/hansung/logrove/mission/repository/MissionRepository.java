package com.hansung.logrove.mission.repository;

import com.hansung.logrove.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    // 모든 미션을 가져옵니다.
    // 나중에 성능 최적화가 필요하면 페이징(Paging) 처리를 추가할 수 있습니다.
    List<Mission> findAll();
}