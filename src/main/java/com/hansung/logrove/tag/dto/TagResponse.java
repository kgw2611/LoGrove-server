package com.hansung.logrove.tag.dto;

import com.hansung.logrove.tag.entity.Tag;
import com.hansung.logrove.tag.entity.TagName;
import lombok.Getter;

@Getter
public class TagResponse {

    private final Long id;
    private final TagName name;

    private TagResponse(Long id, TagName name) {
        this.id = id;
        this.name = name;
    }

    public static TagResponse from(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}