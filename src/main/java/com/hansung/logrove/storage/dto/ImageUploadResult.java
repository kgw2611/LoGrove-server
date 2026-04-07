package com.hansung.logrove.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageUploadResult {

    private String url;        // 접근 URL (예: /images/posts/1/uuid.jpg)
    private String filePath;   // 실제 저장 경로 (예: ./uploads/posts/1/uuid.jpg)
}