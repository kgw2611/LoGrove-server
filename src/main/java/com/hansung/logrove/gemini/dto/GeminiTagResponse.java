package com.hansung.logrove.gemini.dto;

import com.hansung.logrove.tag.entity.TagName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GeminiTagResponse {

    private List<TagName> tags;
}