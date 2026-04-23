package com.hansung.logrove.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hansung.logrove.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private Long commentId;
    private String content;
    private LocalDateTime createdAt;

    // 작성자 정보 — 댓글 목록에서 닉네임 표시에 사용
    private Long userId;
    private String nickname;

    // 해당 댓글의 좋아요 수 — likes 컬렉션 size()로 계산
    private int likeCount;

    // 현재 로그인한 유저의 좋아요 여부
    // @JsonProperty 없으면 Lombok이 isLiked() getter를 만들어 Jackson이 "liked"로 직렬화함
    @JsonProperty("isLiked")
    private boolean isLiked;

    // 엔티티 → DTO 변환 팩토리 메서드
    // 컨트롤러/서비스에서 new 없이 CommentResponse.from(comment)로 간결하게 사용
    public static CommentResponse from(Comment comment, boolean isLiked) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .userId(comment.getUser().getId())
                .nickname(comment.getUser().getNickname())
                // 좋아요 수는 likes 리스트 크기로 계산 (별도 COUNT 쿼리 불필요)
                .likeCount(comment.getLikes().size())
                .isLiked(isLiked)
                .build();
    }
}