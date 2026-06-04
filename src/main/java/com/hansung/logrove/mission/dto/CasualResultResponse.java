package com.hansung.logrove.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CasualResultResponse {
    private Long id;
    private Integer score;
    private String reason;
    private String scoreReason;
    private String resultUrl;
    private LocalDateTime submittedAt;
}
