package com.hansung.logrove.comment.repository;

import com.hansung.logrove.comment.entity.CommentLike;
import com.hansung.logrove.comment.entity.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {

    Optional<CommentLike> findByUser_IdAndComment_Id(Long userId, Long commentId);

    boolean existsByUser_IdAndComment_Id(Long userId, Long commentId);

    @Query("""
            SELECT cl.comment.id
            FROM CommentLike cl
            WHERE cl.user.id = :userId AND cl.comment.id IN :commentIds
            """)
    List<Long> findLikedCommentIdsByUserIdAndCommentIds(
            @Param("userId") Long userId,
            @Param("commentIds") List<Long> commentIds);

    @Query("""
            SELECT cl.comment.id, COUNT(cl)
            FROM CommentLike cl
            WHERE cl.comment.id IN :commentIds
            GROUP BY cl.comment.id
            """)
    List<Object[]> countLikesByCommentIds(@Param("commentIds") List<Long> commentIds);
}
