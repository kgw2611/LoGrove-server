package com.hansung.logrove.comment.repository;

import com.hansung.logrove.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 댓글 목록을 작성 시간 오름차순으로 조회
    // → 오래된 댓글이 위에 표시되는 일반적인 댓글 정렬 방식
    // 최상위 댓글만 조회 (parent가 null인 것만)
    List<Comment> findByPost_IdAndParentIsNullOrderByCreatedAtAsc(Long postId);

    @Query("""
            SELECT c FROM Comment c
            JOIN FETCH c.post p
            JOIN FETCH p.boardType
            JOIN FETCH c.user
            WHERE p.id = :postId AND c.parent IS NULL
            ORDER BY c.createdAt ASC
            """)
    List<Comment> findTopLevelByPostIdWithPostAndUser(@Param("postId") Long postId);

    @Query("""
            SELECT c FROM Comment c
            JOIN FETCH c.post p
            JOIN FETCH p.boardType
            JOIN FETCH c.user
            JOIN FETCH c.parent parent
            WHERE parent.id IN :parentIds
            ORDER BY c.createdAt ASC
            """)
    List<Comment> findRepliesByParentIdsWithPostAndUser(@Param("parentIds") List<Long> parentIds);

    // 특정 유저가 작성한 댓글 목록 조회 (마이페이지 - 작성 댓글 목록)
    List<Comment> findByUser_Id(Long userId);
}
