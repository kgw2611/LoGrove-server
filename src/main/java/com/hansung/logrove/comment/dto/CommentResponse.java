package com.hansung.logrove.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hansung.logrove.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentResponse {

    private Long commentId;
    private Long parentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long userId;
    private String nickname;
    private String profileUrl;

    private int likeCount;

    @JsonProperty("isLiked")
    private boolean isLiked;

    private List<CommentResponse> replies;

    public static CommentResponse from(Comment comment, boolean isLiked) {
        return from(comment, isLiked, List.of());
    }

    public static CommentResponse from(Comment comment, boolean isLiked, List<CommentResponse> replies) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .userId(comment.getUser().getId())
                .nickname(comment.getUser().getNickname())
                .profileUrl(comment.getUser().getProfileUrl())
                .likeCount(comment.getLikes().size())
                .isLiked(isLiked)
                .replies(replies)
                .build();
    }
}