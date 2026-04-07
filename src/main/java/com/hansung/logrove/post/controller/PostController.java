package com.hansung.logrove.post.controller;

import com.hansung.logrove.global.response.ApiResponse;
import com.hansung.logrove.global.jwt.JwtUtil;
import com.hansung.logrove.post.dto.PostCreateRequest;
import com.hansung.logrove.post.dto.PostListResponse;
import com.hansung.logrove.post.dto.PostResponse;
import com.hansung.logrove.post.dto.PostUpdateRequest;
import com.hansung.logrove.post.dto.*;
import com.hansung.logrove.post.entity.BoardType;
import com.hansung.logrove.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 API")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "게시글 작성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @RequestHeader("Authorization") String token,
            @Valid @ModelAttribute PostCreateRequest request,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(postService.createPost(userId, request, images)));
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getPost(postId)));
    }

    @Operation(summary = "게시판별 목록 조회 / 검색 (제목, 태그, 복합)")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostListResponse>>> getPosts(
            @RequestParam BoardType board,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<Long> tagIds,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(postService.searchPosts(board, title, tagIds, pageable)));
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(postService.updatePost(userId, postId, request)));
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId) {
        Long userId = jwtUtil.extractUserId(token);
        postService.deletePost(userId, postId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "게시글 좋아요")
    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<Void>> likePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId) {
        Long userId = jwtUtil.extractUserId(token);
        postService.likePost(userId, postId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "게시글 좋아요 취소")
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<Void>> unlikePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId) {
        Long userId = jwtUtil.extractUserId(token);
        postService.unlikePost(userId, postId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "내 게시글 목록")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<PostListResponse>>> getMyPosts(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(postService.getMyPosts(userId)));
    }
}