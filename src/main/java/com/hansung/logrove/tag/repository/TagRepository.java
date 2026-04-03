package com.hansung.logrove.tag.repository;

import com.hansung.logrove.tag.entity.Tag;
import com.hansung.logrove.tag.entity.TagName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(TagName name);

    boolean existsByName(TagName name);
}