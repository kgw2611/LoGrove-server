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
        try {
            String json = extractJson(rawText);
            JsonNode node = objectMapper.readTree(json);
            JsonNode tagsNode = node.get("tags");

            if (tagsNode != null && tagsNode.isArray()) {
                for (JsonNode tagNode : tagsNode) {
                    String korean = tagNode.asText().trim();
                    TagName tagName = TagName.fromKorean(korean);
                    if (tagName != null) {
                        tags.add(tagName);
                    }
                    // null이면 Gemini가 목록에 없는 태그를 반환한 것 → 무시
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Gemini 태그 응답 파싱 실패: " + e.getMessage());
        }
        return new GeminiTagResponse(tags);
    }

    public GeminiEvaluationResponse parseEvaluationResponse(String rawText, int passScore) {
        try {
            String json = extractJson(rawText);
            JsonNode node = objectMapper.readTree(json);

            int score = node.get("score").asInt();
            String reason = node.get("reason").asText(); // 프롬프트 필드명과 일치
            boolean passed = score >= passScore;

            return new GeminiEvaluationResponse(score, passed, reason);

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