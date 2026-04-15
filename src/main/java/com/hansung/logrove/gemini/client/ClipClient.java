package com.hansung.logrove.gemini.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ClipClient {

    private final RestTemplate restTemplate;

    @Value("${ai.server.url:http://localhost:8000/api/analyze-tags}")
    private String aiServerUrl;

    public String requestTagging(MultipartFile file) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", file.getResource());

            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

            // FastAPI 서버 호출 및 결과 반환
            return restTemplate.postForObject(aiServerUrl, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("CLIP AI 서버 호출 실패: " + e.getMessage());
        }
    }
}