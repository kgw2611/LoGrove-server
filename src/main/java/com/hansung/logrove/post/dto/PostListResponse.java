package com.hansung.logrove.post.dto;

import com.hansung.logrove.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostListResponse {

    private Long id;
    private long rowNumber;
    private String title;
    private int view;
    private int likeCount;
    private String boardType;
    private LocalDateTime createdAt;
    private String nickname;
    private Integer authorLevel;
    private List<String> tagNames;
    private List<String> imageUrls;
    private List<ImageMeta> images;

    @Getter
    @AllArgsConstructor
    public static class ImageMeta {
        private String url;
        private Integer width;
        private Integer height;
    }

    public static PostListResponse from(Post post) {
        PostListResponse dto = new PostListResponse();
        dto.id = post.getId();
        dto.title = post.getTitle();
        dto.view = post.getView();
        dto.likeCount = post.getLikes().size();
        dto.boardType = post.getBoardType().getBoard();
        dto.createdAt = post.getCreatedAt();
        dto.nickname = post.getUser() != null ? post.getUser().getNickname() : null;
        dto.authorLevel = post.getUser() != null ? post.getUser().getLevel() : null;
        dto.tagNames = post.getTags().stream()
                .map(pt -> pt.getTag().getName())
                .toList();
        dto.imageUrls = post.getImages().stream()
                .map(img -> img.getUrl())
                .toList();
        dto.images = post.getImages().stream()
                .map(img -> new ImageMeta(img.getUrl(), img.getWidth(), img.getHeight()))
                .toList();
        return dto;
    }

    public void setRowNumber(long rowNumber) {
        this.rowNumber = rowNumber;
    }
}
