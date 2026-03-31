package com.hansung.logrove.comment.repository;

import com.hansung.logrove.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    // 특정 유저가 특정 댓글에 좋아요를 눌렀는지 확인
    // → 좋아요 중복 방지 및 좋아요 취소 시 대상 엔티티 조회에 사용
    Optional<CommentLike> findByUser_IdAndComment_Id(Long userId, Long commentId);

    // 특정 유저가 특정 댓글에 좋아요를 눌렀는지 여부만 확인 (boolean 반환)
    // → existsBy~ 는 COUNT 쿼리 대신 EXISTS 쿼리를 실행하므로 성능상 유리
    boolean existsByUser_IdAndComment_Id(Long userId, Long commentId);
}