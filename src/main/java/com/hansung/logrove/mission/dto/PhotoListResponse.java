package com.hansung.logrove.mission.dto;

import com.hansung.logrove.mission.entity.MissionImageResult;
import com.hansung.logrove.mission.entity.MissionResultStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PhotoListResponse {
    private Long resultId;
    private Long missionId;
    private String theme;
    private Integer score;
    private MissionResultStatus result;
    private String resultUrl;
    private LocalDateTime submittedAt;

    public static PhotoListResponse from(MissionImageResult result) {
        return PhotoListResponse.builder()
                .resultId(result.getId())
                .missionId(result.getMission().getId())
                .theme(result.getMission().getMissionImage().getTheme())
                .score(result.getScore())
                .result(result.getResult())
                .resultUrl(result.getResultUrl())
                .submittedAt(result.getSubmittedAt())
                .build();
    }
}