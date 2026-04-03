package com.hansung.logrove.mission.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionResultResponse {
    private String feedback;    // AI 분석 피드백
    private boolean isSuccess;  // 미션 통과 여부
}