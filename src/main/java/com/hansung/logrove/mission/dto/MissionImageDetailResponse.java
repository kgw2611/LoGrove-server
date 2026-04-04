package com.hansung.logrove.mission.dto;

import com.hansung.logrove.mission.entity.MissionImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionImageDetailResponse {
    private Long missionId;
    private String theme;
    private String content;
    private String guide;
    private String sampleUrl;
    private Integer passScore;

    public static MissionImageDetailResponse from(MissionImage missionImage) {
        return MissionImageDetailResponse.builder()
                .missionId(missionImage.getId())
                .theme(missionImage.getTheme())
                .content(missionImage.getContent())
                .guide(missionImage.getGuide())
                .sampleUrl(missionImage.getSampleUrl())
                .passScore(missionImage.getPassScore())
                .build();
    }
}