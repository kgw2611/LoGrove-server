package com.hansung.logrove.mission.controller;

import com.hansung.logrove.global.jwt.JwtUtil;
import com.hansung.logrove.global.response.ApiResponse;
import com.hansung.logrove.mission.dto.CasualResultResponse;
import com.hansung.logrove.mission.service.CasualPhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photo/casual")
@RequiredArgsConstructor
@Tag(name = "CasualPhoto", description = "자유 사진 채점 API")
public class CasualPhotoController {

    private final CasualPhotoService service;
    private final JwtUtil jwtUtil;

    @Operation(summary = "자유 사진 채점 제출")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CasualResultResponse> evaluate(
            @RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file) {
        Long userId = jwtUtil.extractUserId(token);
        return ApiResponse.ok(service.evaluate(userId, file));
    }
}
