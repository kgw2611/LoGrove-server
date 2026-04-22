package com.hansung.logrove.post.dto;

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

    public static PostResponse from(Post post) {
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
                .map(pt -> pt.getTag().getName().name())
                .toList();
        dto.likeCount = post.getLikes().size();
        return dto;
    }
}
