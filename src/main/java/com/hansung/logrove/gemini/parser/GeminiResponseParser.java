package com.hansung.logrove.gemini.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansung.logrove.gemini.dto.GeminiEvaluationResponse;
import com.hansung.logrove.gemini.dto.GeminiTagResponse;
import com.hansung.logrove.tag.entity.TagName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GeminiResponseParser {

    private final ObjectMapper objectMapper;


    public GeminiTagResponse parseTagResponse(String rawText) {
        List<TagName> tags = new ArrayList<>();
        String[] parts = rawText.trim().split(",");

        for (String part : parts) {
            String trimmed = part.trim();
            try {
                tags.add(TagName.valueOf(trimmed));
            } catch (IllegalArgumentException e) {
                // Gemini가 유효하지 않은 태그를 반환한 경우 무시
            }
        }

        return new GeminiTagResponse(tags);
    }

    public GeminiEvaluationResponse parseEvaluationResponse(String rawText, int passScore) {
        try {
            String json = extractJson(rawText);
            JsonNode node = objectMapper.readTree(json);

            int score = node.get("score").asInt();
            String feedback = node.get("feedback").asText();
            boolean passed = score >= passScore;

            return new GeminiEvaluationResponse(score, passed, feedback);

        } catch (Exception e) {
            throw new RuntimeException("Gemini 채점 응답 파싱 실패: " + e.getMessage());
        }
    }

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start == -1 || end == -1) {
            throw new RuntimeException("JSON 형식을 찾을 수 없음: " + text);
        }
        return text.substring(start, end + 1);
    }
}