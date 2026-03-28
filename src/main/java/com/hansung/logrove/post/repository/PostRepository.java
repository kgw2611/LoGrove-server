package com.hansung.logrove.post.repository;

import com.hansung.logrove.post.entity.BoardType;
import com.hansung.logrove.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시판 종류별 글 목록 조회 (페이징)
    Page<Post> findByBoardType(BoardType boardType, Pageable pageable);

    // 내가 작성한 글 목록
    List<Post> findByUserId(Long userId);

    // 제목 검색
    Page<Post> findByTitleContaining(String title, Pageable pageable);
}