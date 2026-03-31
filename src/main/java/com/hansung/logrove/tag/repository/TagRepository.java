package com.hansung.logrove.tag.repository;

import com.hansung.logrove.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    // 태그명으로 조회 — 없으면 새로 생성하는 find-or-create 패턴에서 사용
    Optional<Tag> findByName(String name);

    boolean existsByName(String name);
}