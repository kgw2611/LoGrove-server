package com.hansung.logrove.gemini.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        System.out.println(">>> GEMINI KEY PREFIX: " + apiKey.substring(0, 10));
        System.out.println(">>> TAG URL: " + tagUrl);
    }

    @Value("${gemini.api.tag-url}")
    private String tagUrl;

    @Value("${gemini.api.eval-url}")
    private String evalUrl;

    // 태그 추천용 (gemini-3-flash-preview)
    public String requestTagging(MultipartFile file, String prompt) {
        return call(tagUrl, file, prompt);
    }

    // 미션 채점용 (gemini-3.1-pro-preview)
    public String requestEvaluation(MultipartFile file, String prompt) {
        return call(evalUrl, file, prompt);
    }

    private String call(String url, MultipartFile file, String prompt) {
        try {
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            String mimeType = file.getContentType() != null ? file.getContentType() : "image/jpeg";

            // Gemini API 요청 바디 구성
            Map<String, Object> imagePart = Map.of(
                    "inline_data", Map.of(
                            "mime_type", mimeType,
                            "data", base64Image
                    )
            );
            Map<String, Object> textPart = Map.of("text", prompt);

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(imagePart, textPart))
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            String urlWithKey = url + "?key=" + apiKey;
            ResponseEntity<Map> response = restTemplate.postForEntity(urlWithKey, entity, Map.class);

            return extractText(response.getBody());

        } catch (IOException e) {
            throw new RuntimeException("Gemini API 호출 실패: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map responseBody) {
        try {
            List<Map> candidates = (List<Map>) responseBody.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                throw new RuntimeException("Gemini 응답에 candidates가 없습니다: " + responseBody);
            }
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            throw new RuntimeException("Gemini 응답 파싱 실패: " + e.getMessage());
        }
    }
}