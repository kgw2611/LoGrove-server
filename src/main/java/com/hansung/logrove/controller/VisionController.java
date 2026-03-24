package com.hansung.logrove.controller;

import com.hansung.logrove.service.GeminiService; // 👈 추가
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/vision")
@RequiredArgsConstructor
public class VisionController {

    private final GeminiService geminiService; // 👈 GeminiService 주입 추가


    // 2. 새로운 제미나이 API (미션 채점용)
    @PostMapping("/gemini-analyze") // 👈 경로가 겹치지 않게 새로 생성
    public String analyzeWithGemini(
            @RequestParam("file") MultipartFile file,
            @RequestParam("missionTopic") String missionTopic) throws IOException {

        // 아까 만든 제미나이 서비스 호출!
//        return geminiService.analyzeMission(file, missionTopic);
        return geminiService.extractImageTags(file);
    }
}