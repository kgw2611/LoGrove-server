package com.hansung.logrove.gemini.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeminiEvaluationResponse {

    private int score;        // 0~100 점수
    private boolean passed;   // 통과 여부 (score >= passScore)
    private String feedback;  // Gemini의 평가 코멘트
}