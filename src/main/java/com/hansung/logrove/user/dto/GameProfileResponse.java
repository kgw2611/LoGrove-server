package com.hansung.logrove.user.dto;

import com.hansung.logrove.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameProfileResponse {

    private static final int[] LEVEL_THRESHOLDS = {0, 500, 1500, 3000, 5500, 9000, 13300};

    private String nickname;
    private Integer level;
    private Integer exp;
    private Integer progress;

    public static GameProfileResponse from(User user) {
        int lv = user.getLevel();
        int exp = user.getExp();
        int prevThreshold = LEVEL_THRESHOLDS[Math.min(lv - 1, LEVEL_THRESHOLDS.length - 1)];

        return GameProfileResponse.builder()
                .nickname(user.getNickname())
                .level(lv)
                .exp(exp)
                .progress(exp - prevThreshold)
                .build();
    }
}