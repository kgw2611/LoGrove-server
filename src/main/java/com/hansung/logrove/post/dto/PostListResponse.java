package com.hansung.logrove.post.dto;

import com.hansung.logrove.post.entity.BoardType;
import com.hansung.logrove.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

// 목록 조회용 — PostResponse와 달리 content(본문)는 제외해 응답 크기 최소화
@Getter
public class PostListResponse {

    private Long id;
    private String title;
    private int view;
    private int likeCount;
    private BoardType boardType;
    private LocalDateTime createdAt;
    private String nickname;
    private List<String> tagNames;

    public static PostListResponse from(Post post) {
        PostListResponse dto = new PostListResponse();
        dto.id = post.getId();
        dto.title = post.getTitle();
        dto.view = post.getView();
        dto.likeCount = post.getLikes().size();
        dto.boardType = post.getBoardType();
        dto.createdAt = post.getCreatedAt();
        dto.nickname = post.getUser() != null ? post.getUser().getNickname() : null;
        dto.tagNames = post.getTags().stream()
                .map(pt -> pt.getTag().getName())
                .toList();
        return dto;
    }
}