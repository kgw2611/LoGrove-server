package com.hansung.logrove.mission.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionResultResponse {
    private String feedback;
    private Boolean isSuccess;  // boolean → Boolean (null 표현 가능)
}