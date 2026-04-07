package com.hansung.logrove.storage.provider;

import com.hansung.logrove.storage.dto.ImageUploadResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class LocalFileStorageProvider {

    @Value("${storage.upload-dir}")
    private String uploadDir;

    public ImageUploadResult store(MultipartFile file, String subDir) {
        try {
            // 저장 경로 생성 (예: ./uploads/posts/1)
            Path dirPath = Paths.get(uploadDir, subDir).toAbsolutePath();
            Files.createDirectories(dirPath);

            // 원본 확장자 유지하며 UUID로 파일명 생성 (충돌 방지)
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String savedFilename = UUID.randomUUID() + extension;

            // 실제 파일 저장
            Path filePath = dirPath.resolve(savedFilename);
            file.transferTo(filePath.toFile());

            // 접근 URL 생성 (예: /images/posts/1/uuid.jpg)
            String accessUrl = "/images/" + subDir + "/" + savedFilename;

            return new ImageUploadResult(accessUrl, filePath.toString());

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage());
        }
    }

    public void delete(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패: " + e.getMessage());
        }
    }
}