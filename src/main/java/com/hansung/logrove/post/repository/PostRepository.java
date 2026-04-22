package com.hansung.logrove.post.repository;

import com.hansung.logrove.post.entity.BoardType;
import com.hansung.logrove.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p.id FROM Post p WHERE p.boardType = :boardType ORDER BY p.id DESC")
    Page<Long> findIdsByBoardType(@Param("boardType") BoardType boardType, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.tags pt " +
            "LEFT JOIN FETCH pt.tag " +
            "WHERE p.id IN :ids " +
            "ORDER BY p.id DESC")
    List<Post> findByIdsWithTags(@Param("ids") List<Long> ids);

    List<Post> findByUser_Id(Long userId);

    Page<Post> findByBoardTypeAndTitleContaining(BoardType boardType, String title, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p JOIN PostTag pt ON pt.post = p WHERE p.boardType = :boardType AND pt.tag.id IN :tagIds")
    Page<Post> findByBoardTypeAndTagIds(@Param("boardType") BoardType boardType, @Param("tagIds") List<Long> tagIds, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p JOIN PostTag pt ON pt.post = p WHERE p.boardType = :boardType AND p.title LIKE %:title% AND pt.tag.id IN :tagIds")
    Page<Post> findByBoardTypeAndTitleContainingAndTagIds(@Param("boardType") BoardType boardType, @Param("title") String title, @Param("tagIds") List<Long> tagIds, Pageable pageable);
}
