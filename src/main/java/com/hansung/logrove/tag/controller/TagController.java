package com.hansung.logrove.tag.controller;

import com.hansung.logrove.global.response.ApiResponse;
import com.hansung.logrove.tag.dto.TagResponse;
import com.hansung.logrove.tag.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(name = "Tag", description = "태그 API")
public class TagController {

    private final TagService tagService;

    // 전체 태그 목록 조회 - 인증 불필요
    @Operation(summary = "전체 태그 목록 조회")
    @GetMapping
    public ApiResponse<List<TagResponse>> getAllTags() {
        return ApiResponse.ok(tagService.getAllTags());
    }
}