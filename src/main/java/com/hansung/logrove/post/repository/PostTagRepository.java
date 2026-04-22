package com.hansung.logrove.post.repository;

import com.hansung.logrove.post.entity.PostTag;
import com.hansung.logrove.post.entity.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {

    List<PostTag> findByPost_Id(Long postId);

    List<PostTag> findByTag_Id(Long tagId);
}
