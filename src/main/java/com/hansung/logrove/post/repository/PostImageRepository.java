package com.hansung.logrove.post.repository;

import com.hansung.logrove.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    List<PostImage> findByPostId(Long postId);

    List<PostImage> findByPostUserIdAndPostBoardTypeBoard(Long userId, String board);
}
