package com.hansung.logrove.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostUpdateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}