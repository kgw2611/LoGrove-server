package com.hansung.logrove.gemini.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CasualEvaluationResponse {
    private Integer score;
    private String reason;
}
