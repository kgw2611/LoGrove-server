package com.hansung.logrove.comment.repository;

import com.hansung.logrove.comment.entity.CommentLike;
import com.hansung.logrove.comment.entity.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {

    Optional<CommentLike> findByUser_IdAndComment_Id(Long userId, Long commentId);

    boolean existsByUser_IdAndComment_Id(Long userId, Long commentId);
}
