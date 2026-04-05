package com.hansung.logrove.post.dto;

import com.hansung.logrove.post.entity.BoardType;
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
    private BoardType boardType;
    private LocalDateTime createdAt;
    private Long userId;
    private String nickname;

    // 이미지 URL 목록
    private List<String> imageUrls;

    // 태그명 목록
    private List<String> tagNames;

    private int likeCount;

    // 엔티티 → DTO 변환은 정적 팩토리 메서드 패턴으로 일관성 유지
    public static PostResponse from(Post post) {
        PostResponse dto = new PostResponse();
        dto.id = post.getId();
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.view = post.getView();
        dto.boardType = post.getBoardType();
        dto.createdAt = post.getCreatedAt();
        dto.userId = post.getUser() != null ? post.getUser().getId() : null;
        dto.nickname = post.getUser() != null ? post.getUser().getNickname() : null;
        dto.imageUrls = post.getImages().stream()
                .map(img -> img.getUrl())
                .toList();
        dto.tagNames = post.getTags().stream()
                .map(pt -> pt.getTag().getName().name())
                .toList();
        dto.likeCount = post.getLikes().size();
        return dto;
    }
}