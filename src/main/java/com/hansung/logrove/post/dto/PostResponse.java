package com.hansung.logrove.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hansung.logrove.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private int view;
    private String boardType;
    private LocalDateTime createdAt;
    private Long userId;
    private String nickname;
    private List<String> imageUrls;
    private List<String> tagNames;
    private int likeCount;

    // @JsonProperty 없으면 Lombok이 isLiked() getter를 만들어 Jackson이 "liked"로 직렬화함
    @JsonProperty("isLiked")
    private boolean isLiked;

    // isLiked 상태를 알 수 없는 경우 (작성/수정 응답 등) — 기본값 false
    public static PostResponse from(Post post) {
        return from(post, false);
    }

    // 게시글 상세 조회 시 로그인 유저의 좋아요 여부를 함께 반환
    public static PostResponse from(Post post, boolean isLiked) {
        PostResponse dto = new PostResponse();
        dto.id = post.getId();
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.view = post.getView();
        dto.boardType = post.getBoardType().getBoard();
        dto.createdAt = post.getCreatedAt();
        dto.userId = post.getUser() != null ? post.getUser().getId() : null;
        dto.nickname = post.getUser() != null ? post.getUser().getNickname() : null;
        dto.imageUrls = post.getImages().stream()
                .map(img -> img.getUrl())
                .toList();
        dto.tagNames = post.getTags().stream()
                .map(pt -> pt.getTag().getName())
                .toList();
        dto.likeCount = post.getLikes().size();
        dto.isLiked = isLiked;
        return dto;
    }
}
