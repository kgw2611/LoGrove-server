package com.hansung.logrove.post.dto;

import com.hansung.logrove.post.entity.BoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class PostCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private BoardType boardType;

    // 태그 ID 목록 — Vision API 자동 태그 + 사용자 직접 선택 태그 모두 포함
    private List<Long> tagIds;
}