package com.hansung.logrove.storage.provider;

import com.hansung.logrove.storage.dto.ImageUploadResult;
import com.hansung.logrove.storage.util.ImageConverter;
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

    private final String uploadDir;
    private final ImageConverter imageConverter;

    public LocalFileStorageProvider(
            @Value("${storage.upload-dir}") String uploadDir,
            ImageConverter imageConverter) {
        this.uploadDir = uploadDir;
        this.imageConverter = imageConverter;
    }

    public ImageUploadResult store(MultipartFile file, String subDir) {
        try {
            Path dirPath = Paths.get(uploadDir, subDir).toAbsolutePath();
            Files.createDirectories(dirPath);

            String savedFilename = UUID.randomUUID() + ".webp";
            Path filePath = dirPath.resolve(savedFilename);

            byte[] webpBytes = imageConverter.toWebP(file.getBytes(), resolveMaxDimension(subDir));
            Files.write(filePath, webpBytes);

            String accessUrl = "/images/" + subDir + "/" + savedFilename;
            return new ImageUploadResult(accessUrl, filePath.toString());

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage());
        }
    }

    public void delete(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패: " + e.getMessage());
        }
    }

    // profiles: 512px, submissions: 1024px, 그 외(posts/missions): 1920px
    private int resolveMaxDimension(String subDir) {
        if (subDir.startsWith("profiles/")) return 512;
        if (subDir.startsWith("submissions/")) return 1024;
        return 1920;
    }
}
