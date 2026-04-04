package com.hansung.logrove.mission.dto;

import com.hansung.logrove.mission.entity.MissionState;
import com.hansung.logrove.mission.entity.MissionStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionListResponse {
    private Long missionId;
    private String question;
    private MissionStatus state;

    public static MissionListResponse from(MissionState missionState) {
        return MissionListResponse.builder()
                .missionId(missionState.getMission().getId())
                .question(missionState.getMission().getQuestion())
                .state(missionState.getState())
                .build();
    }
}