package com.hansung.logrove.storage.service;

import com.hansung.logrove.storage.dto.ImageUploadResult;
import com.hansung.logrove.storage.provider.LocalFileStorageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageStorageService {

    private final LocalFileStorageProvider storageProvider;

    // 게시글 이미지 저장 (예: /images/posts/{postId}/uuid.jpg)
    public ImageUploadResult storePostImage(Long postId, MultipartFile file) {
        return storageProvider.store(file, "posts/" + postId);
    }

    // 미션 샘플 이미지 저장 (예: /images/missions/{missionId}/uuid.jpg)
    public ImageUploadResult storeMissionImage(Long missionId, MultipartFile file) {
        return storageProvider.store(file, "missions/" + missionId);
    }

    // 유저 미션 제출 사진 저장 (예: /images/submissions/{userId}/uuid.jpg)
    public ImageUploadResult storeSubmissionImage(Long userId, MultipartFile file) {
        return storageProvider.store(file, "submissions/" + userId);
    }

    public void delete(String filePath) {
        storageProvider.delete(filePath);
    }
}