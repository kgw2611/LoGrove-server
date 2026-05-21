package com.hansung.logrove.gemini.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hansung.logrove.gemini.dto.CasualEvaluationResponse;
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
            String reason = node.get("reason").asText();
            boolean passed = score >= passScore;

            return new GeminiEvaluationResponse(score, passed, reason);

        } catch (Exception e) {
            return new GeminiEvaluationResponse(0, false, "사진이 주제와 관련 없거나 분석할 수 없습니다.");
        }
    }

    public CasualEvaluationResponse parseCasualResponse(String rawText) {
        try {
            String json = extractJson(rawText);
            JsonNode node = objectMapper.readTree(json);

            int score = node.path("score").asInt(0);
            String reason = node.path("reason").asText("평가 결과를 받지 못했어요.");

            return new CasualEvaluationResponse(score, reason);
        } catch (Exception e) {
            return new CasualEvaluationResponse(0, "평가 결과를 받지 못했어요.");
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
