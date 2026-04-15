package com.hansung.logrove.gemini.controller;

import com.hansung.logrove.gemini.dto.GeminiTagResponse;
import com.hansung.logrove.gemini.service.ClipTaggingService;
import com.hansung.logrove.gemini.service.ImageTaggingService;
import com.hansung.logrove.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gemini")
public class GeminiController {

    private final ImageTaggingService imageTaggingService;
    private final ClipTaggingService clipTaggingService;

    // 태그 추천 버튼 클릭 시 호출
    // POST /api/gemini/tags
    @PostMapping(value = "/tags", consumes = "multipart/form-data")
    public ApiResponse<GeminiTagResponse> recommendTags(
            @RequestParam("file") MultipartFile file) {
//        return ApiResponse.ok(imageTaggingService.recommendTags(file));
        return ApiResponse.ok(clipTaggingService.recommendTags(file));
    }
}