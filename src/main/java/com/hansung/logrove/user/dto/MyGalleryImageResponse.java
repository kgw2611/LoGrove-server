package com.hansung.logrove.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyGalleryImageResponse {
    private Long id;
    private String imageUrl;
    private String source;       // "gallery" | "mission"
    private String title;
    private LocalDateTime createdAt;
    private Long referenceId;    // postId or missionResultId
}
