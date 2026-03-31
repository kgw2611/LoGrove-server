package com.hansung.logrove.comment.controller;

import com.hansung.logrove.comment.dto.CommentCreateRequest;
import com.hansung.logrove.comment.dto.CommentResponse;
import com.hansung.logrove.comment.service.CommentService;
import com.hansung.logrove.global.jwt.JwtUtil;
import com.hansung.logrove.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.ok(commentService.getComments(postId)));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> getComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        return ResponseEntity.ok(ApiResponse.ok(commentService.getComment(postId, commentId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CommentCreateRequest request) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(commentService.createComment(postId, userId, request)));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CommentCreateRequest request) {
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.ok(commentService.updateComment(postId, commentId, userId, request)));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        commentService.deleteComment(postId, commentId, userId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<ApiResponse<Void>> likeComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        commentService.likeComment(commentId, userId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<ApiResponse<Void>> unlikeComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);
        commentService.unlikeComment(commentId, userId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}