package com.hansung.logrove.post.repository;

import com.hansung.logrove.post.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> findByPostId(Long postId);

    // 특정 태그가 달린 게시글 ID 목록 조회 — 태그 검색 기능에서 사용
    List<PostTag> findByTagId(Long tagId);
}