package com.hansung.logrove.gemini.service;

import com.hansung.logrove.gemini.client.ClipClient;
import com.hansung.logrove.gemini.dto.GeminiTagResponse;
import com.hansung.logrove.gemini.parser.GeminiResponseParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ClipTaggingService {

    private final ClipClient clipClient;
    private final GeminiResponseParser geminiResponseParser;

    public GeminiTagResponse recommendTags(MultipartFile file) {
        // 1. 새로운 ClipClient를 통해 분석 요청
        String rawResponse = clipClient.requestTagging(file);

        // 2. 기존 파서 로직 그대로 활용 (JSON 형식이 동일하므로)
        return geminiResponseParser.parseTagResponse(rawResponse);
    }
}