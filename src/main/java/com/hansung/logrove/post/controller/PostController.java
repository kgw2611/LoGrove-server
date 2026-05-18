package com.hansung.logrove.post.controller;

import com.hansung.logrove.global.jwt.JwtUtil;
import com.hansung.logrove.global.response.ApiResponse;
import com.hansung.logrove.post.dto.NeighborPostsResponse;
import com.hansung.logrove.post.dto.PostCreateRequest;
import com.hansung.logrove.post.dto.PostListResponse;
import com.hansung.logrove.post.dto.PostResponse;
import com.hansung.logrove.post.dto.PostUpdateRequest;
import com.hansung.logrove.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "Post API")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Create post multipart")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @RequestHeader("Authorization") String token,
            @Valid @ModelAttribute PostCreateRequest request,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(postService.createPost(userId, request, images)));
    }

    @Operation(summary = "Create post JSON")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PostResponse>> createPostJson(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody PostCreateRequest request) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(postService.createPost(userId, request, null)));
    }

    @Operation(summary = "Upload inline post image")
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadInlineImage(
            @RequestHeader("Authorization") String token,
            @RequestParam("image") MultipartFile image) {
        Long userId = jwtUtil.extractUserId(token);
        String url = postService.saveInlineImage(userId, image);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("url", url)));
    }

    @Operation(summary = "Get post detail")
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(
            @PathVariable Long postId,
            @RequestHeader(value = "Authorization", required = false) String token) {
        Long userId = token != null ? jwtUtil.extractUserId(token) : null;
        return ResponseEntity.ok(ApiResponse.ok(postService.getPost(postId, userId)));
    }

    @Operation(summary = "Get popular posts")
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<PostListResponse>>> getPopularPosts(
            @RequestParam String board,
            @RequestParam(required = false) Integer days) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getPopularPosts(board, days)));
    }

    @Operation(summary = "Get neighboring posts")
    @GetMapping("/{postId}/neighbors")
    public ResponseEntity<ApiResponse<NeighborPostsResponse>> getNeighbors(
            @PathVariable Long postId,
            @RequestParam String board,
            @RequestParam(defaultValue = "2") int count) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getNeighbors(board, postId, count)));
    }

    @Operation(summary = "Get posts by board")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostListResponse>>> getPosts(
            @RequestParam String board,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<Long> tagIds,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(postService.searchPosts(board, title, tagIds, pageable)));
    }

    @Operation(summary = "Update post")
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(postService.updatePost(userId, postId, request)));
    }

    @Operation(summary = "Delete post")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId) {
        Long userId = jwtUtil.extractUserId(token);
        postService.deletePost(userId, postId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "Like post")
    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<Void>> likePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId) {
        Long userId = jwtUtil.extractUserId(token);
        postService.likePost(userId, postId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "Unlike post")
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<Void>> unlikePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId) {
        Long userId = jwtUtil.extractUserId(token);
        postService.unlikePost(userId, postId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "Check post like")
    @GetMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<Boolean>> isLikedPost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(postService.isLikedPost(userId, postId)));
    }
}
