package com.hansung.logrove.post.repository;

import com.hansung.logrove.post.entity.PostLike;
import com.hansung.logrove.post.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {

    Optional<PostLike> findByUser_IdAndPost_Id(Long userId, Long postId);

    boolean existsByUser_IdAndPost_Id(Long userId, Long postId);
}
